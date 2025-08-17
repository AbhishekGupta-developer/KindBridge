package com.myorganisation.KindBridge.dto.response;

import com.myorganisation.KindBridge.enums.RoleType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private RoleType roleType;
    private boolean anonymous;

}

