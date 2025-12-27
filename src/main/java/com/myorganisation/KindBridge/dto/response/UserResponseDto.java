package com.myorganisation.KindBridge.dto.response;

import com.myorganisation.KindBridge.enums.AreaType;
import com.myorganisation.KindBridge.enums.RoleType;
import com.myorganisation.KindBridge.enums.SupporterType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String email;
    private boolean isEmailVerified;
    private boolean active;
    private RoleType role;
    private SupporterType supporterType;
    private List<AreaType> areas;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean anonymous;
    private boolean isRegistrationCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

