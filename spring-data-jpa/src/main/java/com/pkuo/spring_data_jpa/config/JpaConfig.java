package com.pkuo.spring_data_jpa.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    // Default user for createdBy and updatedBy fields
    private static final String DEFAULT_USER = "spring-data-jpa-system";

    /**
     * Provides the current auditor (user) for JPA auditing.
     * Gets the username from request headers or returns the default value.
     *
     * @return AuditorAware implementation that returns the current user or default
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            try {
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (attributes != null) {
                    HttpServletRequest request = attributes.getRequest();
                    String userName = request.getHeader("userName");
                    if (userName != null && !userName.isEmpty()) {
                        return Optional.of(userName);
                    }
                }
            } catch (Exception e) {
                // Ignore exceptions and return default user
            }
            return Optional.of(DEFAULT_USER);
        };
    }

}
