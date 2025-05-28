package com.finekuo.nats.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Includes @Getter, @Setter, @ToString, @EqualsAndHashCode, and a constructor for all final fields (or all fields if none are final)
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all arguments
public class NatsPublishRequest {
    private String subject;
    private String message;
}
