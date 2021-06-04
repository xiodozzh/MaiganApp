//
// Created by zhaixiang on 2017/8/21.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include "ecg/ecg_types.h"
#include "ecg/ecg_emxAPI.h"
#include "ecg/filter_ecg.h"
#include "ecg/detect_ecg.h"




extern "C"
{
JNIEXPORT jdoubleArray JNICALL
Java_com_mgtech_blelib_ecg_EcgUtil_filter(JNIEnv *env, jclass type, jdoubleArray ecg_data, jint ecgSize,
                                      jdouble sampleRate) {
    jdouble *ecgData = env->GetDoubleArrayElements(ecg_data, NULL);
//        __android_log_print(ANDROID_LOG_INFO, "JNITag","add size : %d", ecgSize);
    int iv_ecg[2] = {1, ecgSize};
    int i;
    emxArray_real_T *ecg;
    emxArray_real_T *ret;

    // 输入参数：ECG数据
    ecg = emxCreateND_real_T(2, iv_ecg);            // 设置数组大小
    for (i = 0; i < ecg->size[1]; i++) {
        ecg->data[ecg->size[0] * i] = ecgData[i];
    }
    // 输出参数：滤波结果
    emxInitArray_real_T(&ret, 2);
    // 滤波
    filter_ecg(ecg, sampleRate, ret);

    jdoubleArray result = env->NewDoubleArray(ecgSize);
    double *r = new double[ecgSize];
    for (i = 0; i < ecgSize; i++) {
        r[i] = ret->data[i];
    }
    env->SetDoubleArrayRegion(result, 0, ecgSize, r);
    delete[] r;
    // 释放内存
    emxDestroyArray_real_T(ecg);
    emxDestroyArray_real_T(ret);

    env->ReleaseDoubleArrayElements(ecg_data, ecgData, 0);
    return result;
}

JNIEXPORT jboolean JNICALL
Java_com_mgtech_blelib_ecg_EcgUtil_detect(JNIEnv *env, jclass type, jdoubleArray ecg_data,
                                      jint ecgSize, jdouble sampleRate, jdouble hrMin,
                                      jdouble hrMax, jdouble rr_num) {
    jdouble *ecgData = env->GetDoubleArrayElements(ecg_data, NULL);

    // TODO
    int iv_ecg[2] = {1, ecgSize};
    int i;
    emxArray_real_T *ecg;
    double err;
    double rrNum;
    double hrAvg;
    double vMax;
    double vMin;

    // 输入参数：滤波后的ECG数据
    ecg = emxCreateND_real_T(2, iv_ecg);
    for (i = 0; i < ecg->size[1]; i++) {
        ecg->data[ecg->size[0] * i] = ecgData[i];
    }
    // 识别心电
    detect_ecg(ecg, sampleRate, hrMin, hrMax, rr_num, &err, &rrNum, &hrAvg, &vMax, &vMin);

    // 释放内存
    emxDestroyArray_real_T(ecg);

    env->ReleaseDoubleArrayElements(ecg_data, ecgData, 0);
    return (jboolean) (err == 0);
}
JNIEXPORT jstring JNICALL
Java_com_mgtech_blelib_ecg_EcgUtil_stringFromJNI(JNIEnv *env, jclass type) {
    std::string hello = "Ecg String from JNI";
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jdoubleArray JNICALL
Java_com_mgtech_blelib_ecg_EcgUtil_detectEcg(JNIEnv *env, jclass type, jdoubleArray ecg_data,
                                         jint ecgSize, jdouble sampleRate, jdouble hrMin,
                                         jdouble hrMax, jdouble rr_num) {
    jdouble *ecgData = env->GetDoubleArrayElements(ecg_data, NULL);

    // TODO
    int iv_ecg[2] = {1, ecgSize};
    int i;
    emxArray_real_T *ecg;
    double err;
    double rrNum;
    double hrAvg;
    double vMax;
    double vMin;

    // 输入参数：滤波后的ECG数据
    ecg = emxCreateND_real_T(2, iv_ecg);
    for (i = 0; i < ecg->size[1]; i++) {
        ecg->data[ecg->size[0] * i] = ecgData[i];
    }
    // 识别心电
    detect_ecg(ecg, sampleRate, hrMin, hrMax, rr_num, &err, &rrNum, &hrAvg, &vMax, &vMin);

    // 释放内存
    emxDestroyArray_real_T(ecg);

    env->ReleaseDoubleArrayElements(ecg_data, ecgData, 0);


    jdoubleArray result = env->NewDoubleArray(5);
    double *r = new double[5];
    r[0] = err;
    r[1] = rrNum;
    r[2] = hrAvg;
    r[3] = vMax;
    r[4] = vMin;
    env->SetDoubleArrayRegion(result, 0, 5, r);
    delete[] r;
    // 释放内存
    return result;
}
}