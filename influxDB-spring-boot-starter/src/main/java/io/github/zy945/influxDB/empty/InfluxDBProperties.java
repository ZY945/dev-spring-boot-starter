package io.github.zy945.influxDB.empty;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 伍六七
 * @date 2023/7/1 22:31
 */
@ConfigurationProperties(prefix = "influxdb")//导入后自定义
public class InfluxDBProperties {
    private String url;
    private String token;
    private String bucket;
    private String org;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}
