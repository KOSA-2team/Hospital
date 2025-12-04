package com.kosa2.hospital.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.kosa2.hospital.enums.Gender;

@Data
public class PatientDto {
    private Integer patient_num;        // 환자번호(PK)
    private String name;                // 이름
    private String phone;               // 연락처
    private LocalDate birth_date;       // 생년월일
    private Gender gender;              // 성별 (0: 알 수 없음, 1: 남성, 2: 여성)
    private LocalDateTime created_at;   // 가입일
}
