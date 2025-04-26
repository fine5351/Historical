package com.jkos.mybatisflex.config;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Handler for automatically filling createdAt and updatedAt fields with the current date and time.
 */
@Component
public class DateTimeFillHandler {

    /**
     * Fill createdAt and updatedAt fields for insert operations.
     *
     * @param entity The entity to fill
     */
    public void fillInsert(Object entity) {
        if (entity == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        try {
            // Try to set createdAt field
            setFieldValue(entity, "createdAt", now);

            // Try to set updatedAt field
            setFieldValue(entity, "updatedAt", now);
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Fill updatedAt field for update operations.
     *
     * @param entity The entity to fill
     */
    public void fillUpdate(Object entity) {
        if (entity == null) {
            return;
        }

        try {
            // Try to set updatedAt field
            setFieldValue(entity, "updatedAt", LocalDateTime.now());
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Set field value using reflection.
     *
     * @param entity    The entity
     * @param fieldName The field name
     * @param value     The value to set
     */
    private void setFieldValue(Object entity, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = entity.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(entity, value);
    }

}