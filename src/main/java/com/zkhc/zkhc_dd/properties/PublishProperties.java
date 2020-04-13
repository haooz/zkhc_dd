package com.zkhc.zkhc_dd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = PublishProperties.PREFIX)
public class PublishProperties {
    public static final String PREFIX = "spring";

    private  String publishInfo;
}
