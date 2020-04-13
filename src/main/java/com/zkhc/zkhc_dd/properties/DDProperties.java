package com.zkhc.zkhc_dd.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 钉钉配置
 */
@ConfigurationProperties(prefix = DDProperties.PREFIX)
public class DDProperties {
    public static final String PREFIX = "dd";
    private static final String LOCAL_TEST_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=";
    private String open;
    private String accessToken;
    private String devToken;
    private String testToken;
    private String proToken;
    private String aliMsgToken;
    private String addUrl="http://admin_test.zkhc.kangmochou.com/exceptionLog/addException";
    private String showUrl="http://admin_test.zkhc.kangmochou.com/exceptionLog/exceptionLog_update/";

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = LOCAL_TEST_TOKEN+accessToken;
    }

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = LOCAL_TEST_TOKEN+devToken;
    }

    public String getTestToken() {
        return testToken;
    }

    public void setTestToken(String testToken) {
        this.testToken = LOCAL_TEST_TOKEN+testToken;
    }

    public String getProToken() {
        return proToken;
    }

    public void setProToken(String proToken) {
        this.proToken = LOCAL_TEST_TOKEN+proToken;
    }

    public String getAliMsgToken() {
        return aliMsgToken;
    }

    public void setAliMsgToken(String aliMsgToken) {
        this.aliMsgToken = LOCAL_TEST_TOKEN+aliMsgToken;
    }

    public String getAddUrl() {
        return addUrl;
    }

    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
