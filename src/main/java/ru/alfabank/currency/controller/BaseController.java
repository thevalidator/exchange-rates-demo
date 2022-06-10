/*
 * The Krechet Software
 */
package ru.alfabank.currency.controller;

import java.io.IOException;
import java.util.HashMap;
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
    public ResponseEntity getAllAviableCurrencies() {

        Map<String, String> map = exchangeService.getAviableCurrencies();
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @GetMapping("/api/v1/rates/main")
    public ResponseEntity getMainCurrencyResult() throws IOException {

        return exchangeService.getCustomCurrencyResult(mainCurrency);

    }

    @GetMapping("/api/v1/rates/{currency}")
    public ResponseEntity getCustomCurrencyResult(@PathVariable String currency) throws IOException {

        if (!currency.matches("[a-zA-Z]{3}")) {

            Map<String, String> map = new HashMap<>();
            map.put("message", HttpStatus.NOT_FOUND.toString());
            map.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));
            map.put("description", "Wrong currency format, must be 3-letters format ");

            return new ResponseEntity(map, HttpStatus.NOT_FOUND);

        }

        return exchangeService.getCustomCurrencyResult(currency);

    }

}
