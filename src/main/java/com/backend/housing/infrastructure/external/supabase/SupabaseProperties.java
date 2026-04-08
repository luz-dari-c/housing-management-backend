package com.backend.housing.infrastructure.external.supabase;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "supabase")
public class SupabaseProperties {

    private String url;
    private String key;
    private String bucket;


}