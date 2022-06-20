package com.yandex.mega_market.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
public class ValidationException extends CustomException {

    public ValidationException() {
        super( 400, HttpStatus.BAD_REQUEST, "Validation Failed" );
    }

}
