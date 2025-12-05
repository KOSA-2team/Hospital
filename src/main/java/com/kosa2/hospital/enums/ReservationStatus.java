package com.kosa2.hospital.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    // 1. 정의: 이름(숫자, 설명)
    RESERVED(0, "예약 됨"),
    CONFIRMED(1, "진료 대기"),
    CANCELED(2, "취소 됨"),
    COMPLETED(3, "진료 완료");

    // 2. 필드
    private final int code;      // DB에 들어갈 숫자 (0, 1, 2, 3)
    private final String desc;   // 화면에 보여줄 한글 설명

    // 3. 생성자
    ReservationStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
