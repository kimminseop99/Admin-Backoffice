package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.domain.UserRole;
import com.minseop.admin_backoffice.domain.UserStatus;
import com.minseop.admin_backoffice.dto.UserSignupForm;
import com.minseop.admin_backoffice.dto.UserUpdateForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.minseop.admin_backoffice.service.UserService;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signupForm(UserSignupForm form) {
        return "user/signupForm";
    }

    @PostMapping("/signup")
    public String processSignup(@Valid UserSignupForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "user/signupForm";
        }

        if (!form.getPassword1().equals(form.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "user/signupForm";
        }

        if (userService.findByUsername(form.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "duplicate", "이미 사용 중인 아이디입니다.");
            return "user/signupForm";
        }

        if (userService.findByEmail(form.getEmail()).isPresent()) {
            bindingResult.rejectValue("email", "duplicate", "이미 사용 중인 이메일입니다.");
            return "user/signupForm";
        }


        UserEntity user = UserEntity.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPassword1()))
                .email(form.getEmail())
                .fullName(form.getFullName())
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();

        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {

        Object errorMessage = request.getSession().getAttribute("loginErrorMessage");
        if (errorMessage != null) {
            model.addAttribute("loginErrorMessage", errorMessage);
            request.getSession().removeAttribute("loginErrorMessage");
        }
        return "user/login";
    }

    @GetMapping("/update")
    public String updateForm(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
                             Model model) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        UserUpdateForm form = new UserUpdateForm();
        form.setFullName(user.getFullName());
        model.addAttribute("userUpdateForm", form);
        return "user/updateForm";
    }

    @PostMapping("/update")
    public String processUpdate(@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
                                @Valid UserUpdateForm form, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "user/updateForm";
        }

        if (!form.getPassword1().equals(form.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "user/updateForm";
        }

        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(form.getPassword1()));
        user.setFullName(form.getFullName());

        userService.saveUser(user);

        model.addAttribute("message", "정보가 성공적으로 변경되었습니다.");
        return "user/updateForm";
    }
}
