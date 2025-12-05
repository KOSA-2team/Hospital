package com.kosa2.hospital.dto;

import com.kosa2.hospital.model.Prescription;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

// 진료 내용 + 처방리스트 담는 큰 리스트
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecordFormDto {

    // 처방전 목록 (PrescriptionDto를 여러 개 담을 수 있는 리스트)
    private List<Prescription> prescriptions;

    private Long treatmentNum; //PK : 진료 번호
    private Long reservationNum; // FK : 예약 번호 (reservation 테이블로부터 참조)
    private String diagnosis; // 진단명
    private String treatment; // 치료 내용
    private LocalDateTime treatmentDate; // 진료일

}
