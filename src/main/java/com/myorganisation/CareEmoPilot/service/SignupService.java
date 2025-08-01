package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.SignupRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;

public interface SignupService {
    GenericResponseDto completeSignup(String authHeader, SignupRequestDto signupRequestDto);
}
