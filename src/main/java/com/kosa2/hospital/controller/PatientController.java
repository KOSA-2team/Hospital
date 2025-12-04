package com.kosa2.hospital.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kosa2.hospital.dto.PatientDto;
import com.kosa2.hospital.service.PatientService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;
    
    // 환자 목록 페이지, 이름 or 전화번호로 검색 가능
    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("list", patientService.getPatientList(keyword));
        model.addAttribute("keyword", keyword);
        return "patients/list";
    }

    // 환자 상세 페이지
    @GetMapping("/{id}")
    public String details(@PathVariable int id, Model model) {
        model.addAttribute("patient", patientService.getPatientDetail(id));
        return "patients/detail";
    }

    // 환자 등록 페이지
    @GetMapping("/new")
    public String newForm() {
        return "patients/insert-form";
    }

    // 환자 등록 처리
    @PostMapping("/new")
    public String insert(PatientDto dto) {
        patientService.registerPatient(dto);
        return "redirect:/patients/";
    }
    
    // 환자 수정 페이지
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("patient", patientService.getPatientDetail(id));
        return "patients/edit-form";
    }

    // 환자 수정 처리
    @PostMapping("/{id}/edit")
    public String update(@PathVariable int id, PatientDto dto) {
        dto.setPatient_num(id);
        patientService.updatePatient(dto);
        return "redirect:/patients/{id}";
    }
    
    // 환자 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        try {
            patientService.deletePatient(id);
            return "redirect:/patients/";
        } catch (IllegalStateException e) {
            // 예약이 존재해 삭제 불가
            return "redirect:/patients/?error=hasReservations";
        }
    }
}
