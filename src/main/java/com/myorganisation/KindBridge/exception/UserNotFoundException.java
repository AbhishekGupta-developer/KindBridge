package com.myorganisation.KindBridge.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("User doesn't exist");
    }

    public UserNotFoundException(String m) {
        super(m);
    }
}
