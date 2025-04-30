package com.pkuo.spring_data_jpa.constant;

public enum SkillLevel {

    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    GOOD_TO_HAVE;


    public static SkillLevel getFromOrdinal(int ordinal) {
        for (SkillLevel skillLevel : SkillLevel.values()) {
            if (skillLevel.ordinal() == ordinal) {
                return skillLevel;
            }
        }

        throw new IllegalArgumentException("SkillLevel for %s not found".formatted(ordinal));
    }

}
