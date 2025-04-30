package com.finekuo.mybatisflexcore.config;

import com.finekuo.mybatisflexcore.entity.BaseEntity;
import com.mybatisflex.annotation.InsertListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

@Slf4j
public class FineInsertListener implements InsertListener {

    private String getCurrentUserName() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String system = "system";
        if (requestAttributes != null) {
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            return StringUtils.defaultIfBlank(request.getHeader("userName"), system);
        }
        // 如果沒有請求上下文，則返回預設值
        return system;
    }

    @Override
    public void onInsert(Object o) {
        if (o instanceof BaseEntity baseEntity) {
            LocalDateTime now = LocalDateTime.now();
            String userName = getCurrentUserName();

            baseEntity.setCreatedAt(now);
            baseEntity.setCreatedBy(userName);
            baseEntity.setUpdatedAt(now);
            baseEntity.setUpdatedBy(userName);
        }
    }


}
