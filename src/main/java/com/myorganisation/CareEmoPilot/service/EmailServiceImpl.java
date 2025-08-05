package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.constants.UserConstants;
import com.myorganisation.CareEmoPilot.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.CareEmoPilot.dto.request.EmailRequestDto;
import com.myorganisation.CareEmoPilot.dto.response.GenericResponseDto;
import com.myorganisation.CareEmoPilot.enums.OtpPurpose;
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
    public GenericResponseDto sendOtp(EmailRequestDto emailRequestDto, OtpPurpose otpPurpose) {
        String email = emailRequestDto.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        if(otpPurpose == OtpPurpose.SIGNUP) {
            if(user != null && user.isActive()) {
                return GenericResponseDto.builder()
                        .success(false)
                        .message("Email is already registered.")
                        .build();
            }
        } else if(otpPurpose == OtpPurpose.PASSWORD_RESET) {
            if(user == null || !user.isActive()) {
                return GenericResponseDto.builder()
                        .success(false)
                        .message("Email is not registered.")
                        .build();
            }
        }

        String otp = OtpUtil.generateOtp();
        OtpStore.storeOtp(email, otp);

        String subject = "CareEmoPilot OTP Code";

        StringBuilder mailBody = new StringBuilder();
        mailBody.append("Your OTP is: ").append(otp).append("\n");
        String messagePostBody = (otpPurpose == OtpPurpose.SIGNUP) ?
                "This OTP is to Signup to CareEmoPilot and it is valid for 5 min only. Do not share it with anyone." :
                "This OTP is to reset password of your CareEmoPilot account and it is valid for 5 min only. Do not share it with anyone.";
        mailBody.append(messagePostBody);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(mailBody.toString());

        mailSender.send(mailMessage);

        return GenericResponseDto.builder()
                .success(true)
                .message("OTP sent to " + email)
                .data(Map.of("Purpose", otpPurpose.name()))
                .build();
    }

    @Override
    public GenericResponseDto verifyOtp(EmailOtpVerificationRequestDto emailOtpVerificationRequestDto, OtpPurpose otpPurpose) {
        String email = emailOtpVerificationRequestDto.getEmail();
        String otp = emailOtpVerificationRequestDto.getOtp();

        String storedOtp = OtpStore.getOtp(email);

        if(storedOtp != null && storedOtp.equals(otp)) {
            OtpStore.clearOtp(email); // Clear after successful verification

            if(otpPurpose == OtpPurpose.SIGNUP) {
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

            } else if(otpPurpose == OtpPurpose.PASSWORD_RESET) {
                User user = userRepository.findByEmail(email).orElse(null);
                if(user != null && user.isActive()) {
                    String passwordResetToken = jwtUtil.generatePasswordResetToken(email);

                    return GenericResponseDto.builder()
                            .success(true)
                            .message("OTP verified")
                            .data(Map.of("passwordResetToken", passwordResetToken))
                            .build();
                }
            }
        }

        return GenericResponseDto.builder()
                .success(false)
                .message("OTP verification failed")
                .data(null)
                .build();
    }

}
