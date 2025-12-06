package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.PatientDto;
import com.kosa2.hospital.dto.ReservationDto;
import com.kosa2.hospital.model.MedicalStaff;
import com.kosa2.hospital.service.MedicalStaffService;
import com.kosa2.hospital.service.PatientService;
import com.kosa2.hospital.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final MedicalStaffService medicalStaffService;
    private final PatientService patientService;

    // 1. 예약 목록 조회
    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "reservations/list";
    }

    // 2. 예약 등록 폼
    @GetMapping("/new")
    public String form(Model model) {
        List<PatientDto> realPatients = patientService.getPatientList(null);
        model.addAttribute("patients", realPatients);

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