package com.kosa2.hospital.model;

import com.kosa2.hospital.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    private Integer patient_num;        // PK
    private String name;                // 이름    
    private String phone;               // 전화번호
    private LocalDate birth_date;       // 생년월일
    private Gender gender;              // DB에는 0, 1, 2 (INT)로 저장
    private LocalDateTime created_at;   // 등록일시
}