package io.github.zy945.dingtalk.util;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.taobao.api.DefaultTaobaoClient;
import io.github.zy945.dingtalk.empty.DingDingConnect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author 伍六七
 * @date 2023/7/9 12:40
 */
@Slf4j
public class DingTalkManager {

    public static DingTalkClient textClient;
    public static DefaultTaobaoClient proxyClient;

    public static DingTalkClient connectByToken(DingDingConnect connect) {

        try {
            textClient = new DefaultDingTalkClient(connect.getAccesstoken() + sign(connect.getSecret()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return textClient;
    }

    public static String sign(String secret) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Long timestamp = System.currentTimeMillis();

        String stringToSign = timestamp + "\n" + secret;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes(StandardCharsets.UTF_8));
        String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), StandardCharsets.UTF_8);
        return "&timestamp=" + timestamp + "&sign=" + sign;
    }


    public static DefaultTaobaoClient proxy(DingDingConnect connect) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        if (connect.getProxyhost() == null || connect.getProxyport() == null) {
            log.warn("代理未设置");
            return null;
        }
        String url = connect.getAccesstoken() + sign(connect.getSecret());
        proxyClient = new DefaultTaobaoClient(url, connect.getAccesstoken(), connect.getSecret());
        SocketAddress sa = new InetSocketAddress(connect.getProxyhost(), connect.getProxyport());
        proxyClient.setProxy(new Proxy(Proxy.Type.HTTP, sa));
        return proxyClient;
    }
}
