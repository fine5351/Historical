package com.finekuo.normalcore.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseStatusCode {

    SUCCESS("0"),
    FAILURE("1"),
    ;

    private final String code;

}
