package com.mgtech.data.net.http;


import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author zhaixiang
 * 缓存
 */

public class CacheUtil {
    private static final String CACHE_FILE_NAME = "cachedData";
    private static final String TAG = "cache";
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 100;
    private static final int CACHE_VERSION = 1;
    private static final byte[] KEY = {'M', 'Y', 'S', 'T', 'R', 'A', 'C', 'E', 'G', 'R', 'E', 'C', 'A', 'C', 'H', 'E'};
    private static final byte[] VECTOR = {'c', 'a', 'c', 'h', 'e', 's', 't', 'r', 'a', 'c', 'e', 'g', 'r', 'e', 'a', 't'};
    private static final String CHARSET = "ISO-8859-1";
    private static final String CHARSET2 = "GB2312";

    private static volatile CacheUtil cacheUtil;

    public static CacheUtil getInstance(){
        if (cacheUtil == null){
            synchronized (CacheUtil.class){
                if (cacheUtil == null){
                    cacheUtil = new CacheUtil();
                }
            }
        }
        return cacheUtil;
    }

    public synchronized void save(Context context, String url,@NonNull String content) throws IOException {
        File dir = getDiskCacheDir(context);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("缓存路径无法创建");
            }
        }
        Log.e(TAG, "save: " + content);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, CACHE_VERSION, 1, DISK_CACHE_SIZE);
        String key = hashKeyFromUrl(url);
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        OutputStream os = editor.newOutputStream(0);
        BufferedWriter bw = null;
        try {
            byte[] savedContent = encrypt(KEY, content.getBytes(CHARSET2), VECTOR);
            if (savedContent == null) {
                return;
            }
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(byteToString(savedContent));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
        editor.commit();
    }

    public synchronized String get(Context context, String url) throws IOException {
        File dir = getDiskCacheDir(context);
        if (!dir.exists()) {
            return "";
        }
        String key = hashKeyFromUrl(url);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, CACHE_VERSION, 1, DISK_CACHE_SIZE);
        DiskLruCache.Snapshot snap = diskLruCache.get(key);
        if (snap == null) {
            return "";
        }
        try {
            byte[] data = stringToBytes(snap.getString(0));
            byte[] content = decrypt(KEY, data, VECTOR);
            if (content != null) {
                return new String(content, CHARSET2);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean delete(Context context, String url) throws IOException {
        File dir = getDiskCacheDir(context);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, CACHE_VERSION, 1, DISK_CACHE_SIZE);
        String key = hashKeyFromUrl(url);
        return diskLruCache.remove(key);
    }

    public void deleteAll(Context context) throws IOException {
        File dir = getDiskCacheDir(context);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, CACHE_VERSION, 1, DISK_CACHE_SIZE);
        diskLruCache.delete();
    }

    /**
     * url转换
     *
     * @param url 访问信息
     * @return 转换后的字符串
     */
    private String hashKeyFromUrl(String url) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(url.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节转换为16进制字符
     *
     * @param bytes 需要转换的字节
     * @return 字符
     */
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
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
        if (key.length == 0) {
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
     * 获取缓存文件路径
     *
     * @param context    上下文
     * @return File
     */
    private static File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + CACHE_FILE_NAME);
    }

    private static byte[] stringToBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private static String byteToString(byte[] array) {
        return Base64.encodeToString(array, Base64.DEFAULT);
    }
}
