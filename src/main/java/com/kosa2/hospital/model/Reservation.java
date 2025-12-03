package com.kosa2.hospital.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Reservation {
    private Long reservationNum;    // PK (reservation_num)
    private Long patientNum;        // FK (patient_num)
    private Long medicalNum;        // FK (medical_num)
    private LocalDateTime reservation; // 예약 날짜 (reservation)
    private int status;             // 상태 (status) - 0:신청, 1:확인, 2:취소, 3:완료
}