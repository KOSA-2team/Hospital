package com.kosa2.hospital.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginDoctor") == null) {
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 컨트롤러 진입 막기
        }

        // 로그인 되어 있으면 컨트롤러로 진행
        return true;
    }
}
