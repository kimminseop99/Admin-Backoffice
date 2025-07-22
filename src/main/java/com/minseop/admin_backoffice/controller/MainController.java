package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Admin Backoffice - Dashboard");

        long totalUsers = userService.getTotalUserCount();
        long newUsersLast7Days = userService.getNewUsersCountLast7Days();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("newUsersLast7Days", newUsersLast7Days);

        // 대시보드 fragment 경로
        model.addAttribute("dashboard", "admin/dashboard :: dashboard");

        return "layout/main";
    }
}
