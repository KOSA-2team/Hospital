package com.kosa2.hospital.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class HomeDto {
    private LocalDate todayDate;
    private Integer totalCount;
    private Integer waitingCount;
    private Integer newPatientCount;
    private List<Map<String, Object>> scheduleList;
    private List<Map<String, Object>> doctorList;
    private Integer monthlyNewCount;
}