package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.dto.request.CompleteRegistrationRequestDto;
import com.myorganisation.KindBridge.dto.request.UserProfileRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.dto.response.UserResponseDto;
import com.myorganisation.KindBridge.enums.UserProfileType;
import com.myorganisation.KindBridge.enums.UserRoleType;
import com.myorganisation.KindBridge.exception.UserNotFoundException;
import com.myorganisation.KindBridge.model.User;
import com.myorganisation.KindBridge.model.UserProfile;
import com.myorganisation.KindBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public GenericResponseDto completeRegistration(String email, CompleteRegistrationRequestDto completeRegistrationRequestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User email: " + email + " doesn't exist"));

        // If the role already set, optionally you can prevent re-completion
        if(user.getRole() != null) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Registration already completed")
                    .details(null)
                    .build();
        }

        // Validate role-specific requirements
        if(completeRegistrationRequestDto.getRole() == UserRoleType.SEEKER) {
            user.setRole(UserRoleType.SEEKER);
            // future scopes
        } else if(completeRegistrationRequestDto.getRole() == UserRoleType.PROVIDER) {
            user.setRole(UserRoleType.PROVIDER);
            // future scopes
        } else {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid role")
                    .details(null)
                    .build();
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setType(UserProfileType.INDEPENDENT);

        user.setProfile(userProfile);

        mapUserProfileRequestDtoToUserProfile(completeRegistrationRequestDto.getUserProfile(), user.getProfile());

        user.getMetaData().setRegistrationCompleted(true);
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("Registration complete")
                .details(mapUserToUserResponseDto(user))
                .build();
    }

    // Helper methods

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Helper method to map User to UserResponseDto
    private UserResponseDto mapUserToUserResponseDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setActive(user.isActive());
        userResponseDto.setRole(user.getRole());
        userResponseDto.setProfile(user.getProfile().getId());
        userResponseDto.setMetadata(user.getMetaData().getId());
        userResponseDto.setCreatedAt(user.getCreatedAt());
        userResponseDto.setUpdatedAt(user.getUpdatedAt());

        return userResponseDto;
    }

    private void mapUserProfileRequestDtoToUserProfile(UserProfileRequestDto userProfileRequestDto, UserProfile userProfile) {
        userProfile.setFirstName(userProfileRequestDto.getFirstName());
        userProfile.setMiddleName(userProfileRequestDto.getMiddleName());
        userProfile.setLastName(userProfileRequestDto.getLastName());
        userProfile.setDateOfBirth(userProfileRequestDto.getDateOfBirth());
        userProfile.setGender(userProfileRequestDto.getGender());
        userProfile.setBirthTime(userProfileRequestDto.getBirthTime());
        userProfile.setBirthCity(userProfileRequestDto.getBirthCity());
        userProfile.setBirthCountry(userProfileRequestDto.getBirthCountry());
        userProfile.setEmail(userProfileRequestDto.getEmail());
        userProfile.setPhoneNumber(userProfileRequestDto.getPhoneNumber());
    }
}
