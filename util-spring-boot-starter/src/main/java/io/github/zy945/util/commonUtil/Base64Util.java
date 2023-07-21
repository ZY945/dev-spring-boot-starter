package io.github.zy945.util.commonUtil;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author 伍六七
 * @date 2022/12/21 11:18
 * 默认字符串都是utf8
 */
public class Base64Util {
    /**
     * 加密
     *
     * @param code
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encoderGetStrByByte(String code) throws UnsupportedEncodingException {
        byte[] bytes = code.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String encoderGetStrByByte(byte[] bytes) throws UnsupportedEncodingException {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decoderGetStrByStr(String code) {
        byte[] decode = Base64.getDecoder().decode(code);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public static String decoderGetStrByByte(byte[] decode) {
        byte[] bytes = Base64.getDecoder().decode(decode);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] decoderGetByteByStr(String code) {
        return Base64.getDecoder().decode(code);
    }

    public static byte[] decoderGetByteByByte(byte[] decode) {
        return Base64.getDecoder().decode(decode);
    }
}
