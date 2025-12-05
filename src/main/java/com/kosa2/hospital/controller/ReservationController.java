package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.service.MedicalStaffService; // ✨ 팀원이 만든 서비스 임포트
import com.kosa2.hospital.service.ReservationService;
import com.kosa2.hospital.model.MedicalStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MedicalStaffService medicalStaffService;

    // private final PatientService patientService;

    // 1. 예약 목록 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations/list";
    }

    // 2. 예약 등록 폼
    @GetMapping("/new")
    public String form(Model model) {
        List<Map<String, Object>> dummyPatients = new ArrayList<>();
        dummyPatients.add(Map.of("patientNum", 1L, "name", "임시환자_김철수"));
        dummyPatients.add(Map.of("patientNum", 2L, "name", "임시환자_이영희"));
        model.addAttribute("patients", dummyPatients);

        List<MedicalStaff> realDoctors = medicalStaffService.findAll();
        model.addAttribute("doctors", realDoctors);

        return "reservations/form";
    }

    // 3. 예약 등록 처리
    @PostMapping("/new")
    public String create(ReservationDto dto) {
        try {
            reservationService.createReservation(dto);
        } catch (IllegalStateException e) {
            return "redirect:/reservations/new?error=duplicate";
        }
        return "redirect:/reservations";
    }

    // 4. 예약 취소
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return "redirect:/reservations";
    }

    // 5. 예약 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ReservationDto dto = reservationService.getReservation(id);
        model.addAttribute("reservations", dto);
        return "reservations/detail";
    }
}