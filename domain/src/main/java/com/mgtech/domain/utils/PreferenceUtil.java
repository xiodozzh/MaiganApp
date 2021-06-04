package com.mgtech.domain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 *
 * @author zhaixiang
 * @date 2017/1/5
 * preference 工具
 */
//TODO 增加一层，可选择存储方式
public class PreferenceUtil {
    public static final boolean encrypt = true;
    public static final String name = "com.mgtech.www";
    public static final String CHARSET = "ISO-8859-1";
    public static final String CHARSET2 = "gb2312";

    public static void writeStringToPreferences(Context context, String key,
                                                String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (TextUtils.isEmpty(value)){
            editor.putString(key,"");
            editor.apply();
            return;
        }
        if (encrypt) {
            try {
                editor.putString(key, AESUtils.encryptString(value));
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        if (encrypt) {
            try {
                String s = sharedPreferences.getString(key, "");
                if (s.isEmpty()) {
                    return "";
                }
//                byte[] result = AESUtils.decryptWithException(BleConstant.PREFE_KEY, getBytes(s), BleConstant.PREFE_VECTOR);
//                return new String(result, CHARSET2);
                return AESUtils.decryptString(s);
            } catch (Exception e) {
                deletePreferences(context, key);
                e.printStackTrace();
                return "";
            }
        }
        return sharedPreferences.getString(key, "");
    }

    public static boolean safeWriteStringToPreferences(Context context, String key,
                                                       String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (encrypt) {
            try {
                byte[] result = AESUtils.encrypt(MyConstant.PREFE_KEY, value.getBytes(CHARSET2), MyConstant.PREFE_VECTOR);
                if (result == null) {
                    return false;
                }
                editor.putString(key, byteToString(result));
                return editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        editor.putString(key, value);
        return editor.commit();
    }



    public static boolean safeWriteBytesToPreferences(Context context, String key,
                                                      byte[] value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (encrypt) {
            try {
                byte[] result = AESUtils.encrypt(MyConstant.PREFE_KEY, value, MyConstant.PREFE_VECTOR);
                if (result == null) {
                    return false;
                }
                editor.putString(key, byteToString(result));
                return editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        try {
            editor.putString(key, byteToString(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editor.commit();
    }

    private static byte[] getBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private static String byteToString(byte[] array) {
        return Base64.encodeToString(array, Base64.DEFAULT);
    }

    public static byte[] getBytesFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        if (encrypt) {
            try {
                String s = sharedPreferences.getString(key, "");
                if (s.isEmpty()) {
                    return null;
                }
                return AESUtils.decrypt(MyConstant.PREFE_KEY, getBytes(s), MyConstant.PREFE_VECTOR);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            return getBytes(sharedPreferences.getString(key, ""));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean getBooleanFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static boolean getBooleanFromPreferences(Context context, String key,boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void writeBooleanToPreferences(Context context, String key,
                                                 boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean safeWriteBooleanToPreferences(Context context, String key,
                                                        boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }


    public static void writeIntToPreferences(Context context, String key,
                                             int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static boolean safeWriteIntToPreferences(Context context, String key,
                                                    int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static void writeFloatToPreferences(Context context, String key,
                                               float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void writeLongToPreferences(Context context, String key,
                                              long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long getLongToPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, 0);
    }

    public static int getIntFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static int getIntFromPreferences(Context context, String key,int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static float getFloatFromPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0);
    }

    public static float getFloatFromPreferences(Context context, String key,float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void deletePreferences(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

    public static int[] getIntArrayPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        String array = sharedPreferences.getString(key, "");
        if (array.isEmpty()) {
            return new int[0];
        } else {
            String[] split = array.split(",");
            int[] result = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                try {
                    result[i] = Integer.parseInt(split[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new int[0];
                }
            }
            return result;
        }
    }

    public static void writeIntArrayToPreferences(Context context, String key, int[] value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        StringBuilder builder = new StringBuilder();
        for (int i : value) {
            builder.append(i);
            builder.append(",");
        }
        if (builder.length() != 0) {
            builder.delete(builder.length() - 1, builder.length());
        }
        editor.putString(key, builder.toString());
        editor.apply();
    }
}
