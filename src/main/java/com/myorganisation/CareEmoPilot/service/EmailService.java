package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.enums.OtpPurpose;

public interface EmailService {
    GenericResponseDto sendOtp(EmailRequestDto emailRequestDto, OtpPurpose otpPurpose);
    GenericResponseDto verifyOtp(EmailOtpVerificationRequestDto emailOtpVerificationRequestDto, OtpPurpose otpPurpose);
}
