package com.minseop.admin_backoffice.config;

import com.minseop.admin_backoffice.security.CustomAccessDeniedHandler;
import com.minseop.admin_backoffice.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .userDetailsService(customUserDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/login", "/user/signup", "/h2-console/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/audit/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .anyRequest().authenticated()
                ).csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher("/h2-console/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))
                .formLogin((formLogin) -> formLogin
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/user/login?error=true"))
                .logout((logout) -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
                        .logoutSuccessUrl("/user/login")
                        .invalidateHttpSession(true))
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(customAccessDeniedHandler())
                )
        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}

