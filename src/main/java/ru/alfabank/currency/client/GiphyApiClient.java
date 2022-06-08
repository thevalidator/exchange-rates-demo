/*
 * The Krechet Software
 */
package ru.alfabank.currency.client;

import ru.alfabank.currency.client.config.GiphyFeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfabank.currency.model.dto.GiphyResponseDTO;

/**
 *
 * @author theValidator <the.validator@yandex.ru>
 */
@FeignClient(name="${giphy.feign.config.name}", 
        url="${giphy.feign.config.url}", 
        configuration = GiphyFeignClientConfig.class)
public interface GiphyApiClient {
    
    @GetMapping("/random")
    public GiphyResponseDTO getTaggedRandomResponse(@RequestParam("tag") String tag);
    
}
