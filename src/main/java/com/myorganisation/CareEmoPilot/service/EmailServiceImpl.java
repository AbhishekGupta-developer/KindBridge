package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.constants.UserConstants;
import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.model.User;
import com.myorganisation.CareEmoPilot.repository.UserRepository;
import com.myorganisation.CareEmoPilot.store.OtpStore;
import com.myorganisation.CareEmoPilot.util.JwtUtil;
import com.myorganisation.CareEmoPilot.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public GenericResponseDto sendOtp(EmailRequestDto emailRequestDto) {
        String email = emailRequestDto.getEmail();
        if(userRepository.existsByEmail(email)) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            if(user.isActive()) {
                return GenericResponseDto.builder()
                        .success(false)
                        .message("Email is already registered.")
                        .build();
            }
        }

        String otp = OtpUtil.generateOtp();
        OtpStore.storeOtp(email, otp);

        String subject = "Your OTP Code";
        String message = "Your OTP is: " + otp + "\nIt is valid for 5 minutes.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);

        return GenericResponseDto.builder()
                .success(true)
                .message("OTP sent to " + email)
                .build();
    }

    @Override
    public GenericResponseDto verifyOtp(EmailOtpVerificationRequestDto emailOtpVerificationRequestDto) {
        String email = emailOtpVerificationRequestDto.getEmail();
        String otp = emailOtpVerificationRequestDto.getOtp();

        String storedOtp = OtpStore.getOtp(email);
        if(storedOtp != null && storedOtp.equals(otp)) {
            OtpStore.clearOtp(email); // Clear after successful verification

            // Create user record with email only
            if(!userRepository.existsByEmail(email)) {
                User user = User.builder()
                        .email(email)
                        .isEmailVerified(true)
                        .password(UserConstants.PASSWORD_NOT_SET)  // Will be updated on Signup
                        .active(false)
                        .build();
                userRepository.save(user);
            }

            String signupToken = jwtUtil.generateSignupToken(email);

            return GenericResponseDto.builder()
                    .success(true)
                    .message("OTP verified")
                    .data(Map.of("signupToken", signupToken))
                    .build();
        }
        return GenericResponseDto.builder()
                .success(false)
                .message("OTP verification failed")
                .data(null)
                .build();
    }
}
