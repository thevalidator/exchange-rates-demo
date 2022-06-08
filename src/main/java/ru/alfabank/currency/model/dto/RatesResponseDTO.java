/*
 * The Krechet Software
 */
package ru.alfabank.currency.model.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class RatesResponseDTO {

//    private String disclaimer;
//    private String license;
    private Integer timestamp;
    private String base;
    private Map<String, BigDecimal> rates = new HashMap<>();

//    public String getDisclaimer() {
//        return disclaimer;
//    }
//
//    public void setDisclaimer(String disclaimer) {
//        this.disclaimer = disclaimer;
//    }
//
//    public String getLicense() {
//        return license;
//    }
//
//    public void setLicense(String license) {
//        this.license = license;
//    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, BigDecimal> getRates() {
        return rates;
    }

    public void setRates(Map<String, BigDecimal> rates) {
        this.rates = rates;
    }

}
