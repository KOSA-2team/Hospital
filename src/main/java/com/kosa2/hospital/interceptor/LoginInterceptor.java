package com.kosa2.hospital.interceptor;

import com.kosa2.hospital.enums.Grade;
import com.kosa2.hospital.model.MedicalStaff;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {

    public static final String LOGIN_STAFF_SESSION_KEY = "LOGIN_STAFF";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);

        // 1. 로그인 여부 확인
        if (session == null || session.getAttribute(LOGIN_STAFF_SESSION_KEY) == null) {
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }

        MedicalStaff staff = (MedicalStaff) session.getAttribute(LOGIN_STAFF_SESSION_KEY);

        // 2. 관리자 이상만 허용할 URL
        boolean adminOnly =
                requestURI.startsWith("/staff/delete") ||
                        requestURI.startsWith("/staff/authority") ||
                        requestURI.startsWith("/admin");

        if (adminOnly && staff.getPower() < Grade.ADMIN) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자만 접근 가능합니다.");
            return false;
        }

        return true;
    }
}