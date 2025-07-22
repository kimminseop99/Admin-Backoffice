package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.minseop.admin_backoffice.service.UserService;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public String userList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        Page<UserEntity> userPage = userService.findAllUsers(keyword, pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("keyword", keyword);
        return "user/user-list";
    }
}
