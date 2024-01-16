package ru.kudryashov.springcource.springSecurityApp.util;

import java.time.LocalDateTime;

public class AuthErrorResponse {
    private String message;
    private LocalDateTime date;

    public AuthErrorResponse(String message, LocalDateTime date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
