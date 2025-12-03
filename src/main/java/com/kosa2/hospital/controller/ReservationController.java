package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/reservations") // ✨ 기본 URL 변경됨
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // private final PatientService patientService;
    // private final MedicalService medicalService;

    // 1. 예약 목록 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations/list";
    }

    // 2. 예약 등록 폼
    @GetMapping("/new")
    public String form(Model model) {
        // '가짜 데이터' (드롭다운 테스트용)
        List<Map<String, Object>> dummyPatients = new ArrayList<>();
        dummyPatients.add(Map.of("patientNum", 1L, "name", "임시환자_김철수"));
        dummyPatients.add(Map.of("patientNum", 2L, "name", "임시환자_이영희"));

        List<Map<String, Object>> dummyDoctors = new ArrayList<>();
        dummyDoctors.add(Map.of("medicalNum", 1L, "mname", "임시의사_허준"));
        dummyDoctors.add(Map.of("medicalNum", 2L, "mname", "임시의사_장금이"));

        model.addAttribute("patients", dummyPatients);
        model.addAttribute("doctors", dummyDoctors);

        // model.addAttribute("patients", patientService.getAllPatients());
        // model.addAttribute("doctors", medicalService.getAllDoctors());

        return "reservations/form"; // ✨ 뷰 폴더 위치 변경
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
}