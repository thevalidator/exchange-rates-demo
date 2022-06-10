/*
 * The Krechet Software
 */

package ru.alfabank.currency.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;


/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class ExchangeServiceImpl implements ExchangeService {

    @Override
    public ResponseEntity getCustomCurrencyResult(String currency) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, String> getAviableCurrencies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

}
