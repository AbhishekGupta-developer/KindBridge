package com.myorganisation.CareEmoPilot.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

@Getter
@Setter
public class SigninRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
