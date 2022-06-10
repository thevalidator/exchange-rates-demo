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
import ru.alfabank.currency.client.exception.NoSuchCurrencyException;
import ru.alfabank.currency.client.exception.WrongCurrencyFormatException;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException e, HttpServletResponse response) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.resolve(e.status()));

    }

    @ExceptionHandler(WrongCurrencyFormatException.class)
    public ResponseEntity<String> handleWrongCurrencyFormatException(WrongCurrencyFormatException e, HttpServletResponse response) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

    }
    
    @ExceptionHandler(NoSuchCurrencyException.class)
    public ResponseEntity<String> handleNoSuchgCurrencyException(NoSuchCurrencyException e, HttpServletResponse response) {

        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletResponse response) {

        return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.BAD_REQUEST);

    }

}
