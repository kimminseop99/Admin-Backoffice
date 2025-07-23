package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserRole;
import com.minseop.admin_backoffice.domain.UserStatus;
import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;


    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search,
                            Pageable pageable,
                            Model model) {
        Page<UserEntity> users = userService.findAllUsers(search, pageable);
        model.addAttribute("users", users);
        model.addAttribute("search", search);
        return "admin/user_list";
    }

    @GetMapping("/{id}")
    public String viewUserDetail(@PathVariable("id") Long id, Model model) {
        UserEntity user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        model.addAttribute("statuses", UserStatus.values());
        model.addAttribute("roles", UserRole.values());
        return "admin/user_detail";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("status") UserStatus status,
                             @RequestParam("role") UserRole role) {
        UserEntity user = userService.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(status);
        user.setRole(role);
        userService.saveUser(user);
        return "redirect:/admin/user/" + id + "?updated=true";
    }
}

