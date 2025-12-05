package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.MedicalStaffDao;
import com.kosa2.hospital.model.MedicalStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MedicalStaffDao medicalStaffDao;

    public MedicalStaff login(String medicalId, String pwd) {
        return medicalStaffDao.findByMedicalId(medicalId)
                .filter(m -> m.getPwd().equals(pwd))
                .orElse(null);
    }
}
