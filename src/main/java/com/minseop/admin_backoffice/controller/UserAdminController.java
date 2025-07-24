package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserRole;
import com.minseop.admin_backoffice.domain.UserStatus;
import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;


    @GetMapping
    public String listUsers(@RequestParam(value = "search", required = false) String search,
                            Pageable pageable,
                            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<UserRole> excludedRoles = new ArrayList<>();

        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))) {
            excludedRoles.add(UserRole.SUPER_ADMIN);
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            excludedRoles.add(UserRole.SUPER_ADMIN);
            excludedRoles.add(UserRole.ADMIN);
        }


        Page<UserEntity> users = userService.findUsersExcludingRoles(search, pageable, excludedRoles);
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

        UserEntity targetUser = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 현재 로그인한 관리자 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity currentAdmin = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // 핵심 로직 호출: 상태/권한 변경 + 로그 기록
        userService.updateUserStatusOrRole(targetUser, currentAdmin, status, role);

        return "redirect:/admin/user/" + id + "?updated=true";
    }

}

