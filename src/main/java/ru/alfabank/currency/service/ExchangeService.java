/*
 * The Krechet Software
 */
package ru.alfabank.currency.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author theValidator <the.validator@yandex.ru>
 */
public interface ExchangeService {
    
    ResponseEntity getCustomCurrencyResult(String currency);
    Map<String, String> getAviableCurrencies();
    
}
