package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.dto.request.UserRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.dto.response.UserResponseDto;
import com.myorganisation.CareEmoPilot.model.User;
import com.myorganisation.CareEmoPilot.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponseDto registerUser(UserRequestDto requestDTO) {
        User user = User.builder()
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .password(requestDTO.getPassword()) // Note: encrypt later when security is added
                .role(requestDTO.getRole())
                .anonymous(requestDTO.isAnonymous())
                .active(true)
                .build();

        User saved = userRepository.save(user);
        return mapToDTO(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .filter(User::isActive) // Only return active users
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDTO(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setFirstName(requestDTO.getFirstName());
        user.setLastName(requestDTO.getLastName());
        user.setUsername(requestDTO.getUsername());
        user.setEmail(requestDTO.getEmail());
        user.setPhone(requestDTO.getPhone());
        user.setPassword(requestDTO.getPassword());
        user.setRole(requestDTO.getRole());
        user.setAnonymous(requestDTO.isAnonymous());

        User updated = userRepository.save(user);
        return mapToDTO(updated);
    }

    @Override
    public GenericResponseDto removeUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isEmpty() || !userOptional.get().isActive()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("User not found with id: " + id)
                    .build();
        }

        User user = userOptional.get();
        user.setActive(false); // ✅ Soft delete
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("User removed successfully.")
                .build();

    }

    // Helper method to convert User entity to UserResponseDTO
    private UserResponseDto mapToDTO(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .anonymous(user.isAnonymous())
                .build();
    }

}
