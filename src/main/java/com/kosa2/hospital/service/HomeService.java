package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.HomeDao;
import com.kosa2.hospital.dto.HomeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HomeService {
    
    private final HomeDao homeDao;
    
    public HomeDto getHomeData() {
        LocalDate today = LocalDate.now();
        
        HomeDto dto = new HomeDto();
        dto.setTodayDate(today);
        dto.setTotalCount(homeDao.countTodayReservations(today));
        dto.setWaitingCount(homeDao.countWaitingReservations(today));
        dto.setNewPatientCount(homeDao.countTodayNewPatients(today));
        dto.setScheduleList(homeDao.getTodayScheduleList(today));
        dto.setDoctorList(homeDao.getAllDoctors());
        dto.setMonthlyNewCount(homeDao.countMonthlyNewPatients(today));
        
        return dto;
    }
}