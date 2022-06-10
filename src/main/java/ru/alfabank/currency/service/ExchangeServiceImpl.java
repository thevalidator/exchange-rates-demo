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
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alfabank.currency.client.ExchangeApiClient;
import ru.alfabank.currency.client.exception.NoSuchCurrencyException;
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
    public ResponseEntity<byte[]> getCustomCurrencyResult(String currency) throws IOException {

        currency = currency.toUpperCase();

        if (!isExists(currency)) {
            throw new NoSuchCurrencyException(currency);
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

        byte[] gifBytes = getGifBytes(tag);
        
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
    public Map<String, String> getAvailableCurrencies() {
        return exClient.getCurrencies();
    }

    private boolean isExists(String currency) {

        Map<String, String> availableCurrencies = getAvailableCurrencies();
        if (!availableCurrencies.containsKey(currency.toUpperCase())) {
            return false;
        }

        return true;
    }
    
    private byte[] getGifBytes(String tag) throws IOException {
        
        GiphyResponseDTO r = giphyService.getTaggedRandomResponse(tag);

        String link = "https://i.giphy.com/media/" + r.getData().getId() + "/giphy.gif";

        URL url = new URL(link);

        Resource resource = new UrlResource(url);

        return IOUtils.toByteArray(resource.getInputStream());
        
    }

}
