package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.MedicalStaffDao;
import com.kosa2.hospital.dto.MedicalStaffDto;
import com.kosa2.hospital.model.MedicalStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalStaffService {

    private final MedicalStaffDao medicalStaffDao;

    // 회원가입
    public void join(MedicalStaffDto dto) {
        medicalStaffDao.findByMedicalId(dto.getMedicalId())
                .ifPresent(m -> { throw new IllegalStateException("이미 사용 중인 아이디입니다."); });


        MedicalStaff staff = MedicalStaff.builder()
                .medicalId(dto.getMedicalId())
                .pwd(dto.getPwd())
                .mname(dto.getMName())
                .specialty(dto.getSpecialty())
                .mphone(dto.getMPhone())
                .email(dto.getEmail())
                .power(1)                 // 기본 normal
                .build();

        medicalStaffDao.insert(staff);
    }
    // 의료진 목록 조회
    public List<MedicalStaff> findAll() {
        return medicalStaffDao.findAll();
    }

    // 권한변경
    public MedicalStaff findById(Long medicalNum) {
        return medicalStaffDao.findById(medicalNum)
                .orElseThrow(() -> new IllegalArgumentException("의료진을 찾을 수 없습니다."));
    }


    // 정보 수정 (이름/전공/전화/이메일)
    public void updateProfile(MedicalStaffDto dto) {
        MedicalStaff staff = findById(dto.getMedicalNum());
        staff.setMedicalId(dto.getMedicalId());
        staff.setMname(dto.getMName());
        staff.setSpecialty(dto.getSpecialty());
        staff.setMphone(dto.getMPhone());
        staff.setEmail(dto.getEmail());
        medicalStaffDao.updateProfile(staff);

        // 비밀번호 변경 요청이 있으면(옵션)
        if (dto.getPwd() != null && !dto.getPwd().isBlank()) {
            medicalStaffDao.updatePassword(dto.getMedicalNum(), dto.getPwd());
        }
    }

    // 권한 변경
    public void updatePower(MedicalStaffDto dto) {
        if (dto.getPower() == null) return;
        medicalStaffDao.updatePower(dto.getMedicalNum(), dto.getPower());
    }

    public void delete(Long medicalNum) {
        medicalStaffDao.delete(medicalNum);
    }


    // 의료진 검색
    public List<MedicalStaff> findByMnameContaining(String mname) {
        // 검색어가 유효한지 확인하는 간단한 로직을 추가할 수 있습니다.
        if (mname == null || mname.trim().isEmpty()) {
            // 검색어가 없으면 전체 목록을 반환하거나 예외 처리
            return medicalStaffDao.findAll();
        }

        return medicalStaffDao.findByMnameContaining(mname.trim());
    }
}
