package com.jkos.mybatisflex.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jkos.mybatisflex.mapper")
public class MybatisFlexConfig {

    @Bean
    public AutoFillHandler dateTimeFillHandler() {
        return new AutoFillHandler();
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer(AutoFillHandler autoFillHandler) {
        return configuration -> {
            // Register the AutoFillHandler as a global listener
            FlexGlobalConfig globalConfig = FlexGlobalConfig.getDefaultConfig();

            // Register insert listener
            globalConfig.registerInsertListener(entity -> autoFillHandler.onInsert(entity));

            // Register update listener
            globalConfig.registerUpdateListener(entity -> autoFillHandler.onUpdate(entity));
        };
    }

}
