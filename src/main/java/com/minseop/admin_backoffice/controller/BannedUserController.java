package com.minseop.admin_backoffice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BannedUserController {

    @GetMapping("/banned-info")
    public String bannedInfo(HttpServletRequest request, Model model) {
        String bannedUserId = (String) request.getSession().getAttribute("bannedUserId");
        model.addAttribute("bannedUserId", bannedUserId);
        model.addAttribute("adminEmail", "admin@gmail.com");
        return "user/banned-info";
    }
}
