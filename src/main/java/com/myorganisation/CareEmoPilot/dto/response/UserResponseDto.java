package com.myorganisation.CareEmoPilot.dto.response;

import com.myorganisation.CareEmoPilot.model.enums.Role;
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
    private Role role;
    private boolean anonymous;

}

