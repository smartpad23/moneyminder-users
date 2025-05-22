package com.moneyminder.moneyminderusers.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BadCredentialsException extends AuthenticationException {
    public BadCredentialsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadCredentialsException(String msg) {
        super(msg);
    }
}
