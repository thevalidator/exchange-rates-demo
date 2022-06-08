/*
 * The Krechet Software
 */

package ru.alfabank.currency.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
public class Data {
    
    private String id;
    
    @JsonProperty("embed_url")
    private String embedUrl;

     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }    

}
