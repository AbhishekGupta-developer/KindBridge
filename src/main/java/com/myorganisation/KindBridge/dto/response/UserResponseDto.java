package com.myorganisation.KindBridge.dto.response;

import com.myorganisation.KindBridge.enums.UserRoleType;
import com.myorganisation.KindBridge.model.UserProfile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private UUID id;
    private String email;
    private boolean active;
    private UserRoleType role;
    private UUID profile;
    private UUID metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

