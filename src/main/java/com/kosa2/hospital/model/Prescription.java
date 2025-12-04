package com.kosa2.hospital.model;

import lombok.*;

// 처방
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    private int prescriptionNum; // 처방 번호
    private String medicationName; // 약물명
    private String dosage; // 복용량
    private int durationDays; // 복용기간

}
