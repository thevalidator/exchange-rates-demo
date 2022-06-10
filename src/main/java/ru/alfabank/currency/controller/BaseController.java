/*
 * The Krechet Software
 */
package ru.alfabank.currency.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.currency.client.ExchangeApiClient;
import ru.alfabank.currency.client.GiphyApiClient;
import ru.alfabank.currency.model.dto.GiphyResponseDTO;
import ru.alfabank.currency.model.dto.RatesResponseDTO;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@RestController
public class BaseController {

    @Value("${currency.main}")
    private String mainCurrency;
    
    @Value("${currency.base}")
    private String baseCurrency;
    
    @Value("${giphy.tag.rich}")
    private String richTag;

    @Value("${giphy.tag.broke}")
    private String brokeTag;

    @Autowired
    private ExchangeApiClient exClient;

    @Autowired
    private GiphyApiClient giphyClient;
    

    @PostMapping("/api/v1/rates/main")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void postGetResult() {
    }
    
    @PostMapping("/api/v1/rates/{currency}")
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void postCustomCurrencyResult() {
    }

    @GetMapping("/api/v1/rates/main")
    public ResponseEntity getMainCurrencyResult() throws IOException {

        return getCustomCurrencyResult(mainCurrency);

    }

    @GetMapping("/api/v1/rates/{currency}")
    public ResponseEntity getCustomCurrencyResult(@PathVariable String currency) throws IOException {

        currency = currency.toUpperCase();

        Map<String, String> currencies = exClient.getCurrencies();
        if (!currencies.containsKey(currency.toUpperCase())) {

            Map<String, String> map = new HashMap<>();
            map.put("message", HttpStatus.NOT_FOUND.toString());
            map.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));
            map.put("description", "No such currency: " + currency);

            return new ResponseEntity(map, HttpStatus.NOT_FOUND);
        }

        RatesResponseDTO ratesForToday = exClient.getLatestRatesByCurrency(baseCurrency, currency);

        long actualTimestampInMillis = Long.valueOf(ratesForToday.getTimestamp()) * 1_000;
        LocalDate yesterday = Instant.ofEpochMilli(actualTimestampInMillis)
                .atZone(ZoneId.of("UTC")).toLocalDate().minusDays(1);

        RatesResponseDTO ratesForYesterday = exClient
                .getRatesByDateAndCurrency(yesterday.toString(),
                        baseCurrency, currency);

        BigDecimal todaysRate = ratesForToday.getRates().get(currency);
        BigDecimal yesterdaysRate = ratesForYesterday.getRates().get(currency);

        String tag = brokeTag;
        if (todaysRate.compareTo(yesterdaysRate) == 1) {
            tag = richTag;
        }

        GiphyResponseDTO r = giphyClient.getTaggedRandomResponse(tag);
        String link = "https://i.giphy.com/media/" + r.getData().getId() + "/giphy.gif";
        URL url = new URL(link);
        Resource resource = new UrlResource(url);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + tag + "_" + currency + "_"
                        + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + ".gif\"")
                .body(IOUtils.toByteArray(resource.getInputStream()));

    }

}
