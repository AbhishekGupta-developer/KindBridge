package com.myorganisation.KindBridge.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailOtpVerificationRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;

}
