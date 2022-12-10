package com.egt.challenge.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class ErrorMessage {
    private HttpStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> messages;

    public ErrorMessage(HttpStatus status) {
        this.status = status;
    }

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorMessage(HttpStatus status, List<String> messages) {
        this.status = status;
        this.messages = messages;
    }


}
