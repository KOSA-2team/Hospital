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
@RequestMapping("/staff")
@RequiredArgsConstructor
public class MedicalStaffController {

    private final MedicalStaffService medicalStaffService;

    // 회원가입 폼
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("MedicalStaffDto", new MedicalStaffDto());
        return "login/joinForm";
    }

    // 회원가입 처리
    @PostMapping("/join")
    public String join(@ModelAttribute("MedicalStaffDto") MedicalStaffDto dto) {
        medicalStaffService.join(dto);
        return "redirect:/login/loginForm";
    }

    // 목록
    @GetMapping("/list")
    public String list(Model model) {
        List<MedicalStaff> staffList = medicalStaffService.findAll();
        model.addAttribute("staffList", staffList);
        return "staff/listForm";
    }

    // 수정 폼
    @GetMapping("/edit/{medicalNum}")
    public String editForm(@PathVariable Long medicalNum, Model model) {
        MedicalStaff staff = medicalStaffService.findById(medicalNum);

        MedicalStaffDto dto = new MedicalStaffDto();
        dto.setMedicalNum(staff.getMedicalNum());
        dto.setMedicalId(staff.getMedicalId());
        dto.setMName(staff.getMname());
        dto.setSpecialty(staff.getSpecialty());
        dto.setMPhone(staff.getMphone());
        dto.setEmail(staff.getEmail());
        // pwd, power 는 화면에서 필요할 때만 입력

        model.addAttribute("staffDto", dto);
        return "staff/editForm";
    }

    // 수정 처리 (프로필 + 비밀번호 옵션 변경)
    @PostMapping("/edit")
    public String edit(@ModelAttribute("staffDto") MedicalStaffDto dto) {
        medicalStaffService.updateProfile(dto);
        return "redirect:/staff/list";
    }

    // 권한 변경 폼 (관리자만, 인터셉터에서 막힘)
    @GetMapping("/authority/{medicalNum}")
    public String authorityForm(@PathVariable Long medicalNum, Model model) {
        MedicalStaff staff = medicalStaffService.findById(medicalNum);

        MedicalStaffDto dto = new MedicalStaffDto();
        dto.setMedicalNum(staff.getMedicalNum());
        dto.setMedicalId(staff.getMedicalId());
        dto.setMName(staff.getMname());
        dto.setPower(staff.getPower());

        model.addAttribute("staffDto", dto);
        model.addAttribute("staff", staff);
        return "staff/authorityForm";
    }

    // 권한 변경 처리
    @PostMapping("/authority")
    public String authority(@ModelAttribute("staffDto") MedicalStaffDto dto) {
        medicalStaffService.updatePower(dto);
        return "redirect:/staff/list";
    }

    // 삭제 (관리자만, 인터셉터 + 컨트롤러 이중 방어)
    @PostMapping("/delete/{medicalNum}")
    public String delete(@PathVariable Long medicalNum, HttpSession session) {
        MedicalStaff login = (MedicalStaff) session.getAttribute(LOGIN_STAFF_SESSION_KEY);
        if (login == null || login.getPower() <= Grade.ADMIN) {
            return "redirect:/staff/list?error=forbidden";
        }
        medicalStaffService.delete(medicalNum);
        return "redirect:/staff/list";
    }
}


