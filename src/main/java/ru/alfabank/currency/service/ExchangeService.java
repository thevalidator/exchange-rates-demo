/*
 * The Krechet Software
 */
package ru.alfabank.currency.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author theValidator <the.validator@yandex.ru>
 */
public interface ExchangeService {

    ResponseEntity<byte[]> getCustomCurrencyResult(String currency) throws IOException;

    Map<String, String> getAvailableCurrencies();

}
