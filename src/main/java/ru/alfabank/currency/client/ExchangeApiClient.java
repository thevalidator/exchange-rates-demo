/*
 * The Krechet Software
 */
package ru.alfabank.currency.client;

import java.util.Map;
import ru.alfabank.currency.client.config.ExchangeFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfabank.currency.model.dto.RatesResponseDTO;

/**
 *
 * @author theValidator <the.validator@yandex.ru>
 */
@FeignClient(name = "${exchange.feign.config.name}",
        url = "${exchange.feign.config.url}",
        configuration = ExchangeFeignClientConfig.class)
public interface ExchangeApiClient {
    
    @GetMapping("/currencies.json")
    Map<String, String> getCurrencies();

    @GetMapping("/latest.json")
    RatesResponseDTO getLatestRatesByCurrency(@RequestParam("base") String baseCurrency, 
            @RequestParam("symbols") String currency);

    @GetMapping("/historical/{date}.json")
    RatesResponseDTO getRatesByDateAndCurrency(@PathVariable String date,
            @RequestParam("base") String baseCurrency, @RequestParam("symbols") String currency);

}
