package com.example.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SensitiveDataConfig {

    private List<String> sensitiveKeys;

    public SensitiveDataConfig() {
        // Initialize the list of sensitive keys
        sensitiveKeys = new ArrayList<>(Arrays.asList(
                "roc_id",
                "account_number",
                "credit_card_number"
                // Add more sensitive keys as needed
        ));
    }

    public List<String> getSensitiveKeys() {
        return sensitiveKeys;
    }
}
