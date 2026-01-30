package com.myorganisation.KindBridge.dto.request;

import com.myorganisation.KindBridge.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UserProfileRequestDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Gender is required")
    private Gender gender;

    private LocalTime birthTime; // HH:mm format
    private String birthCity;
    private String birthCountry;
    private String email;
    private String phoneNumber;
}
