package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.constants.UserConstants;
import com.myorganisation.KindBridge.dto.request.EmailAndPasswordRequestDto;
import com.myorganisation.KindBridge.dto.request.SigninRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.model.User;
import com.myorganisation.KindBridge.repository.UserRepository;
import com.myorganisation.KindBridge.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public GenericResponseDto signup(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return GenericResponseDto.builder()
                            .success(false)
                            .message("Missing or malformed token")
                            .details(null)
                            .build();
        }

        String signupToken = authHeader.substring(7);

        if(signupToken == null || !jwtUtil.isValidSignupToken(signupToken)) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid or expired signup token")
                    .details(null)
                    .build();
        }

        String emailFromToken = jwtUtil.extractEmail(signupToken);
        if(!emailFromToken.equals(emailAndPasswordRequestDto.getEmail())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email mismatch")
                    .details(null)
                    .build();
        }

        User user = userRepository.findByEmail(emailAndPasswordRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.isEmailVerified()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email not verified")
                    .details(null)
                    .build();
        }

        if(user.getPassword() != null && !UserConstants.PASSWORD_NOT_SET.equals(user.getPassword())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Password already set")
                    .details(null)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(emailAndPasswordRequestDto.getPassword()));
        user.setActive(true);
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("Signup complete")
                .details(null)
                .build();
    }

    @Override
    public GenericResponseDto signin(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail())
                .orElse(null);

        if(user == null) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .details(null)
                    .build();
        }

        if(!user.isActive()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Account not active")
                    .details(null)
                    .build();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequestDto.getEmail(), signinRequestDto.getPassword())
            );
        } catch(AuthenticationException e) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid credentials")
                    .details(null)
                    .build();
        }

        if(!user.isEmailVerified()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email not verified")
                    .details(null)
                    .build();
        }

        String authToken = jwtUtil.generateAuthToken(user.getEmail());

        return GenericResponseDto.builder()
                .success(true)
                .message("Signin successful")
                .details(Map.of("authToken", authToken))
                .build();
    }

    @Override
    public GenericResponseDto resetPassword(String authHeader, EmailAndPasswordRequestDto emailAndPasswordRequestDto) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Missing or malformed token")
                    .details(null)
                    .build();
        }

        String passwordResetToken = authHeader.substring(7);

        if(passwordResetToken == null || !jwtUtil.isValidPasswordResetToken(passwordResetToken)) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid or expired signup token")
                    .details(null)
                    .build();
        }

        String emailFromToken = jwtUtil.extractEmail(passwordResetToken);
        if(!emailFromToken.equals(emailAndPasswordRequestDto.getEmail())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email mismatch")
                    .details(null)
                    .build();
        }

        User user = userRepository.findByEmail(emailAndPasswordRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!user.isEmailVerified()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email not verified")
                    .details(null)
                    .build();
        }

        if(!user.isActive()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("User is not active")
                    .details(null)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(emailAndPasswordRequestDto.getPassword()));
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("Password reset successfully")
                .details(null)
                .build();
    }

}
