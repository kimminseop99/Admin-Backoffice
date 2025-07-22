package com.minseop.admin_backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping({"/", "/admin", "/admin/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Admin Backoffice - Dashboard");
        // layouts/main.html의 content 영역에 삽입될 fragment 경로
        model.addAttribute("content", "admin/dashboard :: content");
        return "layout/main";
    }
}
