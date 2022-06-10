/*
 * The Krechet Software
 */
package ru.alfabank.currency.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.currency.client.GiphyApiClient;
import ru.alfabank.currency.model.dto.GiphyResponseDTO;

/**
 * @author theValidator <the.validator@yandex.ru>
 */
@Service
public class GiphyServiceImpl implements GiphyService {

    @Autowired
    private GiphyApiClient giphyClient;

    @Override
    public GiphyResponseDTO getTaggedRandomResponse(String tag) {
        return giphyClient.getTaggedRandomResponse(tag);
    }

}
