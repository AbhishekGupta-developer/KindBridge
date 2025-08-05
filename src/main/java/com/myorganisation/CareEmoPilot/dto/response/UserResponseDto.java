package com.myorganisation.CareEmoPilot.dto.response;

import com.myorganisation.CareEmoPilot.enums.RoleType;
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

