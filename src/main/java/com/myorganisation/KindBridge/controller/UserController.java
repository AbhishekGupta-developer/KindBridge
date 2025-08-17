package com.myorganisation.KindBridge.controller;

import com.myorganisation.KindBridge.dto.request.CompleteRegistrationRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/complete-registration")
    public ResponseEntity<GenericResponseDto> completeRegistration(
            @AuthenticationPrincipal(expression = "username") String email,
            @Valid @RequestBody CompleteRegistrationRequestDto completeRegistrationRequestDto
    ) {
        System.out.println("Email inside controller: " + email);
        return new ResponseEntity<>(userService.completeRegistration(email, completeRegistrationRequestDto), HttpStatus.OK);
    }

}
