package com.finekuo.mybatisflexcore.config;

import com.finekuo.mybatisflexcore.entity.BaseEntity;
import com.mybatisflex.core.FlexGlobalConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.finekuo.mybatisflexcore.mapper")
public class MybatisFlexConfig {

    public MybatisFlexConfig() {
        FlexGlobalConfig config = FlexGlobalConfig.getDefaultConfig();
        config.registerInsertListener(new FineInsertListener(), BaseEntity.class);
        config.registerUpdateListener(new FineUpdateListener(), BaseEntity.class);
    }

}
