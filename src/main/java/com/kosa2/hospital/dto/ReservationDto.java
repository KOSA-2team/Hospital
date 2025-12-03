package com.kosa2.hospital.dto;

import com.kosa2.hospital.enums.ReservationStatus; // ✨ Enum 임포트 필수!
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long reservationNum;
    private Long patientNum;
    private Long medicalNum;
    private LocalDateTime reservationDate;

    // DB에서 가져온 숫자값 (0, 1, 2...)
    private int status;

    private String patientName;
    private String doctorName;

    // 코드가 어떤 상태를 나타내는지에 대한 설명
    public String getStatusDescription() {
        for (ReservationStatus s : ReservationStatus.values()) {
            if (s.getCode() == this.status) {
                return s.getDesc();
            }
        }
        return "알 수 없음";
    }
}