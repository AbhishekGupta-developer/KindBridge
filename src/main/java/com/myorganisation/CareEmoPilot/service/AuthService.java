package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.EmailAndPasswordRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SigninRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;

public interface AuthService {
    GenericResponseDto signup(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto);
    GenericResponseDto signin(SigninRequestDto signinRequestDto);
    GenericResponseDto resetPassword(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto);
}
