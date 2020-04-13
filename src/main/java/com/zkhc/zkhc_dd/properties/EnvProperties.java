package com.zkhc.zkhc_dd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = EnvProperties.PREFIX)
public class EnvProperties {
    public static final String PREFIX = "spring.profiles";

    private String active;
}
