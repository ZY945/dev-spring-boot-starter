package io.github.zy945.dingtalk.config;

import com.dingtalk.api.DingTalkClient;
import com.taobao.api.DefaultTaobaoClient;
import io.github.zy945.dingtalk.empty.DingDingConnect;
import io.github.zy945.dingtalk.util.DingTalkHelper;
import io.github.zy945.dingtalk.util.DingTalkManager;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author 伍六七
 * @date 2023/7/1 22:34
 */
@Configuration
@EnableConfigurationProperties(DingDingConnect.class)
public class DingTalkAutoConfiguration {
    @Autowired
    private DingDingConnect connect;

    @Bean(name = "DingTalkClient")
    @ConditionalOnMissingBean
    public DingTalkClient getDingTalkClient() {
        return DingTalkManager.connectByToken(connect);
    }

    @Bean(name = "DingTalkProxyClient")
    @ConditionalOnMissingBean
    public DefaultTaobaoClient getDingTalkProxyClient() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        return DingTalkManager.proxy(connect);
    }

    @Bean(name = "DingTalkHelper")
    @ConditionalOnMissingBean
    public DingTalkHelper getDingTalkHelper() {
        return DingTalkHelper.getSingleton();
    }

}
