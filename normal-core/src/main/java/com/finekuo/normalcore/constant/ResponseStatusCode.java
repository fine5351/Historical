package com.finekuo.normalcore.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseStatusCode {

    SUCCESS("0000"),
    FAILURE("9999"),
    NOT_FOUND("0001"),
    BAD_REQUEST("0002"),
    ;

    private final String code;

}
