package com.mgtech.blelib.entity;


import com.mgtech.blelib.biz.RandomCodeGenerator;

public class PairRequestResponse {
    private byte[] encryptKey;
    private byte[] encryptVector;
    private byte[] encryptSignature;
    private byte[] id;
    private boolean isError;

    public PairRequestResponse(byte[] data){
        if (data.length != 54 && data[1] == RandomCodeGenerator.getInstance().get()){
            isError = true;
            return;
        }
        encryptKey = new byte[16];
        encryptVector = new byte[16];
        encryptSignature = new byte[4];
        id = new byte[16];

        encryptKey[0] = data[2];
        encryptVector[1] = data[3];
        id[2] = data[4];
        encryptKey[1] = data[5];
        encryptVector[2] = data[6];
        id[3] = data[7];
        encryptKey[2] = data[8];
        encryptVector[3] = data[9];
        id[4] = data[10];
        encryptKey[3] = data[11];
        encryptVector[4] = data[12];
        id[5] = data[13];
        encryptSignature[0] = data[14];
        encryptKey[4] = data[15];
        encryptVector[5] = data[16];
        id[6] = data[17];
        encryptKey[5] = data[18];
        encryptVector[6] = data[19];
        id[7] = data[20];
        encryptKey[6] = data[21];
        encryptVector[7] = data[22];
        id[8] = data[23];
        encryptKey[7] = data[24];
        encryptVector[8] = data[25];
        id[9] = data[26];
        encryptSignature[1] = data[27];
        encryptKey[8] = data[28];
        encryptVector[9] = data[29];
        id[10] = data[30];
        encryptKey[9] = data[31];
        encryptVector[10] = data[32];
        id[11] = data[33];
        encryptKey[10] = data[34];
        encryptVector[11] = data[35];
        id[12] = data[36];
        encryptKey[11] = data[37];
        encryptVector[12] = data[38];
        id[13] = data[39];
        encryptSignature[2] = data[40];
        encryptKey[12] = data[41];
        encryptVector[13] = data[42];
        id[14] = data[43];
        encryptKey[13] = data[44];
        encryptVector[14] = data[45];
        id[15] = data[46];
        encryptKey[14] = data[47];
        encryptVector[15] = data[48];
        id[0] = data[49];
        encryptKey[15] = data[50];
        encryptVector[0] = data[51];
        id[1] = data[52];
        encryptSignature[3] = data[53];
    }

    public byte[] getEncryptKey() {
        return encryptKey;
    }

    public byte[] getEncryptVector() {
        return encryptVector;
    }

    public byte[] getEncryptSignature() {
        return encryptSignature;
    }

    public byte[] getId() {
        return id;
    }

    public boolean isError() {
        return isError;
    }
}
