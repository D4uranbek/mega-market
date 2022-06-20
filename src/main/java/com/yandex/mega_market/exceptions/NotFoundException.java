package com.yandex.mega_market.exceptions;

import org.springframework.http.HttpStatus;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
public class NotFoundException extends CustomException {

    public NotFoundException() {
        super( 404, HttpStatus.NOT_FOUND, "Item not found" );
    }

}
