package com.finekuo.normalcore.config;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class SensitiveDataConfig {

    private final List<String> sensitiveKeys;

    public SensitiveDataConfig() {
        // Initialize the list of sensitive keys
        sensitiveKeys = new ArrayList<>(Arrays.asList(
                "roc_id",
                "rocId",
                "account_number",
                "credit_card_number"
                // Add more sensitive keys as needed
        ));
    }

}
