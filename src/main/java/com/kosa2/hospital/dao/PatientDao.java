package com.kosa2.hospital.dao;

import com.kosa2.hospital.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PatientDao {

    private final JdbcTemplate jdbcTemplate;

    // 목록 조회
    public List<Patient> findAll(String keyword) {
        String sql = "SELECT * FROM patient";
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE name LIKE ? OR phone LIKE ?";
            String param = "%" + keyword + "%";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class), param, param);
        }
        
        sql += " ORDER BY patient_num DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Patient.class));
    }

    // 상세 조회
    public Patient findById(int id) {
        String sql = "SELECT * FROM patient WHERE patient_num = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Patient.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    // 등록
    public void insert(Patient p) {
        String sql = """
            INSERT INTO patient (name, phone, birth_date, gender, created_at) 
            VALUES (?, ?, ?, ?, NOW())
        """;
        int genderValue = (p.getGender() != null) ? p.getGender().ordinal() : 0;
        
        jdbcTemplate.update(sql, 
            p.getName(), 
            p.getPhone(), 
            p.getBirth_date(), 
            genderValue 
        );
    }

    // 수정
    public void update(Patient p) {
        String sql = "UPDATE patient SET name=?, phone=?, birth_date=?, gender=? WHERE patient_num=?";
        
        int genderValue = (p.getGender() != null) ? p.getGender().ordinal() : 0;
        
        jdbcTemplate.update(sql, 
            p.getName(), 
            p.getPhone(), 
            p.getBirth_date(), 
            genderValue,
            p.getPatient_num()
        );
    }

    // 삭제 관련 메서드
    // 1. 처방 내역 삭제
    public void deletePrescriptionsByPatientId(int patientId) {
        String sql = """
            DELETE FROM prescription 
            WHERE treatment_num IN (
                SELECT treatment_num FROM treatment 
                WHERE reservation_num IN (
                    SELECT reservation_num FROM reservation 
                    WHERE patient_num = ?
                )
            )
        """;
        jdbcTemplate.update(sql, patientId);
    }

    // 2. 진료 내역 삭제
    public void deleteTreatmentsByPatientId(int patientId) {
        String sql = """
            DELETE FROM treatment 
            WHERE reservation_num IN (
                SELECT reservation_num FROM reservation 
                WHERE patient_num = ?
            )
        """;
        jdbcTemplate.update(sql, patientId);
    }

    // 3. 예약 내역 삭제
    public void deleteReservationsByPatientId(int patientId) {
        String sql = "DELETE FROM reservation WHERE patient_num = ?";
        jdbcTemplate.update(sql, patientId);
    }

    // 4. 환자 본인 삭제
    public void delete(int id) {
        String sql = "DELETE FROM patient WHERE patient_num = ?";
        jdbcTemplate.update(sql, id);
    }
}