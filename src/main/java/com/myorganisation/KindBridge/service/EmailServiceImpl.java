package com.myorganisation.KindBridge.service;

import com.myorganisation.KindBridge.constants.UserConstants;
import com.myorganisation.KindBridge.dto.request.EmailOtpVerificationRequestDto;
import com.myorganisation.KindBridge.dto.request.EmailRequestDto;
import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import com.myorganisation.KindBridge.enums.OtpPurpose;
import com.myorganisation.KindBridge.exception.InvalidOtpException;
import com.myorganisation.KindBridge.model.User;
import com.myorganisation.KindBridge.repository.UserRepository;
import com.myorganisation.KindBridge.store.OtpStore;
import com.myorganisation.KindBridge.util.JwtUtil;
import com.myorganisation.KindBridge.util.OtpUtil;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

        String subject = "KindBridge - " + (otpPurpose == OtpPurpose.SIGNUP ? "Signup OTP" : "Password Reset OTP");

        String purposeMessage = (otpPurpose == OtpPurpose.SIGNUP)
                ? "Use the OTP below to complete your signup process."
                : "Use the OTP below to reset your password.";

        String htmlBody = """
                <html>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <h2 style="color: #4CAF50;">KindBridge</h2>
                        <p>Hello,</p>
                        <p>%s</p>
                        <div style="padding: 10px; background-color: #f3f3f3; border-radius: 5px; display: inline-block;">
                            <h3 style="margin: 0; color: #333;">%s</h3>
                        </div>
                        <p>This OTP is valid for <b>5 minutes</b>. Please do not share it with anyone.</p>
                        <p>Thank you,<br/>KindBridge Team</p>
                    </body>
                </html>
                """.formatted(purposeMessage, otp);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Failed to send OTP email. Please try again later.")
                    .build();
        }

        return GenericResponseDto.builder()
                .success(true)
                .message("OTP sent to " + email)
                .details(Map.of("purpose", otpPurpose.name()))
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
                        .details(Map.of("signupToken", signupToken))
                        .build();

            } else if(otpPurpose == OtpPurpose.PASSWORD_RESET) {
                User user = userRepository.findByEmail(email).orElse(null);
                if(user != null && user.isActive()) {
                    String passwordResetToken = jwtUtil.generatePasswordResetToken(email);

                    return GenericResponseDto.builder()
                            .success(true)
                            .message("OTP verified")
                            .details(Map.of("passwordResetToken", passwordResetToken))
                            .build();
                }
            }
        }

        throw new InvalidOtpException("OTP verification failed");
    }

    @Override
    public GenericResponseDto sendSigninAlert(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        String userFullName = user.getFirstName() + " " + user.getLastName();
        String time = "Unknown";
        String ipAddress = "Unknown";
        String device = "Unknown";

        String subject = "KindBridge - Sign-in Alert";

        String htmlBody = """
                <html>
                    <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                        <h2 style="color: #4CAF50;">KindBridge</h2>
                        <p>Hello %s,</p>
                        <p>We noticed a sign-in to your KindBridge account.</p>
                        <p><strong>Sign-in Details:</strong></p>
                        <ul>
                            <li>Time: %s</li>
                            <li>IP Address: %s</li>
                            <li>Device: %s</li>
                        </ul>
                        <p>If this was you, no further action is needed. If you did not sign in, please reset your password immediately.</p>
                        <p>Thank you,<br/>KindBridge Team</p>
                    </body>
                </html>
                """.formatted(userFullName, time, ipAddress, device);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true enables HTML

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            return GenericResponseDto.builder()
                    .success(false)
                    .message("Failed to send OTP email. Please try again later.")
                    .build();
        }

        return GenericResponseDto.builder()
                .success(true)
                .message("OTP sent to " + email)
                .details(Map.of("purpose", "Sign-in Alert"))
                .build();
    }

}
