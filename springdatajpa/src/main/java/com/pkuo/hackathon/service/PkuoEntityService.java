package com.jkos.hackathon.service;

import com.jkos.hackathon.client.PkuoApiClient;
import com.jkos.hackathon.dto.PkuoEntityDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for interacting with the pkuo API
 */
@Service
@RequiredArgsConstructor
public class PkuoEntityService {

    private final PkuoApiClient pkuoApiClient;

    /**
     * Get entity by ID
     *
     * @param id the entity ID
     * @return the entity data
     */
    public PkuoEntityDTO getEntityById(Long id) {
        return pkuoApiClient.getEntityById(id);
    }

}