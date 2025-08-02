package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.constants.UserConstants;
import com.myorganisation.CareEmoPilot.dto.request.SigninRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.SignupRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.model.User;
import com.myorganisation.CareEmoPilot.repository.UserRepository;
import com.myorganisation.CareEmoPilot.util.JwtUtil;
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
    public GenericResponseDto signup(String authHeader, SignupRequestDto signupRequestDto) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return GenericResponseDto.builder()
                            .success(false)
                            .message("Missing or malformed token")
                            .data(null)
                            .build();
        }

        String signupToken = authHeader.substring(7);

        if(signupToken == null || !jwtUtil.isValidSignupToken(signupToken)) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Invalid or expired signup token")
                    .data(null)
                    .build();
        }

        String emailFromToken = jwtUtil.extractEmail(signupToken);
        if(!emailFromToken.equals(signupRequestDto.getEmail())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email mismatch")
                    .data(null)
                    .build();
        }

        User user = userRepository.findByEmail(signupRequestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!Boolean.TRUE.equals(user.getIsEmailVerified())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email not verified")
                    .data(null)
                    .build();
        }

        if(user.getPassword() != null && !UserConstants.PASSWORD_NOT_SET.equals(user.getPassword())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Password already set")
                    .data(null)
                    .build();
        }

        user.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        user.setActive(true);
        userRepository.save(user);

        return GenericResponseDto.builder()
                .success(true)
                .message("Signup complete")
                .data(null)
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
                    .data(null)
                    .build();
        }

        if(!user.isActive()) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Account not active")
                    .data(null)
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
                    .data(null)
                    .build();
        }

        if(!Boolean.TRUE.equals(user.getIsEmailVerified())) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Email not verified")
                    .data(null)
                    .build();
        }

        String authToken = jwtUtil.generateAuthToken(user.getEmail());

        return GenericResponseDto.builder()
                .success(true)
                .message("Signin successful")
                .data(Map.of("authToken", authToken))
                .build();
    }
}
