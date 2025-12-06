package com.kosa2.hospital.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class HomeDto {
    private LocalDate todayDate;                    // 오늘 날짜
    private Integer totalCount;                     // 전체 환자 수
    private Integer waitingCount;                   // 대기 중인 환자 수
    private Integer newPatientCount;                // 신규 환자 수
    private List<Map<String, Object>> scheduleList; // 오늘 일정 리스트
    private List<Map<String, Object>> doctorList;   // 의사 정보 리스트
    private Integer monthlyNewCount;                // 이번 달 신규 환자 수
}