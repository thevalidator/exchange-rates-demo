/*
 * The Krechet Software
 */
package ru.alfabank.currency.service;

import ru.alfabank.currency.model.dto.GiphyResponseDTO;

/**
 *
 * @author theValidator <the.validator@yandex.ru>
 */
public interface GiphyService {
    
    GiphyResponseDTO getTaggedRandomResponse(String tag);
    
}
