package com.minseop.admin_backoffice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateForm {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String password2;

    @NotBlank(message = "이름을 입력해주세요.")
    private String fullName;
}
