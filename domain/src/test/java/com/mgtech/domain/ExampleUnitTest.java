package com.mgtech.domain;

import android.util.Log;

import com.mgtech.domain.utils.AESUtils;
import com.mgtech.domain.utils.FileUtil;
import com.mgtech.domain.utils.MyConstant;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        byte[] iv = new byte[16];
        byte[] key = new byte[16];
        byte[] txt = "123".getBytes(FileUtil.BYTE_TYPE);
        byte[] s = AESUtils.encrypt(key,txt,iv);

        System.out.println(new String(key,FileUtil.BYTE_TYPE));
        System.out.println(new String(txt,FileUtil.BYTE_TYPE));
        System.out.println(new String(iv,FileUtil.BYTE_TYPE));
        System.out.println(new String(s,FileUtil.BYTE_TYPE));
        System.out.println(Arrays.toString(s));
    }
}