/*
 * The Krechet Software
 */
package ru.alfabank.currency.service;

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
import org.springframework.stereotype.Service;
import ru.alfabank.currency.client.ExchangeApiClient;
import ru.alfabank.currency.model.dto.GiphyResponseDTO;
import ru.alfabank.currency.model.dto.RatesResponseDTO;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Value("${currency.base}")
    private String baseCurrency;

    @Value("${giphy.tag.rich}")
    private String richTag;

    @Value("${giphy.tag.broke}")
    private String brokeTag;

    @Autowired
    private ExchangeApiClient exClient;

    @Autowired
    private GiphyService giphyService;

    @Override
    public ResponseEntity getCustomCurrencyResult(String currency) throws IOException {

        currency = currency.toUpperCase();

        Map<String, String> aviableCurrencies = getAviableCurrencies();
        if (!aviableCurrencies.containsKey(currency.toUpperCase())) {

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
                .getRatesByDateAndCurrency(yesterday.toString(), baseCurrency, currency);

        BigDecimal todaysRate = ratesForToday.getRates().get(currency);
        BigDecimal yesterdaysRate = ratesForYesterday.getRates().get(currency);

        String tag = brokeTag;
        if (todaysRate.compareTo(yesterdaysRate) == 1) {
            tag = richTag;
        }

        GiphyResponseDTO r = giphyService.getTaggedRandomResponse(tag);

        String link = "https://i.giphy.com/media/" + r.getData().getId() + "/giphy.gif";

        URL url = new URL(link);

        Resource resource = new UrlResource(url);

        byte[] gifBytes = IOUtils.toByteArray(resource.getInputStream());
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                        + tag + "_"
                        + currency + "_"
                        + date
                        + ".gif\"")
                .body(gifBytes);
    }

    @Override
    public Map<String, String> getAviableCurrencies() {
        return exClient.getCurrencies();
    }

}
