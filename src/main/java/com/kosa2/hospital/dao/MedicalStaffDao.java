package com.kosa2.hospital.dao;

// dao/MedicalStaffDao.java
import com.kosa2.hospital.model.MedicalStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MedicalStaffDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<MedicalStaff> medicalStaffRowMapper() {
        return (rs, rowNum) -> MedicalStaff.builder()
                .medicalNum(rs.getLong("medical_num"))
                .medicalId(rs.getString("medical_id"))
                .pwd(rs.getString("pwd"))
                .mname(rs.getString("mname"))
                .specialty(rs.getString("specialty"))
                .mphone(rs.getString("mphone"))
                .email(rs.getString("email"))
                .power(rs.getInt("power"))
                .build();
    }

    // 의료진 등록
    public void insert(MedicalStaff staff) {
        String sql = "INSERT INTO medical_staff " +
                "(medical_id, pwd, mname, specialty, mphone, email, power) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                staff.getMedicalId(),
                staff.getPwd(),
                staff.getMname(),
                staff.getSpecialty(),
                staff.getMphone(),
                staff.getEmail(),
                staff.getPower());
    }

    // 로그인 검증
    public Optional<MedicalStaff> findByMedicalId(String medicalId) {
        String sql = "SELECT * FROM medical_staff WHERE medical_id = ?";
        List<MedicalStaff> result = jdbcTemplate.query(sql, medicalStaffRowMapper(), medicalId);
        return result.stream().findFirst();
    }

    // 번호로 의료진 조회 (권한조회)
    public Optional<MedicalStaff> findById(Long medicalNum) {
        String sql = "SELECT * FROM medical_staff WHERE medical_num = ?";
        List<MedicalStaff> result = jdbcTemplate.query(sql, medicalStaffRowMapper(), medicalNum);
        return result.stream().findFirst();
    }

    // 의료진 목록 조회
    public List<MedicalStaff> findAll() {
        String sql = "SELECT * FROM medical_staff ORDER BY medical_num DESC";
        return jdbcTemplate.query(sql, medicalStaffRowMapper());
    }

    // 의료진 정보 수정
    public void updateProfile(MedicalStaff staff) {
        String sql = "UPDATE medical_staff " +
                "SET mname = ?, specialty = ?, mphone = ?, email = ? " +
                "WHERE medical_num = ?";
        jdbcTemplate.update(sql,
                staff.getMname(),
                staff.getSpecialty(),
                staff.getMphone(),
                staff.getEmail(),
                staff.getMedicalNum());
    }

    // 비밀번호 변경
    public void updatePassword(Long medicalNum, String pwd) {
        String sql = "UPDATE medical_staff SET pwd = ? WHERE medical_num = ?";
        jdbcTemplate.update(sql, pwd, medicalNum);
    }

    // 권한변경
    public void updatePower(Long medicalNum, int power) {
        String sql = "UPDATE medical_staff SET power = ? WHERE medical_num = ?";
        jdbcTemplate.update(sql, power, medicalNum);
    }

    // 의료진 제명
    public void delete(Long medicalNum) {
        String sql = "DELETE FROM medical_staff WHERE medical_num = ?";
        jdbcTemplate.update(sql, medicalNum);
    }

    // 의료진 검새
    public List<MedicalStaff> findByMnameContaining(String mname) {
        String sql = "SELECT medical_num, medical_id, mname, pwd, specialty, mphone, email, power " +
                "FROM medical_staff WHERE mname LIKE ? ORDER BY medical_num";

        // 검색어에 '%'를 붙여 부분 일치 검색 (LIKE '%검색어%')
        String searchParam = "%" + mname + "%";

        return jdbcTemplate.query(sql, medicalStaffRowMapper(), searchParam);
    }
}
