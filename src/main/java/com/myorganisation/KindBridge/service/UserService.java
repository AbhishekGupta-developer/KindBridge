package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.dto.request.CompleteRegistrationRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;

public interface UserService {
    GenericResponseDto completeRegistration(String email, CompleteRegistrationRequestDto completeRegistrationRequestDto);
}
