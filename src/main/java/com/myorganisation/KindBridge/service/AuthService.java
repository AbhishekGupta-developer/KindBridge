package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.dto.request.EmailAndPasswordRequestDto;
import com.myorganisation.KindBridge.dto.request.SigninRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;

public interface AuthService {
    GenericResponseDto signup(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto);
    GenericResponseDto signin(SigninRequestDto signinRequestDto);
    GenericResponseDto resetPassword(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto);
}
