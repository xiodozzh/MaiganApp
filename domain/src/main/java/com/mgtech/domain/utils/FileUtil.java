package com.mgtech.domain.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by zhaixiang on 2017/1/11.
 * file
 */

public class FileUtil {
    public static final String FIRMWARE_FOLDER = File.separator + "MG" + File.separator + "bin";
    public static final String BYTE_TYPE = "ISO-8859-1";

    /**
     * 新建路径
     */
    public static void createFileDirIfNotExist(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File dataDir = new File(path);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
        }
    }

    public static File createIfNotExist(String pathName,String fileName){
        File sdCardDir = Environment.getExternalStorageDirectory();
        String path;
        try {
            path = sdCardDir.getCanonicalPath() + File.separator + pathName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        File dataDir = new File(path);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        return new File(path,fileName);
    }

    public static File saveTempBitmap(Bitmap bitmap){
        File sdCardDir = Environment.getExternalStorageDirectory();
        String path = null;
        try {
            path = sdCardDir.getCanonicalPath() + File.separator + "temp";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        File dataDir = new File(path);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        File file = new File(path,"ecg.png");
        if (file.exists()){
            file.delete();
        }
        try {
            FileOutputStream outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap.recycle();
        return file;
    }

    public static void saveTest(final String fileName,List list){
        StringBuilder builder = new StringBuilder();
        for (Object i :list) {
            builder.append(i.toString());
            builder.append("\n");
        }
        final String content = builder.toString();
        Log.e("test", "saveTest: " + content );
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File sdCardDir = Environment.getExternalStorageDirectory();
                    String path = null;
                    try {
                        path = sdCardDir.getCanonicalPath() + File.separator + "autoSample";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    createFileDirIfNotExist(path);
                    File file = new File(path,fileName);
                    Log.e("test", "saveTest: "+file  );
                    writeToFile(file,content);
                }
            }
        }).start();
    }

    /**
     * 文件是否存在
     *
     * @param dir      路径
     * @param fileName 文件名
     * @return true 存在，false 不存在
     */
    public static boolean isFileExist(String dir, String fileName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = new File(dir, fileName);
            return file.exists();
        }
        return false;
    }

    /**
     * 获取基础路径
     *
     * @return 路径
     * @throws IOException IO异常
     */
    public static String getBaseFileDir() throws IOException {
        File sdCardDir = Environment.getExternalStorageDirectory();
        return sdCardDir.getCanonicalPath();
    }

    /**
     * Writes a file to Disk.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform this operation using another thread.
     *
     * @param file The file to writeWithResponse to Disk.
     */
    public static void writeToFile(File file, String content) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, false);
            writer.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeToFile(File file, String content, String charset) {
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), charset);
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static boolean deleteFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        File file = new File(fileName);
        return file.delete();
    }

    /**
     * Reads a content from a file.
     * This is an I/O operation and this method executes in the main thread, so it is recommended to
     * perform the operation using another thread.
     *
     * @param file The file to read from.
     * @return A string with the content of the file.
     */
    public static String readFileContent(File file) {
        StringBuilder fileContentBuilder = new StringBuilder();
        if (file.exists()) {
            String stringLine;
            FileReader fileReader = null;
            BufferedReader bufferedReader = null;
            try {
                fileReader = new FileReader(file);
                bufferedReader = new BufferedReader(fileReader);
                while ((stringLine = bufferedReader.readLine()) != null) {
                    fileContentBuilder.append(stringLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (fileReader != null) {
                        fileReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContentBuilder.toString();
    }

    /**
     * 读取文件
     *
     * @param file 文件
     * @return String 内容
     */
    public static String readFileContent(File file, String byteSet) {
        StringBuilder sb = new StringBuilder();
        int size = 0;
        if (file.exists()) {
            size = (int) file.length();
            byte[] bytes = new byte[1024];
            InputStream is = null;
            try {
                is = new FileInputStream(file);

                while ((is.read(bytes)) != -1) {
                    sb.append(new String(bytes, byteSet));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb.substring(0, size);
    }

    public static byte[] readFileContentByte(File file) {
        int size = 0;
        byte[] result = null;
        if (file.exists()) {
            size = (int) file.length();
            result = new byte[size];
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                int i = is.read(result);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    /**
     * 获取缓存文件路径
     *
     * @param context    上下文
     * @param uniqueName 名字
     * @return File
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }
}
