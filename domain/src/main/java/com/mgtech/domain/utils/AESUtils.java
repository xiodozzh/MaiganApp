package com.mgtech.domain.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtils {

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

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



    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
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


    public static final String CHARSET_GB2312 = "gb2312";
    public static String encryptString(String string){
        try {
            byte[] result = AESUtils.encrypt(MyConstant.PREFE_KEY, string.getBytes(CHARSET_GB2312), MyConstant.PREFE_VECTOR);
            if (result == null) {
                return "";
            }
            return byteToString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decryptString(String string){
        try {
            if (TextUtils.isEmpty(string)){
                return "";
            }
            byte[] result = AESUtils.decryptWithException(MyConstant.PREFE_KEY, getBytes(string), MyConstant.PREFE_VECTOR);
            return new String(result, CHARSET_GB2312);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static byte[] getBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private static String byteToString(byte[] array) {
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

}
