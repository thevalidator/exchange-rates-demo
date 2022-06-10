/*
 * The Krechet Software
 */
package ru.alfabank.currency.controller;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.currency.client.exception.WrongCurrencyFormatException;
import ru.alfabank.currency.service.ExchangeService;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@RestController
public class BaseController {

    @Value("${currency.main}")
    private String mainCurrency;

    @Autowired
    private ExchangeService exchangeService;

    @PostMapping("/api/v1/rates/main")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void postGetResult() {
    }

    @PostMapping("/api/v1/rates/{currency}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void postCustomCurrencyResult() {
    }

    @GetMapping("/api/v1/rates")
    public ResponseEntity<Map<String, String>> getAllAvailableCurrencies() {

        Map<String, String> map = exchangeService.getAvailableCurrencies();
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/api/v1/rates/main")
    public ResponseEntity<byte[]> getMainCurrencyResult() throws IOException {

        return exchangeService.getCustomCurrencyResult(mainCurrency);

    }

    @GetMapping("/api/v1/rates/{currency}")
    public ResponseEntity<byte[]> getCustomCurrencyResult(@PathVariable String currency) throws IOException {

        if (!currency.matches("[a-zA-Z]{3}")) {
            throw new WrongCurrencyFormatException();
        }

        return exchangeService.getCustomCurrencyResult(currency);

    }

}
