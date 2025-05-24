package com.finekuo.normalcore;

import com.finekuo.normalcore.dto.example.UserDetails;
import com.finekuo.normalcore.util.Gsons;
import com.finekuo.normalcore.util.Jsons;

public class DemoMain {

    public static void main(String[] args) {
        UserDetails userDetails = new UserDetails(
                "testuser",
                "P@$$wOrd",
                "test@example.com",
                "1234567890"
        );

        System.out.println("Jackson Serialization:");
        String jacksonJson = Jsons.toJson(userDetails);
        System.out.println(jacksonJson);

        System.out.println("\nGson Serialization:");
        String gsonJson = Gsons.toJson(userDetails);
        System.out.println(gsonJson);
    }
}
