package com.kosa2.hospital.service;

import com.kosa2.hospital.dao.PrescriptionDao;
import com.kosa2.hospital.dao.ReservationDao;
import com.kosa2.hospital.dao.TreatmentDao;
import com.kosa2.hospital.dto.PrescriptionDto;
import com.kosa2.hospital.dto.RecordFormDto;
import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.enums.ReservationStatus;
import com.kosa2.hospital.model.Prescription;
import com.kosa2.hospital.model.Treatment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final TreatmentDao treatmentDao;
    private final PrescriptionDao prescriptionDao;
    private final ReservationDao reservationDao;

    // 예약 정보 조회 (작성 폼용)
    public ReservationDto getReservationInfo(Long reservationNum) {
        return reservationDao.findById(reservationNum);
    }

    // 진료 기록 저장 [트랜잭션]
    @Transactional
    public Long saveRecord(RecordFormDto dto) {
        // 진료기록 INSERT
        Treatment treatment = Treatment.builder()
                .reservationNum(dto.getReservationNum())
                .diagnosis(dto.getDiagnosis())
                .treatment(dto.getTreatment())
                .build();

        Long treatmentNum = treatmentDao.insert(treatment);

        // 처방전 리스트 반복 INSERT
        insertPrescriptions(treatmentNum, dto.getPrescriptions());

        // 예약상태 COMPLETED(3)로 UPDATE
        reservationDao.updateStatus(dto.getReservationNum(), ReservationStatus.COMPLETED.getCode()); // 3

        return treatmentNum;
    }

    // 진료 상세 조회 (화면용 DTO 조립)
    public RecordFormDto getRecordDetail(Long treatmentNum) {
        // 진료 정보 조회
        RecordFormDto record = treatmentDao.findById(treatmentNum);

        // 처방전 리스트 조회 및 DTO 변환
        List<Prescription> pList = prescriptionDao.findByTreatmentId(treatmentNum);
        List<PrescriptionDto> pDtoList = pList.stream()
                .map(p -> PrescriptionDto.builder()
                        .prescriptionNum(p.getPrescriptionNum())
                        .medicationName(p.getMedicationName())
                        .dosage(p.getDosage())
                        .durationDays(p.getDurationDays())
                        .build())
                .collect(Collectors.toList());

        record.setPrescriptions(pDtoList);
        return record;
    }

    // 진료 기록 수정 [트랜잭션]
    @Transactional
    public void updateRecord(RecordFormDto dto) {
        // 진료 테이블 UPDATE
        Treatment treatment = Treatment.builder()
                .treatmentNum(dto.getTreatmentNum())
                .diagnosis(dto.getDiagnosis())
                .treatment(dto.getTreatment())
                .build();
        treatmentDao.update(treatment);

        // 기존 처방전 삭제 (DELETE)
        prescriptionDao.deleteByTreatmentId(dto.getTreatmentNum());

        // 새 처방전 INSERT
        insertPrescriptions(dto.getTreatmentNum(), dto.getPrescriptions());
    }

    // (공통) 처방전 저장 로직
    private void insertPrescriptions(Long treatmentNum, List<PrescriptionDto> list) {
        if (list != null) {
            for (PrescriptionDto pDto : list) {
                // 빈 값(이름 없음)은 저장 안 함
                if (pDto.getMedicationName() == null || pDto.getMedicationName().trim().isEmpty()) {
                    continue;
                }
                Prescription p = Prescription.builder()
                        .treatmentNum(treatmentNum)
                        .medicationName(pDto.getMedicationName())
                        .dosage(pDto.getDosage())
                        .durationDays(pDto.getDurationDays())
                        .build();
                prescriptionDao.insert(p);
            }
        }
    }
}