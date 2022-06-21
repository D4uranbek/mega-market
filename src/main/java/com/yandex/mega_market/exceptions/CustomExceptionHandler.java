package com.yandex.mega_market.exceptions;

import com.yandex.mega_market.DTOs.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author D4uranbek
 * @since 17.06.2022
 */
@ControllerAdvice( "com.yandex.mega_market" )
public class CustomExceptionHandler {

    @ExceptionHandler( { CustomException.class, Exception.class } )
    public ResponseEntity<Error> exceptionHandler( Exception e ) {

        if ( e instanceof CustomException customException ) {
            return new ResponseEntity<>( new Error( customException.getCode(), customException.getMessage() ),
                    customException.getStatus() );
        }
        return new ResponseEntity<>( new Error( 400, "Validation Failed" ),
                HttpStatus.BAD_REQUEST );

    }

}
