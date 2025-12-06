package com.kosa2.hospital.enums; // 패키지 위치는 편한 곳으로

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    UNKNOWN("알 수 없음"),   // 0
    MALE("남성"),           // 1
    FEMALE("여성");         // 2

    private final String description;
}