package com.jkos.hackathon.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SkillLevel {

    BEGINNER(1),
    INTERMEDIATE(2),
    ADVANCED(3);

    private final int level;


}
