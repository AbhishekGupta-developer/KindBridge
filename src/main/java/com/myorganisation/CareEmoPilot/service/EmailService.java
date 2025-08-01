package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;

public interface EmailService {
    GenericResponseDto sendOtp(EmailRequestDto emailRequestDto);
    GenericResponseDto verifyOtp(EmailOtpVerificationRequestDto emailOtpVerificationRequestDto);
}
