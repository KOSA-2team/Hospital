package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.ReservationDao;
import com.kosa2.hospital.dto.ReservationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<ReservationDto> getAllReservations() {
        return reservationDao.findAll();
    }

    // 중복 검사 후 저장
    @Transactional
    public void createReservation(ReservationDto dto) {
        // 1. 해당 의사가 그 시간에 예약이 있는지 확인
        if (reservationDao.isDuplicate(dto.getMedicalNum(), dto.getReservationDate())) {
            throw new IllegalStateException("이미 해당 시간에 예약이 존재합니다.");
        }
        // 2. 없으면 저장
        reservationDao.save(dto);
    }

    // 핵심 로직: 예약 취소
    public void cancelReservation(Long id) {
        reservationDao.cancel(id);
    }
}