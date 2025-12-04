package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.PatientDao;
import com.kosa2.hospital.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientDao patientDao;

    // 환자 목록 조회 (이름 or 전화번호로 검색 가능)
    public List<PatientDto> getPatientList(String keyword) {
        return patientDao.findAll(keyword);
    }

    // 환자 상세 조회
    public PatientDto getPatientDetail(int id) {
        return patientDao.findById(id);
    }

    // 환자 등록
    public void registerPatient(PatientDto dto) {
        patientDao.insert(dto);
    }

    // 환자 수정
    public void updatePatient(PatientDto dto) {
        patientDao.update(dto);
    }

    // 환자 삭제
    public void deletePatient(int id) {
        Integer cnt = patientDao.countReservationsByPatient(id);
        if (cnt != null && cnt > 0) {
            throw new IllegalStateException("환자에게 연결된 예약이 있어 삭제할 수 없습니다.");
        }
        patientDao.delete(id);
    }
}