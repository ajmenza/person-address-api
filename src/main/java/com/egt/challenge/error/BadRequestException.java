package com.egt.challenge.error;

import lombok.Data;

import java.util.List;

@Data
public class BadRequestException extends Exception {

    private List<String> messages;


    public BadRequestException() {
        super();
    }

    public BadRequestException(List<String> messages) {
        super();
        this.messages = messages;
    }


    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BadRequestException(String message, Throwable cause,
                                  boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
