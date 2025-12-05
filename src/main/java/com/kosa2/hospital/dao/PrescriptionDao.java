package com.kosa2.hospital.dao;

import com.kosa2.hospital.dto.PrescriptionDto;
import com.kosa2.hospital.dto.RecordFormDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PrescriptionDao {

    private final JdbcTemplate jdbcTemplate;

    public PrescriptionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 처방전 전체 목록 조회
    public List<PrescriptionDto> findAll() {
        String sql = """
            SELECT p.prescription_num, p.medication_name, p.duration_days, p.dosage, p.treatment_num
            FROM Prescription p
            JOIN treatment t ON p.treatment_num = t.treatment_num
            ORDER BY p.prescription_num ASC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return PrescriptionDto.builder()
                    // DB에서 꺼낸 값을 바로 조립
                    .prescriptionNum(rs.getLong("prescription_num"))
                    .medicationName(rs.getString("medication_name"))
                    .durationDays(rs.getInt("duration_days"))
                    .dosage(rs.getString("dosage"))
                    .treatmentNum(rs.getLong(("treatment_num")))
                    .build();
        });
    }
}
