package com.myorganisation.KindBridge.dto.request;

import com.myorganisation.KindBridge.enums.AreaType;
import com.myorganisation.KindBridge.enums.RoleType;
import com.myorganisation.KindBridge.enums.SupporterType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompleteRegistrationRequestDto {

    @NotNull(message = "Role is required")
    private RoleType role;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    /** Areas of expertise or interest */
    @NotEmpty(message = "At least one area must be selected")
    private List<@NotNull AreaType> areas;

    /** Only required when the role == SUPPORTER */
    private SupporterType supporterType;

    /** Only for supporters: whether to continue as anonymous */
    private Boolean anonymous = false;
}
