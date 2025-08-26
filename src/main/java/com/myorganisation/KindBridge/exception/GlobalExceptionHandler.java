package com.myorganisation.KindBridge.exception;

import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        GenericResponseDto genericResponseDto = new GenericResponseDto();
        genericResponseDto.setSuccess(false);
        genericResponseDto.setMessage("User not found");

        return new ResponseEntity<>(genericResponseDto, HttpStatusCode.valueOf(404));
    }
}
