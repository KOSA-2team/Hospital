package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.PatientDao;
import com.kosa2.hospital.dto.PatientDto;
import com.kosa2.hospital.model.Patient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientDao patientDao;

    // 목록 조회
    public List<PatientDto> getPatientList(String keyword) {
        List<Patient> models = patientDao.findAll(keyword);
        
        return models.stream()
                .map(PatientDto::from) 
                .collect(Collectors.toList());
    }

    // 상세 조회
    public PatientDto getPatientDetail(int id) {
        Patient model = patientDao.findById(id);
        if (model == null) return null;
        
        return PatientDto.from(model);
    }

    // 등록
    public void registerPatient(PatientDto dto) {
        patientDao.insert(dto.toModel());
    }

    // 수정
    public void updatePatient(PatientDto dto) {
        patientDao.update(dto.toModel());
    }

    // 삭제 (처방 → 진료 → 예약 → 환자)
    @Transactional
    public void deletePatient(int id) {
        // 1. 처방 삭제
        patientDao.deletePrescriptionsByPatientId(id);

        // 2. 진료 삭제
        patientDao.deleteTreatmentsByPatientId(id);

        // 3. 예약 삭제
        patientDao.deleteReservationsByPatientId(id);

        // 4. 환자 삭제
        patientDao.delete(id);
    }
}