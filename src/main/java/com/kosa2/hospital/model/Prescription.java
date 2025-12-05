package com.kosa2.hospital.model;

import lombok.*;

// 처방
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    private Long prescriptionNum; // PK : 처방 번호
    private Long TreatmentNum; // FK : 진료 번호
    private String medicationName; // 약물명
    private int durationDays; // 복용 기간 => 3일치
    private String dosage; // 복용 시간 => - 0:식후 30분, 1:식전 30분, 2:취침 전

}