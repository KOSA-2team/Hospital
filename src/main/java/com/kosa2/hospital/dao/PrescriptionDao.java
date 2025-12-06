package com.kosa2.hospital.dao;

import com.kosa2.hospital.model.Prescription;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrescriptionDao {

    private final JdbcTemplate jdbcTemplate;

    public PrescriptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 처방전 INSERT
    public void insert(Prescription p) {
        String sql = "INSERT INTO prescription (treatment_num, medication_name, dosage, duration_days) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, p.getTreatmentNum(), p.getMedicationName(), p.getDosage(), p.getDurationDays());
    }

    // 특정 진료(treatmentNum)에 연결된 처방전 리스트 조회
    public List<Prescription> findByTreatmentId(Long treatmentNum) {
        String sql = "SELECT * FROM prescription WHERE treatment_num = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Prescription.builder()
                .prescriptionNum(rs.getLong("prescription_num"))
                .treatmentNum(rs.getLong("treatment_num"))
                .medicationName(rs.getString("medication_name"))
                .dosage(rs.getString("dosage"))
                .durationDays(rs.getInt("duration_days"))
                .build(), treatmentNum);
    }

    // 기존 처방전 삭제(DELETE)
    public void deleteByTreatmentId(Long treatmentNum) {
        String sql = "DELETE FROM prescription WHERE treatment_num = ?";
        jdbcTemplate.update(sql, treatmentNum);
    }
}