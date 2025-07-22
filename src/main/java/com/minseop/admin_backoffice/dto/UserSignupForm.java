package com.minseop.admin_backoffice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupForm {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String username;

    @NotEmpty(message = "이름은 필수입니다.")
    private String fullName;

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수입니다.")
    private String password2;

    @NotEmpty(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
