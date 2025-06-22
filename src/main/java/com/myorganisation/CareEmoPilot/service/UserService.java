package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.UserRequestDTO;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDTO;
import com.myorganisation.CareEmoPilot.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO registerUser(UserRequestDTO requestDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);
    GenericResponseDTO removeUser(Long id);
}
