package com.mgtech.domain.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author zhaixiang
 * 上传失败保存的缓存数据
 */

public class RawDataFileUtil {
    private static final String CHARSET2 = "GB2312";
    private static final String PATH = "sampleData";
    private static final String SUFFIX = ".txt";
    private static final byte[] KEY = {'m', 'y', 's', 't', 'r', 'a', 'c', 'e', 'g', 'r', 'e', 'c', 'a', 'c', 'h', 'e'};
    private static final byte[] VECTOR = {'C', 'A', 'C', 'H', 'E', 'S', 'T', 'R', 'A', 'C', 'E', 'G', 'R', 'E', 'A', 'T'};

    /**
     * 保存信息，文件名为内容哈希值
     *
     * @param content 内容
     */
    public static void save(Context context,String content) throws Exception {
        File dir = getDiskCacheDir(context);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("缓存路径无法创建");
            }
        }
        byte[] savedContent = AESUtils.encrypt(KEY, content.getBytes(CHARSET2), VECTOR);
        if (savedContent == null) {
            return;
        }
        String name = hashKeyFromContent(content) + SUFFIX;
        File file = new File(dir, name);
        FileUtil.writeToFile(file, byteToString(savedContent));
    }

    /**
     * 是否有未上传的数据
     *
     * @return true 有未上传的数据
     */
    public static boolean haveData(Context context) {
        try {
            File file = getDiskCacheDir(context);
            return file.exists() && file.isDirectory() && file.list().length != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String[] getFileNames(Context context) {
        try {
            File file = getDiskCacheDir(context);
            if (file.exists()) {
                return file.list();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getContent(Context context, String fileName) {
        try {
            File dir = getDiskCacheDir(context);
            File file = new File(dir, fileName);
            if (file.exists()) {
                String content = FileUtil.readFileContent(file);
                byte[] contentBytes = AESUtils.decrypt(KEY, stringToBytes(content), VECTOR);
                if (contentBytes == null) {
                    return "";
                }
                return new String(contentBytes, CHARSET2);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean deleteFile(Context context,String fileName) {
        try {
            File dir = getDiskCacheDir(context);
            File file = new File(dir, fileName);
            if (file.exists()) {
                return file.delete();
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        return new File(cachePath + File.separator + PATH );
    }

    /**
     * url转换
     *
     * @param content 访问信息
     * @return 转换后的字符串
     */
    private static String hashKeyFromContent(String content) {
        String cacheKey;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(content.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            cacheKey = String.valueOf(content.hashCode());
        }
        return cacheKey;
    }

    /**
     * 字节转换为16进制字符
     *
     * @param bytes 需要转换的字节
     * @return 字符
     */
    private static String bytesToHexString(byte[] bytes) {
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

    private static byte[] stringToBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private static String byteToString(byte[] array) {
        return Base64.encodeToString(array, Base64.DEFAULT);
    }
}
