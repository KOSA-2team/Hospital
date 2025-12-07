package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.RecordFormDto;
import com.kosa2.hospital.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 처방 기록
@Controller
@RequestMapping("/records")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    // 진료 차트 작성 폼
    // URL: /records/new?reservationNum=10
    // 예약 상태 CONFIRMED인 경우만 진입 (Service or Interceptor 체크 권장)
    @GetMapping("/new")
    public String recordForm(@RequestParam Long reservationNum, Model model) {
        // 예약 정보와 환자 정보를 조회하여 화면에 전달 (상단 카드용)
        model.addAttribute("reservation", recordService.getReservationInfo(reservationNum));

        // 빈 DTO 생성해서 전달
        RecordFormDto formDto = new RecordFormDto();
        formDto.setReservationNum(reservationNum);
        model.addAttribute("recordFormDto", formDto);

        return "records/form";
    }

    // 진료 기록 저장
    // [트랜잭션] 진료기록 INSERT -> 처방전 리스트 반복 INSERT -> 예약상태 COMPLETED로 UPDATE
    @PostMapping("/new")
    public String createRecord(@ModelAttribute RecordFormDto form) {
        Long treatmentNum = recordService.saveRecord(form);
        return "redirect:/records/" + treatmentNum; // 저장 후 상세 페이지로 이동
    }

    // 진료 상세 조회
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // 서비스 호출: 진료 정보 + 처방전 리스트를 합쳐서 가져옴
        RecordFormDto record = recordService.getRecordDetail(id);
        model.addAttribute("record", record);
        return "records/detail";
    }

    // 진료 기록 수정 폼
    // URL: /records/{id}/edit
    // 오진입 수정을 위해 필요. 기존 처방 내역 불러오기.
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        RecordFormDto record = recordService.getRecordDetail(id);
        model.addAttribute("recordFormDto", record);
        return "records/edit";
    }

    // 진료 수정 처리
    // URL: /records/{id}/edit
    // [트랜잭션] 기존 처방전 삭제(DELETE) 후 새 처방전 INSERT 방식 추천
    @PostMapping("/{id}/edit")
    public String updateRecord(@PathVariable Long id, @ModelAttribute RecordFormDto form) {
        // PK 안전하게 세팅
        form.setTreatmentNum(id);
        recordService.updateRecord(form);
        return "redirect:/records/" + id;
    }
}