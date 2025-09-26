package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.KindBridge.dto.request.EmailRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.enums.OtpPurpose;

public interface EmailService {
    GenericResponseDto sendOtp(EmailRequestDto emailRequestDto, OtpPurpose otpPurpose);
    GenericResponseDto verifyOtp(EmailOtpVerificationRequestDto emailOtpVerificationRequestDto, OtpPurpose otpPurpose);
    GenericResponseDto sendSigninAlert(String email);
}
