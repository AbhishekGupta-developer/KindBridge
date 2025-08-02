package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.SigninRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SignupRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;

public interface AuthService {
    GenericResponseDto completeSignup(String authHeader, SignupRequestDto signupRequestDto);
    GenericResponseDto signin(SigninRequestDto signinRequestDto);
}
