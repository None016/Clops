package com.example.Clops.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "your-secret-key-at-least-256-bits-long-for-security";
    private long expiration = 86400000; // 24 часа в миллисекундах
    private String issuer = "user-service";
}
