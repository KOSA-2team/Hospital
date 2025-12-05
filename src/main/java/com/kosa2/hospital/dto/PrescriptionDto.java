package com.kosa2.hospital.dto;

import lombok.*;

// 처방전 한 줄 데이터
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDto {

    private Long prescriptionNum;
    private Long treatmentNum;
    private String medicationName;
    private int durationDays;
    private String dosage;

}