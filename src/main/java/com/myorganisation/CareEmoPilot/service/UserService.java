package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.UserRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto registerUser(UserRequestDto requestDTO);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserById(Long id);
    UserResponseDto updateUser(Long id, UserRequestDto requestDTO);
    GenericResponseDto removeUser(Long id);
}
