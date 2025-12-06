package com.kosa2.hospital.dao;

import com.kosa2.hospital.dto.RecordFormDto;
import com.kosa2.hospital.model.Treatment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class TreatmentDao {

    private final JdbcTemplate jdbcTemplate;

    public TreatmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 진료기록 INSERT (PK 반환)
    public Long insert(Treatment treatment) {
        String sql = "INSERT INTO treatment (reservation_num, diagnosis, treatment, treatment_date) VALUES (?, ?, ?, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, treatment.getReservationNum());
            ps.setString(2, treatment.getDiagnosis());
            ps.setString(3, treatment.getTreatment());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    // 진료 기록 1개 조회 - PK(treatmentNum)
    public RecordFormDto findById(Long treatmentNum) {
        String sql = """
            SELECT t.treatment_num, t.diagnosis, t.treatment, t.treatment_date, t.reservation_num
            FROM Treatment t
            WHERE t.treatment_num = ?
        """;
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> RecordFormDto.builder()
                .treatmentNum(rs.getLong("treatment_num"))
                .diagnosis(rs.getString("diagnosis"))
                .treatment(rs.getString("treatment"))
                .treatmentDate(rs.getTimestamp("treatment_date").toLocalDateTime())
                .reservationNum(rs.getLong("reservation_num"))
                .build(), treatmentNum);
    }

    // 진료 기록 수정
    public void update(Treatment t) {
        String sql = "UPDATE treatment SET diagnosis = ?, treatment = ? WHERE treatment_num = ?";
        jdbcTemplate.update(sql, t.getDiagnosis(), t.getTreatment(), t.getTreatmentNum());
    }
}