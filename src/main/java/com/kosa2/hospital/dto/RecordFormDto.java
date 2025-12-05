package com.kosa2.hospital.dto;

import com.kosa2.hospital.model.Prescription;
import lombok.*;
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
}
