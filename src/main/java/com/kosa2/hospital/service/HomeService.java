package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.HomeDao;
import com.kosa2.hospital.dto.HomeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HomeService {
    
    private final HomeDao homeDao;
    
    // 홈 화면 데이터 조회
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

    // 예약 검색 (환자 이름 or 의사 이름 포함)
    public List<Map<String, Object>> search(String keyword) {
        return homeDao.searchReservations(keyword);
    }
}