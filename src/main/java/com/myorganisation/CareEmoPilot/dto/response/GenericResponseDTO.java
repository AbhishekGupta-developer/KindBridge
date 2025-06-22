package com.myorganisation.CareEmoPilot.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponseDTO {
    private boolean success;
    private String message;
}
