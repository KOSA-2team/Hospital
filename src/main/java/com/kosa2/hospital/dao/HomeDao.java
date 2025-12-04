package com.kosa2.hospital.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// TODO : 후에 클래스 삭제 혹은 다른 DAO를 조합하는 역할 변경
@Repository
@RequiredArgsConstructor
public class HomeDao {
    
    private final JdbcTemplate jdbcTemplate;
    
    public Integer countTodayReservations(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE reservation = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, date);
    }
    
    public Integer countWaitingReservations(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE reservation = ? AND status = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, date);
    }
    
    public Integer countTodayNewPatients(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM patient WHERE DATE(created_at) = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, date);
        } catch (Exception e) {
            return 0;
        }
    }
    
    public List<Map<String, Object>> getTodayScheduleList(LocalDate date) {
        String sql = """
            SELECT r.reservation_num AS id, 
                   r.reservation AS time, 
                   r.status,
                   p.name AS patient_name, 
                   p.patient_num AS patient_id,
                   ms.mname AS doctor_name
            FROM reservation r
            INNER JOIN patient p ON r.patient_num = p.patient_num
            INNER JOIN medical_staff ms ON r.medical_num = ms.medical_num
            WHERE r.reservation = ?
            ORDER BY r.reservation ASC
            """;
        return jdbcTemplate.queryForList(sql, date);
    }
    
    public List<Map<String, Object>> getAllDoctors() {
        String sql = "SELECT mname, specialty FROM medical_staff ORDER BY mname";
        return jdbcTemplate.queryForList(sql);
    }
    
    public Integer countMonthlyNewPatients(LocalDate date) {
        String sql = """
            SELECT COUNT(*) FROM patient 
            WHERE YEAR(created_at) = ? AND MONTH(created_at) = ?
            """;
        try {
            return jdbcTemplate.queryForObject(
                sql, 
                Integer.class, 
                date.getYear(), 
                date.getMonthValue()
            );
        } catch (Exception e) {
            return 0;
        }
    }
}