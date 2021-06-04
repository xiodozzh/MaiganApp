package com.mgtech.blelib.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtils {

    /**
     * 加密
     *
     * @param key       密钥
     * @param plainText 明文
     * @param iv        初始向量
     * @return 密文
     */
    public static byte[] encrypt(byte[] key, byte[] plainText, byte[] iv) {
        if (key == null || key.length == 0) {
            return null;
        }
        byte[] encrypted = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            encrypted = cipher.doFinal(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }


    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 密文
     * @param iv        初始向量
     * @return 明文
     */
    public static byte[] decrypt(byte[] key, byte[] encrypted, byte[] iv) {
        byte[] plainText = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
            plainText = cipher.doFinal(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }

    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 密文
     * @param iv        初始向量
     * @return 明文
     */
    public static byte[] decryptWithException(byte[] key, byte[] encrypted, byte[] iv) throws Exception {
        byte[] plainText;
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        plainText = cipher.doFinal(encrypted);
        return plainText;
    }


    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        gen.init(128, sr);
        SecretKey sKey = gen.generateKey();
        return sKey.getEncoded();
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
