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
    
    // 오늘 예약된 전체 환자 수
    public Integer countTodayReservations(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE reservation = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, date);
    }
    
    // 오늘 대기 중인 환자 수
    public Integer countWaitingReservations(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE reservation = ? AND status = 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, date);
    }
    
    // 오늘 신규 환자 수
    public Integer countTodayNewPatients(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM patient WHERE DATE(created_at) = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, date);
        } catch (Exception e) {
            return 0;
        }
    }
    
    // 오늘 일정 리스트
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
    
    // 모든 의사 정보 리스트
    public List<Map<String, Object>> getAllDoctors() {
        String sql = "SELECT mname, specialty FROM medical_staff ORDER BY mname";
        return jdbcTemplate.queryForList(sql);
    }
    
    // 이번 달 신규 환자 수
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

    // 예약 검색 (환자 이름 or 의사 이름 포함)
    public List<Map<String, Object>> searchReservations(String keyword) {
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
            WHERE p.name LIKE ? OR ms.mname LIKE ?
            ORDER BY r.reservation DESC
            """;
        
        String param = "%" + keyword + "%";
        return jdbcTemplate.queryForList(sql, param, param);
    }
}