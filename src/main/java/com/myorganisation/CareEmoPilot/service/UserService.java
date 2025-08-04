package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.CompleteRegistrationRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;

public interface UserService {
    GenericResponseDto completeRegistration(String email, CompleteRegistrationRequestDto completeRegistrationRequestDto);
}
