package com.jkos.by_shardingsphere_jdbc.config;

import com.mybatisflex.core.FlexGlobalConfig;
import com.mybatisflex.spring.boot.ConfigurationCustomizer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.jkos.by_shardingsphere_jdbc.mapper")
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
            globalConfig.registerInsertListener(autoFillHandler::onInsert);

            // Register update listener
            globalConfig.registerUpdateListener(autoFillHandler::onUpdate);
        };
    }

}
