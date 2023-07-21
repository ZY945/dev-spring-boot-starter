package io.github.zy945.util.encipherUtil.nonreversible;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * @author 伍六七
 * @date 2023/6/20 9:10
 * 不可逆:无法通过加密后获取加密前，但是可以撞库
 * md5、HMAC、SHA1、SHA-244、SHA-256、、
 */
public class NonReversible {
    private static final String MD5_ALGORITHM = "MD5";
    private static final String SHA256_ALGORITHM = "SHA-256";

    /**
     * 散列值为16个，128位
     *
     * @param data 待加密数据
     * @return 加密后数据
     */
    public static String encryptByMD5(String data) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(MD5_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] digest = messageDigest.digest(data.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : digest) {//[-31, 10, -36, 57, 73, -70, 89, -85, -66, 86, -32, 87, -14, 15, -120, 62]
            //补零                e1  0a  dc
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    /**
     * 散列值为32个，256位
     *
     * @param data 待加密的数据
     * @return 加密后数据
     */
    public static String encryptBySHA256(String data) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(SHA256_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        //获取散列值
        byte[] digest = messageDigest.digest(data.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : digest) {
            builder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
        }
        return builder.toString();
    }
}
