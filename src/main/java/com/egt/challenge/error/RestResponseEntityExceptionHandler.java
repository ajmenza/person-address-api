package com.egt.challenge.error;

import com.egt.challenge.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> invalidPersonDataExceptions(BadRequestException exception, WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST);
        if (exception.getMessages() != null) {
            message.setMessages(exception.getMessages());
        } else if (exception.getMessage() != null) {
            message.setMessage(exception.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);

    }
}
