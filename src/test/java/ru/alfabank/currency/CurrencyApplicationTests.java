package ru.alfabank.currency;

import com.github.tomakehurst.wiremock.client.WireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import java.util.HashMap;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import ru.alfabank.currency.client.ExchangeApiClient;
import ru.alfabank.currency.model.dto.RatesResponseDTO;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CurrencyApplicationTests {

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("exchange.feign.config.url", wireMockServer::baseUrl);
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Autowired
    private ExchangeApiClient client;

    @Test
    void feignClientGetCurrenciesTest() {

        wireMockServer.stubFor(
                WireMock.get("/currencies.json")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody("{\"AED\": \"United Arab Emirates Dirham\", \"AFN\": \"Afghan Afghani\"}"))
        );

        this.client
                .getCurrencies()
                .get("AFN")
                .equals("Afghan Afghani");

    }

    @Test
    void feignClientGetRateTest() {

        Map<String, StringValuePattern> parameters = new HashMap<>();
        parameters.put("base", matching("[A-Z]{3}"));
        parameters.put("symbols", matching("[A-Z]{3}"));

        
        wireMockServer.stubFor(
                WireMock.get("/latest.json?base=USD&symbols=AWG")
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody("{\n"
                                        + "    \"disclaimer\": \"Usage subject to terms: https://openexchangerates.org/terms\",\n"
                                        + "    \"license\": \"https://openexchangerates.org/license\",\n"
                                        + "    \"timestamp\": 1654811989,\n"
                                        + "    \"base\": \"USD\",\n"
                                        + "    \"rates\": {\n"
                                        + "        \"AWG\": 1.8\n"
                                        + "    }\n"
                                        + "}"))
        );

        RatesResponseDTO response = this.client
                .getLatestRatesByCurrency("USD", "AWG");
        
        assertThat(response.getBase()).isEqualTo("USD");
        assertThat(response.getTimestamp()).isEqualTo(1654811989);
        
    }

}
