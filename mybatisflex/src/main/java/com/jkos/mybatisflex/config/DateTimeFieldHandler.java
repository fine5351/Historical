package com.jkos.mybatisflex.config;

import java.time.LocalDateTime;

/**
 * Handler for automatically filling createdAt and updatedAt fields with the current date and time.
 */
public class DateTimeFieldHandler {

    /**
     * Get the current date and time for field filling.
     *
     * @return Current LocalDateTime
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

}