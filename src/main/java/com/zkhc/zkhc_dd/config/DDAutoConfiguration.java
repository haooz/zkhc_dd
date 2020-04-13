package com.zkhc.zkhc_dd.config;

import com.zkhc.zkhc_dd.properties.*;
import com.zkhc.zkhc_dd.entity.DD;
import com.zkhc.zkhc_dd.service.DDProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 钉钉配置
 */
@Configuration
@EnableConfigurationProperties({DDProperties.class, EnvProperties.class, NameProperties.class, PortProperties.class, PublishProperties.class
})
@ConditionalOnClass(DDProvider.class)
@ConditionalOnProperty
        (
                prefix = "dd",
                value = "enabled",
                matchIfMissing = true
        )
public class DDAutoConfiguration {
    @Autowired
    private DDProperties ddProperties;
    @Autowired
    private EnvProperties envProperties;
    @Autowired
    private NameProperties nameProperties;
    @Autowired
    private PortProperties portProperties;
    @Autowired
    private PublishProperties publishProperties;

    @Bean
    @ConditionalOnMissingBean(DD.class)
    public DD dd(){
        DD dd=new DD();
        dd.setAccessToken(ddProperties.getAccessToken());
        dd.setAddUrl(ddProperties.getAddUrl());
        dd.setAliMsgToken(ddProperties.getAliMsgToken());
        dd.setDevToken(ddProperties.getDevToken());
        dd.setOpen(ddProperties.getOpen());
        dd.setProToken(ddProperties.getProToken());
        dd.setShowUrl(ddProperties.getShowUrl());
        dd.setTestToken(ddProperties.getTestToken());
        dd.setActive(envProperties.getActive());
        dd.setName(nameProperties.getName());
        dd.setPort(portProperties.getPort());
        dd.setPublishInfo(publishProperties.getPublishInfo());
        return dd;
    }

    @Bean
    public DDProvider ddProvider(){
        return new DDProvider();
    }

}