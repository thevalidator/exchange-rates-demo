/*
 * The Krechet Software
 */

package ru.alfabank.currency.client.exception;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class NoSuchCurrencyException extends RuntimeException {
    
    private String inputdata;

    public NoSuchCurrencyException(String inputdata) {
        this.inputdata = inputdata;
    }

    public String getMessage() {
        return "Currency \"" + inputdata + "\" doesn't exist!";
    }
    
    
    
    
    

}
