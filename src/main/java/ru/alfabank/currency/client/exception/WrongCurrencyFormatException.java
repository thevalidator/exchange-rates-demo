/*
 * The Krechet Software
 */

package ru.alfabank.currency.client.exception;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class WrongCurrencyFormatException extends RuntimeException {
    
    private String message = "WRONG INPUT DATA! Currency code must contain 3 letters (A-Z).";

    public WrongCurrencyFormatException() {
    }

    public String getMessage() {
        return message;
    }
    

}
