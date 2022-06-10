/*
 * The Krechet Software
 */

package ru.alfabank.currency.util;

import feign.FeignException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@RestControllerAdvice
public class FeignExceptionHandler {
    
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException e, HttpServletResponse response) {
        
        return new ResponseEntity(e.getMessage(), HttpStatus.resolve(e.status()));
        
    }

}
