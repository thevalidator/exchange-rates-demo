/*
 * The Krechet Software
 */
package ru.alfabank.currency.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import javax.net.ssl.HttpsURLConnection;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Value("${giphy.tag.rich}")
    private String richTag;

    @Value("${giphy.tag.broke}")
    private String brokeTag;

    @Autowired
    private ExchangeApiClient exClient;

    @Autowired
    private GiphyApiClient giphyClient;


    @GetMapping("/api/v1/rates")
    public RatesResponseDTO getAllRates() {
        return exClient.getLatestRates();
    }


    @GetMapping("/api/v1/rates/gif")
    public ResponseEntity getRates() throws MalformedURLException, IOException {

        RatesResponseDTO ratesForToday = exClient.getLatestRatesByCurrency(mainCurrency);

        long actualTimestampInMillis = Long.valueOf(ratesForToday.getTimestamp()) * 1_000;
        LocalDate yesterday = Instant.ofEpochMilli(actualTimestampInMillis)
                .atZone(ZoneId.of("UTC")).toLocalDate().minusDays(1);

        RatesResponseDTO ratesForYesterday = exClient
                .getRatesByDateAndCurrency(yesterday.toString(),
                        mainCurrency);

        BigDecimal todaysRate = ratesForToday.getRates().get(mainCurrency);
        BigDecimal yesterdaysRate = ratesForYesterday.getRates().get(mainCurrency);

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
                        "attachment; filename=\"result_" 
                                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) 
                                + ".gif\"")
                .body(IOUtils.toByteArray(resource.getInputStream()));
        
    }
    
    @GetMapping("/api/v1/rates/{currency}")
    public String getCustomCurrencyRates(@PathVariable String currency) {
        currency = currency.toUpperCase();
        RatesResponseDTO response = exClient.getLatestRatesByCurrency(currency);
        BigDecimal result = response.getRates().get(currency);

        return currency + ": " + result;

    }

    @GetMapping("/download/")
    public ResponseEntity downloadFileFromLocal() throws MalformedURLException, IOException {
        
        URL url = new URL("https://i.giphy.com/media/QegLtOtLZpsskzgjLH/giphy.gif");
        Resource resource = new UrlResource(url);
        //https://i.giphy.com/media/3oFzlWFLgj0t6LhEyY/giphy.gif
        
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_GIF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"fileGif.gif\"")
                .body(IOUtils.toByteArray(resource.getInputStream()));
    }

}
