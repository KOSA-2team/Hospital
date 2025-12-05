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

    public Optional<MedicalStaff> findByMedicalId(String medicalId) {
        String sql = "SELECT * FROM medical_staff WHERE medical_id = ?";
        List<MedicalStaff> result = jdbcTemplate.query(sql, medicalStaffRowMapper(), medicalId);
        return result.stream().findFirst();
    }

    public Optional<MedicalStaff> findById(Long medicalNum) {
        String sql = "SELECT * FROM medical_staff WHERE medical_num = ?";
        List<MedicalStaff> result = jdbcTemplate.query(sql, medicalStaffRowMapper(), medicalNum);
        return result.stream().findFirst();
    }

    public List<MedicalStaff> findAll() {
        String sql = "SELECT * FROM medical_staff ORDER BY medical_num DESC";
        return jdbcTemplate.query(sql, medicalStaffRowMapper());
    }

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

    public void updatePassword(Long medicalNum, String pwd) {
        String sql = "UPDATE medical_staff SET pwd = ? WHERE medical_num = ?";
        jdbcTemplate.update(sql, pwd, medicalNum);
    }

    public void updatePower(Long medicalNum, int power) {
        String sql = "UPDATE medical_staff SET power = ? WHERE medical_num = ?";
        jdbcTemplate.update(sql, power, medicalNum);
    }

    public void delete(Long medicalNum) {
        String sql = "DELETE FROM medical_staff WHERE medical_num = ?";
        jdbcTemplate.update(sql, medicalNum);
    }
}
