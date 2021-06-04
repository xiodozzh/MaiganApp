package com.mgtech.domain.utils;


import android.content.Context;
import android.os.Environment;

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
 * Created by zhaixiang on 2017/5/4.
 * 缓存
 */

public class CacheUtil {
    private static final String cacheFileName = "sampleData";
    private static final String TAG = "cache";
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 20;
    private static final int cacheVersion = 1;
    private static final byte[] KEY = {'m', 'y', 's', 't', 'r', 'a', 'c', 'e', 'g', 'r', 'e', 'c', 'a', 'c', 'h', 'e'};
    private static final byte[] VECTOR = {'C', 'A', 'C', 'H', 'E', 'S', 'T', 'R', 'A', 'C', 'E', 'G', 'R', 'E', 'A', 'T'};
    private static final String CHARSET = "ISO-8859-1";
    private static final String CHARSET2 = "GB2312";
    private static CacheUtil cacheUtil;

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

    public synchronized void save(Context context, String id, String content) throws IOException {
        if (content == null){
            return;
        }
        File root  = getDiskCacheDir(context, cacheFileName);
        File dir = new File(root, hashKeyFromUrl(id));
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("缓存路径无法创建");
            }
        }
        DiskLruCache diskLruCache = DiskLruCache.open(dir, cacheVersion, 1, DISK_CACHE_SIZE);
        String key = hashKeyFromUrl(id + content);
        DiskLruCache.Editor editor = diskLruCache.edit(key);
        OutputStream os = editor.newOutputStream(0);
        BufferedWriter bw = null;
        try {
            byte[] savedContent = encrypt(KEY, content.getBytes(CHARSET2), VECTOR);
            if (savedContent == null) {
                return;
            }
            bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(new String(savedContent, CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
        editor.commit();
    }

    public synchronized String get(Context context,String id, String fileName) throws IOException {
        File root  = getDiskCacheDir(context, cacheFileName);
        File dir = null;
        if (root != null) {
            dir = new File(root, hashKeyFromUrl(id));
        }
        if (dir == null || !dir.exists()) {
            return "";
        }
        String key = hashKeyFromUrl(id + fileName);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, cacheVersion, 1, DISK_CACHE_SIZE);
        DiskLruCache.Snapshot snap = diskLruCache.get(key);
        if (snap == null) {
            return "";
        }
        try {
            byte[] data = snap.getString(0).getBytes(CHARSET);

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
        File dir = getDiskCacheDir(context, cacheFileName);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, cacheVersion, 1, DISK_CACHE_SIZE);
        String key = hashKeyFromUrl(url);
        return diskLruCache.remove(key);
    }

    public void deleteAll(Context context) throws IOException {
        File dir = getDiskCacheDir(context, cacheFileName);
        DiskLruCache diskLruCache = DiskLruCache.open(dir, cacheVersion, 1, DISK_CACHE_SIZE);
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
     * @param uniqueName 名字
     * @return File
     */
    private static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static String getCachePath(Context context){
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
