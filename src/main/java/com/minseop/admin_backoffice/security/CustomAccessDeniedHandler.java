package com.minseop.admin_backoffice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 403 상태 유지
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // alert 창 띄우도록 간단히 자바스크립트 전달 (Thymeleaf 페이지가 아닌 경우)
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('접근 권한이 없습니다.'); history.back();</script>");
        out.flush();
    }
}
