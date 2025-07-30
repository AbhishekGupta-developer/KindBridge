package com.myorganisation.CareEmoPilot.service;

public interface EmailService {
    void sendOtp(String email);
    boolean verifyOtp(String email, String otp);
}
