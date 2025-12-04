package com.kosa2.hospital.dao;

import com.kosa2.hospital.dto.PatientDto;
import com.kosa2.hospital.enums.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PatientDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<PatientDto> patientRowMapper = new RowMapper<>() {
        @Override
        public PatientDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            PatientDto dto = new PatientDto();
            dto.setPatient_num(rs.getInt("patient_num"));
            dto.setName(rs.getString("name"));
            dto.setPhone(rs.getString("phone"));
            java.sql.Date bd = rs.getDate("birth_date");
            if (bd != null) dto.setBirth_date(bd.toLocalDate());
            try {
                int g = rs.getInt("gender");
                Gender[] vals = Gender.values();
                if (g >= 0 && g < vals.length) dto.setGender(vals[g]);
                else dto.setGender(Gender.UNKNOWN);
            } catch (SQLException e) {
                dto.setGender(Gender.UNKNOWN);
            }
            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) dto.setCreated_at(ts.toLocalDateTime());
            return dto;
        }
    };

    // 전체 조회 (이름 or 전화번호로 검색 가능)
    public List<PatientDto> findAll(String keyword) {
        String sql = "SELECT * FROM patient";

        if (keyword != null && !keyword.isEmpty()) {
            sql += " WHERE name LIKE ? OR phone LIKE ?";
            String param = "%" + keyword + "%";
            sql += " ORDER BY patient_num DESC";
            return jdbcTemplate.query(sql, patientRowMapper, param, param);
        }

        sql += " ORDER BY patient_num DESC";
        return jdbcTemplate.query(sql, patientRowMapper);
    }

    // 상세 조회
    public PatientDto findById(int id) {
        String sql = "SELECT * FROM patient WHERE patient_num = ?";
        return jdbcTemplate.queryForObject(sql, patientRowMapper, id);
    }

    // 등록
    public void insert(PatientDto dto) {
        String sql = "INSERT INTO patient (name, phone, birth_date, gender, created_at) VALUES (?, ?, ?, ?, NOW())";
        Integer genderCode = dto.getGender() != null ? dto.getGender().ordinal() : Gender.UNKNOWN.ordinal();
        jdbcTemplate.update(sql, dto.getName(), dto.getPhone(), dto.getBirth_date(), genderCode);
    }

    // 수정
    public void update(PatientDto dto) {
        String sql = "UPDATE patient SET name=?, phone=?, birth_date=?, gender=?, created_at=? WHERE patient_num=?";
        Integer genderCode = dto.getGender() != null ? dto.getGender().ordinal() : Gender.UNKNOWN.ordinal();
        jdbcTemplate.update(sql, dto.getName(), dto.getPhone(), dto.getBirth_date(), genderCode, dto.getCreated_at(), dto.getPatient_num());
    }

    // 삭제
    public void delete(int id) {
        String sql = "DELETE FROM patient WHERE patient_num = ?";
        jdbcTemplate.update(sql, id);
    }

    // 특정 환자에 대한 예약 수 조회 (FK 체크용)
    public Integer countReservationsByPatient(int patientNum) {
        String sql = "SELECT COUNT(*) FROM reservation WHERE patient_num = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, patientNum);
        } catch (Exception e) {
            return 0;
        }
    }
}