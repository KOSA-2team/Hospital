package com.kosa2.hospital.dao;

import com.kosa2.hospital.dto.RecordFormDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TreatmentDao {

    private final JdbcTemplate jdbcTemplate;

    public TreatmentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 진료 기록 전체 목록 조회
    public List<RecordFormDto> findAll() {
        String sql = """
            SELECT t.treatment_num, t.diagnosis, t.treatment, t.treatment_date, t.reservation_num
            FROM Treatment t
            JOIN reservation r ON t.reservation_num = r.reservation_num
            ORDER BY t.treatment_num ASC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return RecordFormDto.builder()
                    // DB에서 꺼낸 값을 바로 조립
                    .treatmentNum(rs.getLong("treatment_num"))
                    .diagnosis(rs.getString("diagnosis"))
                    .treatment(rs.getString("treatment"))
                    .treatmentDate(rs.getTimestamp("treatement_date").toLocalDateTime())
                    .reservationNum(rs.getLong("reservation_num"))
                    .build();

        });
    }

}
