package com.kosa2.hospital.dao;

import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.enums.ReservationStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. 목록 조회
    public List<ReservationDto> findAll() {
        String sql = """
            SELECT r.reservation_num, r.reservation, r.status,
                   r.patient_num, r.medical_num,  --
                   p.name AS patient_name,
                   m.mname AS doctor_name
            FROM Reservation r
            JOIN patient p ON r.patient_num = p.patient_num
            JOIN medical_staff m ON r.medical_num = m.medical_num
            ORDER BY r.reservation DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ReservationDto dto = new ReservationDto();
            dto.setReservationNum(rs.getLong("reservation_num"));
            dto.setReservationDate(rs.getTimestamp("reservation").toLocalDateTime());
            dto.setStatus(rs.getInt("status"));

            dto.setPatientNum(rs.getLong("patient_num"));
            dto.setMedicalNum(rs.getLong("medical_num"));

            dto.setPatientName(rs.getString("patient_name"));
            dto.setDoctorName(rs.getString("doctor_name"));
            return dto;
        });
    }

    // 2. 중복 예약 확인
    public boolean isDuplicate(Long medicalNum, LocalDateTime time) {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE medical_num = ? AND reservation = ? AND status != ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,
                medicalNum, time, ReservationStatus.CANCELED.getCode());

        return count != null && count > 0;
    }

    // 3. 예약 등록
    public void save(ReservationDto dto) {
        String sql = "INSERT INTO Reservation (patient_num, medical_num, reservation, status) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                dto.getPatientNum(),
                dto.getMedicalNum(),
                dto.getReservationDate(),
                ReservationStatus.RESERVED.getCode()); // 초기 상태는 무조건 '예약됨'
    }

    // 4. 예약 취소
    public void cancel(Long id) {
        String sql = "UPDATE Reservation SET status = ? WHERE reservation_num = ?";

        jdbcTemplate.update(sql, ReservationStatus.CANCELED.getCode(), id);
    }

    // 5. 예약 상세 조회
    public ReservationDto findById(Long id) {
        String sql = """
            SELECT r.reservation_num, r.reservation, r.status,
                   r.patient_num, r.medical_num,
                   p.name AS patient_name,
                   m.mname AS doctor_name
            FROM Reservation r
            JOIN patient p ON r.patient_num = p.patient_num
            JOIN medical_staff m ON r.medical_num = m.medical_num
            WHERE r.reservation_num = ?
        """;

        // 결과가 없으면 예외를 던지거나 null 반환
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ReservationDto dto = new ReservationDto();
            dto.setReservationNum(rs.getLong("reservation_num"));
            dto.setReservationDate(rs.getTimestamp("reservation").toLocalDateTime());
            dto.setStatus(rs.getInt("status"));
            dto.setPatientNum(rs.getLong("patient_num"));
            dto.setMedicalNum(rs.getLong("medical_num"));
            dto.setPatientName(rs.getString("patient_name"));
            dto.setDoctorName(rs.getString("doctor_name"));
            return dto;
        }, id);
    }

    // [추가] 예약 상태 변경 (진료 완료 처리 등)
    public void updateStatus(Long reservationNum, int status) {
        String sql = "UPDATE Reservation SET status = ? WHERE reservation_num = ?";
        jdbcTemplate.update(sql, status, reservationNum);
    }
}