package com.myorganisation.KindBridge.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException() {
        super("Invalid OTP, verification failed");
    }

    public InvalidOtpException(String m) {
        super(m);
    }
}
