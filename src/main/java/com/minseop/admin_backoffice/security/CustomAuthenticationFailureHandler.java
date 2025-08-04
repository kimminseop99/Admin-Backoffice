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
                                        AuthenticationException exception) throws IOException {

        String errorMessage = "아이디 또는 비밀번호가 잘못되었습니다.";

        String message = exception.getMessage();
        if (message != null && message.startsWith("BANNED:")) {
            String userId = message.substring("BANNED:".length());
            request.getSession().setAttribute("bannedUserId", userId);

            // 로그로 확인
            System.out.println(">> banned 사용자 로그인 시도: " + userId);

            // 리다이렉트
            response.sendRedirect("/banned-info");
            return;
        }

        request.getSession().setAttribute("loginErrorMessage", errorMessage);
        response.sendRedirect("/user/login?error");
    }



}
