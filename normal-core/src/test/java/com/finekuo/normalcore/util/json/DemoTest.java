package com.finekuo.normalcore.util.json;

import com.finekuo.normalcore.BaseTest;
import com.finekuo.normalcore.dto.example.UserDetails;
import com.finekuo.normalcore.util.Gsons;
import com.finekuo.normalcore.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DemoTest extends BaseTest {

    @Test
    void test() {
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
