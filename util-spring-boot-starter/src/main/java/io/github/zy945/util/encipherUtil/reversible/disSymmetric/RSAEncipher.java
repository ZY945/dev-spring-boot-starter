package io.github.zy945.util.encipherUtil.reversible.disSymmetric;


import io.github.zy945.util.commonUtil.Base64Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * @author 伍六七
 * @date 2023/6/20 9:07
 * 非对称加密---适合小量数据
 * 大量数据建议对称加密
 */
public class RSAEncipher {

    private static final int KEY_SIZE = 2048;
    private static final String RSA_ALGORITHM = "RSA";


    /**
     * 获得RSA的公私钥
     * 添加伪随机数,来使每次公私钥都不一样
     *
     * @return 钥匙对
     */
    public static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA_ALGORITHM);

            //生成安全的密钥和 IV（Initialization Vector，初始化向量）
            SecureRandom secureRandom = new SecureRandom();

            generator.initialize(KEY_SIZE, secureRandom);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //////////////////////////////////////////RSA//////////////////////////////////////////

    /**
     * 公钥加密
     *
     * @param data    待加密数据
     * @param keyPair 钥匙对
     * @return 加密后数据(rsa加密后的byte[]同时被base64加密)
     */
    public static String encryptedByRSA(String data, KeyPair keyPair) {
        try {
            Cipher instance = Cipher.getInstance(RSA_ALGORITHM);
            instance.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] bytes = instance.doFinal(data.getBytes(StandardCharsets.UTF_8));
            //加密后的数据可能包含控制字符、二进制数据和非 ASCII 码字符等，直接转字符串会乱码
            //可以使用Base64编辑解码
            //return new String(bytes, StandardCharsets.UTF_8);
            return Base64Util.encoderGetStrByByte(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 私钥解密
     *
     * @param data    待解密数据
     * @param keyPair 钥匙对
     * @return 解密后数据
     */
    public static String decryptedByRSA(String data, KeyPair keyPair) {
        try {
            Cipher instance = Cipher.getInstance(RSA_ALGORITHM);
            instance.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());

            //因为SHA加密后因为其他字符被Base64加密了,
            //需要先解密Base64
            byte[] dataByte = Base64Util.decoderGetByteByStr(data);
            byte[] bytes = instance.doFinal(dataByte);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
