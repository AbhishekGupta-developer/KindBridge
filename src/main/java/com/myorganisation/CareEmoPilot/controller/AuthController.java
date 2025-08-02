package com.myorganisation.CareEmoPilot.controller;

import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SigninRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SignupRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.service.AuthService;
import com.myorganisation.CareEmoPilot.service.EmailService;
import com.myorganisation.CareEmoPilot.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup/email/send-otp")
    public ResponseEntity<GenericResponseDto> sendOtp(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        return new ResponseEntity<>(emailService.sendOtp(emailRequestDto), HttpStatus.OK);
    }

    @PostMapping("/signup/email/verify-otp")
    public ResponseEntity<GenericResponseDto> verifyOtp(@Valid @RequestBody EmailOtpVerificationRequestDto emailOtpVerificationRequestDto) {
        return new ResponseEntity<>(emailService.verifyOtp(emailOtpVerificationRequestDto), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericResponseDto> signup(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody SignupRequestDto signupRequestDto
    ) {
        return new ResponseEntity<>(authService.signup(authHeader, signupRequestDto), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<GenericResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return new ResponseEntity<>(authService.signin(signinRequestDto), HttpStatus.OK);
    }

}
