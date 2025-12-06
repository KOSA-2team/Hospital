package com.kosa2.hospital.controller;

import com.kosa2.hospital.dto.LoginDto;
import com.kosa2.hospital.model.MedicalStaff;
import com.kosa2.hospital.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("loginDto", new LoginDto());
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String medicalId,
            @RequestParam String pwd,
            HttpServletRequest request) {

        MedicalStaff loginDoctor = loginService.login(medicalId, pwd);
        if (loginDoctor == null) {
            // 로그인 실패 처리 (메시지 전달 등)
            return "redirect:/login?error=1";
        }

        // 로그인 성공 → 세션 생성
        HttpSession session = request.getSession(); // 없으면 생성
        session.setAttribute("loginDoctor", loginDoctor);

        // 로그인 후 메인 페이지로 이동
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 있으면 가져오고 없으면 null
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}
