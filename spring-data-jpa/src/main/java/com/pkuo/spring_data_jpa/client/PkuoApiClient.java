package com.pkuo.spring_data_jpa.client;

import com.pkuo.spring_data_jpa.dto.PkuoEntityDTO;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * Declarative HTTP client for the pkuo API
 */
@HttpExchange(url = "https://pkuo")
public interface PkuoApiClient {

    /**
     * Get entity by ID
     *
     * @param id the entity ID
     * @return the entity data
     */
    @GetExchange("/entity")
    PkuoEntityDTO getEntityById(@org.springframework.web.bind.annotation.RequestParam("id") Long id);

}
