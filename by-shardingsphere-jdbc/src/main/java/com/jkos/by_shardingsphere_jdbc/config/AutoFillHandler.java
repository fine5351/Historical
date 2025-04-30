package com.jkos.by_shardingsphere_jdbc.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Handler for automatically filling createdAt, updatedAt, createdBy, and updatedBy fields.
 */
@Component
public class AutoFillHandler {

    // Default user for createdBy and updatedBy fields
    private static final String DEFAULT_USER = "mybatis-flex-system";

    /**
     * Get the current user name from the request header or return the default user.
     *
     * @return The current user name or the default user
     */
    private String getCurrentUserName() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String userName = request.getHeader("userName");
                if (userName != null && !userName.isEmpty()) {
                    return userName;
                }
            }
        } catch (Exception e) {
            // Ignore exceptions and return default user
        }
        return DEFAULT_USER;
    }

    /**
     * Called before an entity is inserted.
     * Fills createdAt, updatedAt, createdBy, and updatedBy fields.
     *
     * @param entity The entity to be inserted
     */
    public void onInsert(Object entity) {
        if (entity == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String userName = getCurrentUserName();

        try {
            // Try to set createdAt field
            setFieldValue(entity, "createdAt", now);

            // Try to set updatedAt field
            setFieldValue(entity, "updatedAt", now);

            // Try to set createdBy field
            setFieldValue(entity, "createdBy", userName);

            // Try to set updatedBy field
            setFieldValue(entity, "updatedBy", userName);
        } catch (Exception e) {
            // Ignore exceptions
        }
    }

    /**
     * Called before an entity is updated.
     * Fills updatedAt and updatedBy fields.
     *
     * @param entity The entity to be updated
     */
    public void onUpdate(Object entity) {
        if (entity == null) {
            return;
        }

        try {
            // Try to set updatedAt field
            setFieldValue(entity, "updatedAt", LocalDateTime.now());

            // Try to set updatedBy field
            setFieldValue(entity, "updatedBy", getCurrentUserName());
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

    /**
     * Legacy method for backward compatibility.
     * Use onInsert instead.
     *
     * @param entity The entity to fill
     */
    public void fillInsert(Object entity) {
        onInsert(entity);
    }

    /**
     * Legacy method for backward compatibility.
     * Use onUpdate instead.
     *
     * @param entity The entity to fill
     */
    public void fillUpdate(Object entity) {
        onUpdate(entity);
    }

}
