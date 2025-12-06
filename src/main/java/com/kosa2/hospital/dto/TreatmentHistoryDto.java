package com.kosa2.hospital.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TreatmentHistoryDto {
    private Long treatmentNum;      // 진료 번호 (상세보기 링크용)
    private LocalDateTime treatmentDate; // 진료일
    private String diagnosis;       // 진단명
    private String doctorName;      // 담당 의사명
}