package com.fine_kuo.mybatisflex_core.config;

import com.fine_kuo.mybatisflex_core.entity.BaseEntity;
import com.mybatisflex.annotation.UpdateListener;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;

public class FineUpdateListener implements UpdateListener {

    private String getCurrentUserName() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            return request.getHeader("userName");
        }
        // 如果沒有請求上下文，則返回預設值
        return "system";
    }


    @Override
    public void onUpdate(Object o) {
        if (o instanceof BaseEntity baseEntity) {
            LocalDateTime now = LocalDateTime.now();
            String userName = getCurrentUserName();

            baseEntity.setUpdatedAt(now);
            baseEntity.setUpdatedBy(userName);
        }
    }

}
