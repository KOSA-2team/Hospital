package com.kosa2.hospital.model;

import lombok.*;

import java.time.LocalDateTime;

// 진료 기록
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Treatment {

    private Long treatmentNum; //PK : 진료 번호
    private Long reservationNum; // FK : 예약 번호 (reservation 테이블로부터 참조)
    private String diagnosis; // 진단명
    private String treatment; // 치료 내용
    private LocalDateTime treatmentDate; // 진료일

}
