package com.mgtech.blelib.biz;

import java.util.Random;

public class RandomCodeGenerator {
    private static volatile RandomCodeGenerator generator;
    private byte code;
    private Random random;

    private RandomCodeGenerator(){
        random = new Random();
    }

    public static RandomCodeGenerator getInstance(){
        if (generator == null){
            synchronized (RandomCodeGenerator.class){
                if (generator == null){
                    generator = new RandomCodeGenerator();
                }
            }
        }
        return generator;
    }

    public byte generate(){
        code = (byte) random.nextInt(0x100);
        return code;
    }

    public byte get(){
        return code;
    }

}
