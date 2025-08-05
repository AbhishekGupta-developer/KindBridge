package com.myorganisation.CareEmoPilot.controller;

import com.myorganisation.CareEmoPilot.dto.request.EmailAndPasswordRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SigninRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.enums.OtpPurpose;
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
    public ResponseEntity<GenericResponseDto> sendOtpToSignup(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        return new ResponseEntity<>(emailService.sendOtp(emailRequestDto, OtpPurpose.SIGNUP), HttpStatus.OK);
    }

    @PostMapping("/signup/email/verify-otp")
    public ResponseEntity<GenericResponseDto> verifyOtpToSignup(@Valid @RequestBody EmailOtpVerificationRequestDto emailOtpVerificationRequestDto) {
        return new ResponseEntity<>(emailService.verifyOtp(emailOtpVerificationRequestDto, OtpPurpose.SIGNUP), HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<GenericResponseDto> signup(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody EmailAndPasswordRequestDto emailAndPasswordRequestDto
    ) {
        return new ResponseEntity<>(authService.signup(authHeader, emailAndPasswordRequestDto), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<GenericResponseDto> signin(@Valid @RequestBody SigninRequestDto signinRequestDto) {
        return new ResponseEntity<>(authService.signin(signinRequestDto), HttpStatus.OK);
    }

    @PostMapping("/forgot-password/email/send-otp")
    public ResponseEntity<GenericResponseDto> sendOtpToResetPassword(@Valid @RequestBody EmailRequestDto emailRequestDto) {
        return new ResponseEntity<>(emailService.sendOtp(emailRequestDto, OtpPurpose.PASSWORD_RESET), HttpStatus.OK);
    }

    @PostMapping("/forgot-password/email/verify-otp")
    public ResponseEntity<GenericResponseDto> verifyOtpToResetPassword(@Valid @RequestBody EmailOtpVerificationRequestDto emailOtpVerificationRequestDto) {
        return new ResponseEntity<>(emailService.verifyOtp(emailOtpVerificationRequestDto, OtpPurpose.PASSWORD_RESET), HttpStatus.OK);
    }

    @PostMapping("/forgot-password/reset")
    public ResponseEntity<GenericResponseDto> resetPassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody EmailAndPasswordRequestDto emailAndPasswordRequestDto
    ) {
        return new ResponseEntity<>(authService.resetPassword(authHeader, emailAndPasswordRequestDto), HttpStatus.OK);
    }

}
