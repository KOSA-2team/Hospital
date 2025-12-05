package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.MedicalStaffDto;
import com.kosa2.hospital.enums.Grade;
import com.kosa2.hospital.model.MedicalStaff;
import com.kosa2.hospital.service.MedicalStaffService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kosa2.hospital.interceptor.LoginInterceptor.LOGIN_STAFF_SESSION_KEY;


@Controller
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class MedicalStaffController {

    private final MedicalStaffService medicalStaffService;

    // 회원가입 폼
    @GetMapping("/new")
    public String Form(Model model) {
        model.addAttribute("MedicalStaffDto", new MedicalStaffDto());
        return "doctors/form";
    }

    // 회원가입 처리
    @PostMapping("/new")
    public String join(@ModelAttribute("MedicalStaffDto") MedicalStaffDto dto) {
        medicalStaffService.join(dto);
        return "redirect:/login/loginForm";
    }

    // 목록
    @GetMapping
    public String list(Model model) {
        List<MedicalStaff> staffList = medicalStaffService.findAll();
        model.addAttribute("staffList", staffList);
        return "doctors/list";
    }

    // 수정 폼
    @GetMapping("/{medicalNum}/edit")
    public String editForm(@PathVariable Long medicalNum, Model model) {
        MedicalStaff staff = medicalStaffService.findById(medicalNum);

        MedicalStaffDto dto = new MedicalStaffDto();
        dto.setMedicalNum(staff.getMedicalNum());
        dto.setMedicalId(staff.getMedicalId());
        dto.setMName(staff.getMname());
        dto.setSpecialty(staff.getSpecialty());
        dto.setMPhone(staff.getMphone());
        dto.setEmail(staff.getEmail());

        model.addAttribute("staffDto", dto);
        return "doctors/edit";
    }

    // 수정 처리 (프로필 + 비밀번호 변경)
    @PostMapping("/edit")
    public String edit(@ModelAttribute("staffDto") MedicalStaffDto dto) {
        medicalStaffService.updateProfile(dto);
        return "redirect:/doctors?edit?edited";
    }

    // 권한 변경 폼 (관리자만, 인터셉터에서 막힘)
    @GetMapping("/{medicalNum}/authority")
    public String authorityForm(@PathVariable Long medicalNum, Model model) {
        MedicalStaff staff = medicalStaffService.findById(medicalNum);

        MedicalStaffDto dto = new MedicalStaffDto();
        dto.setMedicalNum(staff.getMedicalNum());
        dto.setMedicalId(staff.getMedicalId());
        dto.setMName(staff.getMname());
        dto.setPower(staff.getPower());

        model.addAttribute("staffDto", dto);
        model.addAttribute("staff", staff);
        return "doctors/authority";
    }

    // 권한 변경 처리
    @PostMapping("/authority")
    public String authority(@ModelAttribute("staffDto") MedicalStaffDto dto) {
        medicalStaffService.updatePower(dto);
        return "redirect:/doctors";
    }

    // 삭제 (관리자만, 인터셉터 + 컨트롤러 이중 방어)
    @PostMapping("/{medicalNum}/delete")
    public String delete(@PathVariable Long medicalNum, HttpSession session) {

        // 1. 세션에서 로그인 객체를 가져와 형변환
        MedicalStaff loginDoctors = (MedicalStaff) session.getAttribute(LOGIN_STAFF_SESSION_KEY);

        // 2. 🚨 수정된 방어 로직:
        //    a) 로그인 객체가 Null이거나 (세션 만료 등)
        //    b) 로그인 객체의 권한이 SYS 등급이 아닌 경우 (== Grade.SYS로 수정 필요)

        // 만약 SYS 권한만 삭제를 허용한다면 (현재 코드의 의도로 추정)
        if (loginDoctors == null || loginDoctors.getPower() != Grade.SYS) {
            // 권한이 없으므로 접근 금지 에러 페이지로 리다이렉트
            return "redirect:/doctors?error=forbidden";
        }

        // 3. 권한 검사 통과 시 삭제 로직 실행
        medicalStaffService.delete(medicalNum);

        return "redirect:/doctors";
    }
}


