package com.mgtech.domain.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhaixiang on 2017/5/2.
 * 控制log文件
 */

public class LogUtil {
    private static final String NAME = "MG_LOG.txt";

    public static boolean saveToLogFile(String line){
        FileWriter fileWriter = null;
        boolean result = true;
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();
            String path = sdCardDir.getCanonicalPath();
            createFileDirIfNotExist(path);
            File file = new File(path, NAME);
            fileWriter = new FileWriter(file, true);
            fileWriter.append("\n"+line);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    result = false;
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 新建路径
     */
    private static void createFileDirIfNotExist(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File dataDir = new File(path);
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
        }
    }
}
