package com.techx7.techstore.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfiguration {

    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;
    private final String secure;

    @Autowired
    public CloudinaryConfiguration(
            @Value("${cloudinary.cloud-name}") String cloudName,
            @Value("${cloudinary.api-key}") String apiKey,
            @Value("${cloudinary.api-secret}") String apiSecret,
            @Value("${cloudinary.secure}") String secure) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.secure = secure;
    }

    @Bean
    public Cloudinary createCloudinary() {
        Map<String, String> configuration = new HashMap<>();

        configuration.put("cloud_name", cloudName);
        configuration.put("api_key", apiKey);
        configuration.put("api_secret", apiSecret);
        configuration.put("secure", secure);

        Cloudinary cloudinary = new Cloudinary(configuration);

        return cloudinary;
    }

}
