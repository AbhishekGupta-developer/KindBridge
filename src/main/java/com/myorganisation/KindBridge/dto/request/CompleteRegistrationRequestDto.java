package com.myorganisation.KindBridge.dto.request;

import com.myorganisation.KindBridge.enums.UserRoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteRegistrationRequestDto {

    @NotNull(message = "Role is required")
    private UserRoleType role;

    private UserProfileRequestDto userProfile;
}
