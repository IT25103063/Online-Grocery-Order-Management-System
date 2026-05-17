package com.groceryhub.exception;

public class AuthenticationException extends BaseException {
    public AuthenticationException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
