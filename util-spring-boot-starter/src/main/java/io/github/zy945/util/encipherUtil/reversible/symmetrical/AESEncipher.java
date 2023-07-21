package io.github.zy945.util.encipherUtil.reversible.symmetrical;


import io.github.zy945.util.commonUtil.Base64Util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * @author 伍六七
 * @date 2023/6/20 9:07<br/>
 * 加密算法:AES<br/>
 * 加密模式<br/>
 * ECB----将明文块分为多个固定大小的数据块,每个数据块独立加密,互不影响,所以加密快,但是同一文本加密结果一致,因此加密性一般,同时缺乏关联性,没法保证完整<br/>
 * 总结:速度快,加密性一般,无法保证完整性<br/>
 * CBC----在加密过程中引入前一个密文块与当前明文块的异或运算,还可以通过使用初始化向量（IV）随机化第一个数据块的加密结果,关联性极强,因此无法并行处理,加密时间长<br/>
 * 总结:速度慢,加密性强,能保证完整性<br/>
 * 例如 CFB、OFB 和 CTR<br/>
 */
public class AESEncipher {

    private static final int KEY_SIZE = 128;
    private static final String AES_ALGORITHM = "AES";

    private static final String ECB_TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String CBC_TRANSFORMATION = "AES/CBC/PKCS5Padding";


    /**
     * 获得AES的公私钥
     * 添加伪随机数,来使每次公私钥都不一样
     *
     * @return 钥匙对
     */
    public static Key generateKeyPair() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance(AES_ALGORITHM);

            //生成安全的密钥和 IV（Initialization Vector,初始化向量）
            SecureRandom secureRandom = new SecureRandom();

            generator.init(KEY_SIZE, secureRandom);
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static IvParameterSpec initVector() {
        // 随机生成 16 字节的初始化向量
        byte[] ivBytes = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(ivBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new IvParameterSpec(ivBytes);
    }


    //////////////////////////////////////////ECB//////////////////////////////////////////


    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  钥匙对
     * @return 加密后数据(rsa加密后的byte[]同时被base64加密)
     */
    public static String encryptedByECB(String data, Key key) {
        try {
            Cipher instance = Cipher.getInstance(ECB_TRANSFORMATION);
            instance.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = instance.doFinal(data.getBytes());
            return Base64Util.encoderGetStrByByte(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  钥匙对
     * @return 解密后数据
     */
    public static String decryptedByECB(String data, Key key) {
        try {
            Cipher instance = Cipher.getInstance(ECB_TRANSFORMATION);
            instance.init(Cipher.DECRYPT_MODE, key);
            byte[] dataByte = Base64Util.decoderGetByteByStr(data);
            byte[] bytes = instance.doFinal(dataByte);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    //////////////////////////////////////////CBC//////////////////////////////////////////

    /**
     * 公钥加密
     *
     * @param data 待加密数据
     * @param key  钥匙对
     * @return 加密后数据(rsa加密后的byte[]同时被base64加密)
     */
    public static String encryptedByCBC(String data, Key key, IvParameterSpec iv) {
        try {
            Cipher instance = Cipher.getInstance(CBC_TRANSFORMATION);
            instance.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] bytes = instance.doFinal(data.getBytes());
            return Base64Util.encoderGetStrByByte(bytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 私钥解密
     *
     * @param data 待解密数据
     * @param key  钥匙对
     * @return 解密后数据
     */
    public static String decryptedByCBC(String data, Key key, IvParameterSpec iv) {
        try {
            Cipher instance = Cipher.getInstance(CBC_TRANSFORMATION);
            instance.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] dataByte = Base64Util.decoderGetByteByStr(data);
            byte[] bytes = instance.doFinal(dataByte);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }
}
