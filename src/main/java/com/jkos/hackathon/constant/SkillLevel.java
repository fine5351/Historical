package com.jkos.hackathon.constant;

public enum SkillLevel {

    ADVANCED,
    BEGINNER,
    INTERMEDIATE,
    ;


    public static SkillLevel getFromOrdinal(int ordinal) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (skillLevel.ordinal() == ordinal) {
                return skillLevel;
            }
        }

        throw new IllegalArgumentException("SkillLevel for %s not found".formatted(ordinal));
    }

}
