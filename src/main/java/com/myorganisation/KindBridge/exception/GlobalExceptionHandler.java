package com.myorganisation.KindBridge.exception;

import com.myorganisation.KindBridge.dto.response.GenericResponseDto;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        GenericResponseDto genericResponseDto = new GenericResponseDto();
        genericResponseDto.setSuccess(false);
        genericResponseDto.setMessage(e.getMessage());

        return new ResponseEntity<>(genericResponseDto, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponseDto> handleValidationExceptions(MethodArgumentNotValidException e) {
        GenericResponseDto genericResponseDto = new GenericResponseDto();
        genericResponseDto.setSuccess(false);
        genericResponseDto.setMessage(e.getMessage());

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        genericResponseDto.setDetails(errors);

        return new ResponseEntity<>(genericResponseDto, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<GenericResponseDto> handleInvalidOtpException(InvalidOtpException e) {
        GenericResponseDto genericResponseDto = new GenericResponseDto();
        genericResponseDto.setSuccess(false);
        genericResponseDto.setMessage(e.getMessage());

        return new ResponseEntity<>(genericResponseDto, HttpStatusCode.valueOf(400));
    }
}
