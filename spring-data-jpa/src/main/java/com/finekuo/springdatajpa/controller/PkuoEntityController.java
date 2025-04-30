package com.finekuo.springdatajpa.controller;

import com.finekuo.springdatajpa.dto.PkuoEntityDTO;
import com.finekuo.springdatajpa.dto.response.BaseResponse;
import com.finekuo.springdatajpa.service.PkuoEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for pkuo entity operations
 */
@RestController
@RequestMapping("/api/pkuo")
@RequiredArgsConstructor
public class PkuoEntityController {

    private final PkuoEntityService pkuoEntityService;

    /**
     * Get entity by ID
     *
     * @param id the entity ID
     * @return the entity data
     */
    @GetMapping("/entity")
    public ResponseEntity<BaseResponse<PkuoEntityDTO>> getEntityById(@RequestParam Long id) {
        PkuoEntityDTO entity = pkuoEntityService.getEntityById(id);
        return ResponseEntity.ok(BaseResponse.success(entity));
    }

}
