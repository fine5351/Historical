package com.finekuo.springdatajpa.service;

import com.google.gson.JsonObject;

public interface EntityMaskService {

    /**
     * 根据账户、请求方法和 API URL 对 JSON 对象应用掩码
     *
     * @param account    账户
     * @param method     请求方法
     * @param apiUrl     API URL
     * @param jsonObject 要处理的 JSON 对象
     * @return 处理后的 JSON 对象
     */
    JsonObject mask(String account, String method, String apiUrl, JsonObject jsonObject);

}
