package com.finekuo.normalcore.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class HistoricalException extends RuntimeException {

    private String code;
    private String message;

}
