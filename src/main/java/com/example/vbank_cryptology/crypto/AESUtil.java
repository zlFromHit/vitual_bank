package com.example.vbank_cryptology.crypto;

import org.apache.tomcat.util.codec.binary.Base64;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;


public class AESUtil {
    //密钥
    public final static String SECRET_KEY="uUXsN6okXYqsh0BB";

    /**
     * 加密方法，采用AES_128位
     * @param plainText
     * @param SECRET_KEY
     * @return
     */
    public static String encrypt(String plainText, String SECRET_KEY) {
        if (SECRET_KEY == null) {
            throw new IllegalArgumentException("sSrc不能为空");
        }
        // 判断Key是否为16位
        if (SECRET_KEY.length() != 16) {
            throw new IllegalArgumentException("sKey长度需要为16位");
        }

        try {
            byte[] raw = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretkeySpec = new SecretKeySpec(raw, "AES");

            //"算法/模式/补码方式"
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretkeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密方法 对AES_128位解密
     * @param cipherText
     * @return
     */
    public static String decrypt(String cipherText,String SECRET_KEY) {
        try {
            byte[] raw = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretkeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretkeySpec);
            //先用base64解密
            byte[] encrypted1 = new Base64().decode(cipherText);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 验证消息完整性  采用SHA-1哈希消息值，进行比对
     * @param SHA1Cipher
     * @param cipherText
     * @return
     */
    public static boolean MACVerify(String cipherText,String SHA1Cipher) throws NoSuchAlgorithmException {
        return SHA1Cipher.equals(Hashcode.sha1(cipherText));
    }
}
