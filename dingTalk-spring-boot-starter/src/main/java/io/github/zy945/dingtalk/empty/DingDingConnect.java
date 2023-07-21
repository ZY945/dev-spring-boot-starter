package io.github.zy945.dingtalk.empty;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 伍六七
 * @date 2023/6/22 22:38
 * 2023-7-20starter的connect一直是null,可能是在get方法时还没有set
 */
@ConfigurationProperties(prefix = "dingding")
public class DingDingConnect {
    private String accesstoken;
    private String secret;
    private String proxyhost;
    private Integer proxyport;

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getProxyhost() {
        return proxyhost;
    }

    public void setProxyhost(String proxyhost) {
        this.proxyhost = proxyhost;
    }

    public Integer getProxyport() {
        return proxyport;
    }

    public void setProxyport(Integer proxyport) {
        this.proxyport = proxyport;
    }
}
