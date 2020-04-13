package com.zkhc.zkhc_dd.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@Data
@ConfigurationProperties(prefix = PortProperties.PREFIX)
public class PortProperties {
    public static final String PREFIX = "server";

    private  String port;
}
