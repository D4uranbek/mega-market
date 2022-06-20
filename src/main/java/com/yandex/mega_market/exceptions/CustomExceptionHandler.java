package com.yandex.mega_market.exceptions;

import com.yandex.mega_market.DTOs.Error;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@ControllerAdvice( "com.yandex.mega_market" )
public class CustomExceptionHandler {

    @ExceptionHandler( { CustomException.class } )
    public ResponseEntity<Error> exceptionHandler( CustomException e ) {
        return new ResponseEntity<>( new Error( e.getCode(), e.getMessage() ),
                e.getStatus() );
    }

}
