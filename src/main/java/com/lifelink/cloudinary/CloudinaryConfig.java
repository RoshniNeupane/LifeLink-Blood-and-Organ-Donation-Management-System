package com.lifelink.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "doxoh01nj",
            "api_key", "689224562416124",
            "api_secret", "UvYYeAcTFm3hGzwjNLG21lj0dJ8",
            "secure", true
        ));
    }
}
 