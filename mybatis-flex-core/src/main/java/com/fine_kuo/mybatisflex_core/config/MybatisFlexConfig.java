package com.fine_kuo.mybatisflex_core.config;

import com.fine_kuo.mybatisflex_core.entity.BaseEntity;
import com.mybatisflex.core.FlexGlobalConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisFlexConfig {

    public MybatisFlexConfig() {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new FineInsertListener(), BaseEntity.class);
        config.registerUpdateListener(new FineUpdateListener(), BaseEntity.class);
    }

}
