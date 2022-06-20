package com.yandex.mega_market.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@Getter
public class CustomException extends RuntimeException {

    private final int code;
    private final HttpStatus status;

    public CustomException( int code, HttpStatus status, String message ) {
        super( message );
        this.code = code;
        this.status = status;
    }
}
