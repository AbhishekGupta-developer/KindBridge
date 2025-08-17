package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.dto.request.CompleteRegistrationRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.enums.RoleType;
import com.myorganisation.KindBridge.model.User;
import com.myorganisation.KindBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public GenericResponseDto completeRegistration(String email, CompleteRegistrationRequestDto completeRegistrationRequestDto) {
        System.out.println("Email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // If the role already set, optionally you can prevent re-completion
        if(user.getRole() != null) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Registration already completed")
                    .data(null)
                    .build();
        }

        // Validate role-specific requirements
        if(completeRegistrationRequestDto.getRole() == RoleType.SEEKER) {
            user.setRole(RoleType.SEEKER);

            // Seeker should not provide supporterType; ignore if present
            user.setSupporterType(null);
            user.setAnonymous(false); // seekers are not anonymous per flow
        } else if(completeRegistrationRequestDto.getRole() == RoleType.SUPPORTER) {
            user.setRole(RoleType.SUPPORTER);

            // supporterType must be present
            if (completeRegistrationRequestDto.getSupporterType() == null) {
                return GenericResponseDto.builder()
                        .success(false)
                        .message("Supporter type is required for supporters")
                        .data(null)
                        .build();
            }

            user.setSupporterType(completeRegistrationRequestDto.getSupporterType());
            user.setAnonymous(Boolean.TRUE.equals(completeRegistrationRequestDto.getAnonymous()));
        } else {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid role")
                    .data(null)
                    .build();
        }

        // Common required fields
        if(isBlank(completeRegistrationRequestDto.getFirstName()) || isBlank(completeRegistrationRequestDto.getLastName())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("First name and last name are required")
                    .data(null)
                    .build();
        }

        user.setFirstName(completeRegistrationRequestDto.getFirstName());
        user.setLastName(completeRegistrationRequestDto.getLastName());

        if(completeRegistrationRequestDto.getAreas() == null || completeRegistrationRequestDto.getAreas().isEmpty()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("At least one area must be selected")
                    .data(null)
                    .build();
        }

        user.setAreas(completeRegistrationRequestDto.getAreas());

        user.setRegistrationCompleted(true);
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("Registration complete")
                .data(null)
                .build();
    }

    // Helper methods

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

}
