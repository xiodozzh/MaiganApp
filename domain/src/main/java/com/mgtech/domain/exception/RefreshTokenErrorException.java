package com.mgtech.domain.exception;

import java.io.IOException;

/**
 * @author zhaixiang
 */
public class RefreshTokenErrorException extends IOException {
    public RefreshTokenErrorException() {
    }

    public RefreshTokenErrorException(String message) {
        super(message);
    }
}
