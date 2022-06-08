/*
 * The Krechet Software
 */

package ru.alfabank.currency.client.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class ExchangeFeignClientConfig {
    
    @Value("${openexchangerates.token}")
    private String token;
    
    @Value("${currency.base}")
    private String baseCurrency;
    
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Authorization", "Token " + token);
            requestTemplate.query("base", baseCurrency);
        };
    }

}
