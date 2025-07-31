package com.myorganisation.CareEmoPilot.service;

import com.myorganisation.CareEmoPilot.repository.UserRepository;
import com.myorganisation.CareEmoPilot.store.OtpStore;
import com.myorganisation.CareEmoPilot.util.OtpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void sendOtp(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already registered.");
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
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = OtpStore.getOtp(email);
        if(storedOtp != null && storedOtp.equals(otp)) {
            OtpStore.clearOtp(email); // Clear after successful verification
            return true;
        }
        return false;
    }
}
