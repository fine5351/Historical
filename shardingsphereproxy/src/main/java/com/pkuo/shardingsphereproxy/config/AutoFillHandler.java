package com.pkuo.shardingsphereproxy.config;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Auto-fill handler for entity fields.
 * This handler automatically fills in the createdAt, updatedAt, createdBy, and updatedBy fields
 * when an entity is inserted or updated.
 */
@Component
public class AutoFillHandler {

    /**
     * Get the current user name.
     * In a real application, this would come from the security context or user session.
     *
     * @return The current user name
     */
    private String getCurrentUserName() {
        // In a real application, this would come from the security context
        // For example: SecurityContextHolder.getContext().getAuthentication().getName()
        return "system";
    }

    /**
     * Called before an entity is inserted.
     *
     * @param entity The entity to be inserted
     */
    public void onInsert(Object entity) {
        if (entity == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String userName = getCurrentUserName();

        // Set created timestamp
        setFieldValue(entity, "createdAt", now);

        // Set updated timestamp
        setFieldValue(entity, "updatedAt", now);

        // Set created by
        setFieldValue(entity, "createdBy", userName);

        // Set updated by
        setFieldValue(entity, "updatedBy", userName);
    }

    /**
     * Called before an entity is updated.
     *
     * @param entity The entity to be updated
     */
    public void onUpdate(Object entity) {
        if (entity == null) {
            return;
        }

        // Set updated timestamp
        setFieldValue(entity, "updatedAt", LocalDateTime.now());

        // Set updated by
        setFieldValue(entity, "updatedBy", getCurrentUserName());
    }

    /**
     * Set a field value on an entity using reflection.
     *
     * @param entity    The entity
     * @param fieldName The name of the field to set
     * @param value     The value to set
     */
    private void setFieldValue(Object entity, String fieldName, Object value) {
        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(entity, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // Field doesn't exist or can't be accessed, ignore
        }
    }

    /**
     * Fill insert fields.
     *
     * @param entity The entity to fill
     */
    public void fillInsert(Object entity) {
        onInsert(entity);
    }

    /**
     * Fill update fields.
     *
     * @param entity The entity to fill
     */
    public void fillUpdate(Object entity) {
        onUpdate(entity);
    }

}
