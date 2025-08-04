package com.minseop.admin_backoffice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";

        Throwable cause = exception.getCause();
        if (cause instanceof LockedException) {
            errorMessage = "계정이 정지되었습니다.";
        } else if (cause instanceof DisabledException) {
            errorMessage = "계정이 비활성화되어 있습니다.";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";
        }

        // 실패 메시지를 세션에 저장
        request.getSession().setAttribute("loginErrorMessage", errorMessage);

        // 로그인 페이지로 리다이렉트
        response.sendRedirect("/login?error");
    }

}
