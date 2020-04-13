package com.zkhc.zkhc_dd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = NameProperties.PREFIX)
public class NameProperties {
    public static final String PREFIX = "spring.application";

    private String name;
}
