package com.kosa2.hospital.controller;

import com.kosa2.hospital.dao.TreatmentDao;
import com.kosa2.hospital.dto.PatientDto;
import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.dto.TreatmentHistoryDto;
import com.kosa2.hospital.service.PatientService;
import com.kosa2.hospital.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final ReservationService reservationService; // ✨ 위로 올렸습니다.
    private final TreatmentDao treatmentDao;

    // 목록
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("list", patientService.getPatientList(keyword));
        model.addAttribute("keyword", keyword);
        return "patients/list";
    }

    // 상세
    @GetMapping("/{id}")
    public String details(@PathVariable int id, Model model) {
        // 환자 정보
        model.addAttribute("patient", patientService.getPatientDetail(id));

        // 예약 내역 가져오기 (int -> Long 형변환)
        List<ReservationDto> reservations = reservationService.getReservationsByPatient((long) id);
        model.addAttribute("reservations", reservations);

        // [추가] 과거 진료 기록 (History) 가져오기
        List<TreatmentHistoryDto> historyList = treatmentDao.findHistoryByPatientNum((long) id);
        model.addAttribute("historyList", historyList);

        return "patients/detail";
    }

    // 등록 폼
    @GetMapping("/new")
    public String newForm() {
        return "patients/insert-form";
    }

    // 등록 처리
    @PostMapping("/new")
    public String insert(PatientDto dto) {
        patientService.registerPatient(dto);
        return "redirect:/patients";
    }

    // 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("patient", patientService.getPatientDetail(id));
        return "patients/edit-form";
    }

    // 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, PatientDto dto) {
        dto.setPatient_num(id);
        patientService.updatePatient(dto);
        return "redirect:/patients/" + id;
    }

    // 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        patientService.deletePatient(id);
        return "redirect:/patients";
    }

}