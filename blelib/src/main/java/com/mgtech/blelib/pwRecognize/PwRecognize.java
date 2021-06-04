package com.mgtech.blelib.pwRecognize;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/2/9.
 * 脉图识别
 */

public class PwRecognize implements IRecognize{
    private Callback callback;
//    public static final int COMPLETE = 0;
//    public static final int FAULT = 2;
//    public static final int CONTINUE = 1;
    private static final short NONE_POINT = -1;
//    private static final int MARGIN_START_DEFAULT = 66;
    private static final float MARGIN_START_DEFAULT = 0.5f;
    private static final int MARGIN_END_DEFAULT = 0;
    //    private static final int HEART_DIFFER_DEFAULT = 10;
    private static final float HEART_DIFFER_RATIO_DEFAULT = 0.15f;
    private static final float HEART_MIN_DEFAULT = 35;
    private static final float HEART_MAX_DEFAULT = 110;
    private static final float RISE_EDGE_HEIGHT_DEFAULT = 3f;
    private static final float RISE_EDGE_WIDTH_DEFAULT = 2f;
    private static final int SECTION_LENGTH_DEFAULT = 2;
    private static final float SAMPLE_RATE_DEFAULT = 128;
    private static final float PEAK_WIDTH_RATIO_DEFAULT = 0.33333f;
    private static final int COMPLETE_PW_MIN_DEFAULT = 20;
    private static final int COMPLETE_PW_MAX_DEFAULT = 25;
    private static final int COMPLETE_TIME_MAX_DEFAULT = 30;
    private static final int TIME_OUT_DEFAULT = 90;
    private static final int RESERVED_SECTION_LENGTH_DEFAULT = 0;

    private static final float OUT_OF_REGION_RATIO = 0.0f;
    private static final int ADC_MAX = 3650;
//    private static final int ADC_MAX = 4096;

    private List<Section> resultData;
    private List<Short> rawData;
    private LinkedList<Integer> maxPoints;
    private LinkedList<Integer> minPoints;
    private LinkedList<PointPair> pairList;
    private int marginStart;
    private int marginEnd;
    private float hrDifferRatio;
    private float hrMin;
    private float hrMax;
    private float riseEdgeHeightRatio;
    private float riseEdgeWidthRatio;
    private int sectionMinPwNumber;
    private float sampleRate;
    private float peakWidthRatio;
    private float maxPointDistance;
    private float minPointDistance;
    private int completePwMin;
    private int completePwMax;
    private int completeTimeMax;
    private int timeOut;

    private int topRegion;
    private int bottomRegion;

    private int reservedPwNumberStart;


    private static final String TAG = "RECG";

//    public interface EcgCallback {
//        void onSectionRemove();
//    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void clear() {
        rawData.clear();
        resultData.clear();
        maxPoints.clear();
        minPoints.clear();
        pairList.clear();
    }


    private void tagI(String msg) {
        Log.i(TAG, msg);
    }

    private void tagE(String msg) {
        Log.e(TAG, msg);
    }

    public PwRecognize() {
        this.marginEnd = MARGIN_END_DEFAULT;
        this.hrDifferRatio = HEART_DIFFER_RATIO_DEFAULT;
        this.hrMin = HEART_MIN_DEFAULT;
        this.hrMax = HEART_MAX_DEFAULT;
        this.riseEdgeHeightRatio = RISE_EDGE_HEIGHT_DEFAULT;
        this.riseEdgeWidthRatio = RISE_EDGE_WIDTH_DEFAULT;
        this.sectionMinPwNumber = SECTION_LENGTH_DEFAULT;
        this.sampleRate = SAMPLE_RATE_DEFAULT;
        this.peakWidthRatio = PEAK_WIDTH_RATIO_DEFAULT;
        this.maxPointDistance = 60 * sampleRate / HEART_MIN_DEFAULT;
        this.minPointDistance = 60 * sampleRate / HEART_MAX_DEFAULT;
        this.marginStart = Math.round(MARGIN_START_DEFAULT * sampleRate);

        this.completePwMax = COMPLETE_PW_MAX_DEFAULT;
        this.completePwMin = COMPLETE_PW_MIN_DEFAULT;
        this.completeTimeMax = COMPLETE_TIME_MAX_DEFAULT;
        this.timeOut = TIME_OUT_DEFAULT;
        this.reservedPwNumberStart = RESERVED_SECTION_LENGTH_DEFAULT;

        this.topRegion = Math.round(ADC_MAX * (1 - OUT_OF_REGION_RATIO));
        this.bottomRegion = Math.round(ADC_MAX * OUT_OF_REGION_RATIO);
        initList();
    }

    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
        this.maxPointDistance = 60 * sampleRate / hrMin;
        this.minPointDistance = 60 * sampleRate / hrMax;
        this.marginStart = Math.round(MARGIN_START_DEFAULT * sampleRate);

    }

    public void setComplete(int completePwMin, int completePwMax) {
        this.completePwMin = completePwMin;
        this.completePwMax = completePwMax;
    }

    /**
     * 设置超时时间
     *
     * @param timeOut 超时时间/秒
     */
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 设置开始预留脉图长度
     *
     * @param number 预留起始脉图长度
     */
    public void setReservedPwNumber(int number) {
//        this.reservedPwNumberStart = number;
    }

    private void initList() {
        this.resultData = new ArrayList<>();
        this.maxPoints = new LinkedList<>();
        this.minPoints = new LinkedList<>();
        this.pairList = new LinkedList<>();
        this.rawData = new ArrayList<>();
    }

    private void log() {
        int size = 0;
        for (Section section : resultData) {
            size += section.pairs.size() - 1;
        }
        Log.e(TAG, "result: size " + size + " " + sampleRate);
    }

    private String arrayToString(List list) {
        StringBuilder s = new StringBuilder();
        s.append("[");
        for (Object i : list) {
            s.append(i.toString());
            s.append(",");
        }
        s.append("]");
        return s.toString();
    }

    /**
     * 添加新点
     *
     * @param data 新的解压数据
     */
    public int addData(short[] data) {
        int beforeSize = rawData.size();
        for (short i : data) {
            rawData.add(i);
        }
        getPeakPoint(beforeSize);
        addPair();
        addSection();
        log();
        if (isMissionComplete()) {
            return COMPLETE;
        }
        if (rawData.size() >= timeOut * sampleRate) {
            initList();
            return FAULT;
        }
        return CONTINUE;
    }

    /**
     * 手环运动，插入100点
     */
    public void addError() {
        for (int i = 0; i < sampleRate * 2; i++) {
            rawData.add((short) 0);
        }
    }

    /**
     * 获取全部数据
     *
     * @return 全部数据
     */
    public List<Short> getRawData() {
        List<Short> rawData = new ArrayList<>();
        rawData.addAll(this.rawData);
        return rawData;
    }

    /**
     * 获得识别后的数据（阿里）
     *
     * @return List<Short>
     */
//    public List<Short> getResult(float heartRate) {
//        List<Short> result = new ArrayList<>();
////        int totalPointNumber = 0;
//        result.add((short) 1);
//        result.add((short) (SAMPLE_RATE_CONSTANT / sampleRate));
//        result.add((short) 0);
//        result.add((short) Math.round(heartRate));
//        result.add((short) 0xFFF);
//        result.add((short) 0xFFF);
//        int size = resultData.size();
//        for (int i = 0; i < size; i++) {
//            Section section = resultData.get(i);
//            int end = section.endPos;
//            int totalPointNumber = end - section.startPos + 1;
//            if (totalPointNumber % 2 == 1) {
//                end++;
//            }
//            for (int j = section.startPos; j <= end; j++) {
//                result.add(rawData.get(j));
//            }
//            result.add((short) 0xFFF);
//            result.add((short) 0xFFF);
//            result.add((short) 0);
//            result.add((short) section.heartRate);
//            result.add((short) 0xFFF);
//            result.add((short) 0xFFF);
//        }
//        initList();
//        return result;
//    }

    /**
     * 获得识别后的数据（修改）
     *
     * @return List<Short>
     */
    public List<Object> getResult() {
        List<Object> result = new ArrayList<>();
//        int totalPointNumber = 0;
        result.add(sampleRate);
        result.add(getTotalHeartRate());
        result.add(65535);
        int size = resultData.size();
        for (int i = 0; i < size; i++) {
            Section section = resultData.get(i);
            int end = section.endPos;
            for (int j = section.startPos; j <= end; j++) {
                result.add(rawData.get(j));
            }
            result.add(65535);
            result.add(section.heartRate);
            result.add(65535);
        }
        initList();
        return result;
    }

    /**
     * 计算脉图个数
     *
     * @return int 个数
     */
    public int getResultSize() {
        int totalPwNumber = 0;
        int sectionSize = resultData.size();
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            if ((i == sectionSize - 1 && section.pairs.size() - 1 >= sectionMinPwNumber + reservedPwNumberStart)
                    || i != sectionSize - 1) {
                totalPwNumber += section.pairs.size() - 1 - reservedPwNumberStart;
            }
        }
        return totalPwNumber;
    }

    /**
     * 获取最大最小值
     *
     * @return [0]最大值，[1]最小值
     */
    public short[] getMaxAndMinPoint() {
        int sectionSize = resultData.size();
        short[] maxAndMin = new short[2];
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            if ((i == sectionSize - 1 && section.pairs.size() - 1 >= sectionMinPwNumber + reservedPwNumberStart)
                    || i != sectionSize - 1) {
                for (PointPair pair : section.pairs) {
                    if (maxAndMin[0] == 0) {
                        maxAndMin[0] = rawData.get(pair.peak);
                    } else {
                        maxAndMin[0] = (short) Math.max(maxAndMin[0], rawData.get(pair.peak));
                    }
                    if (maxAndMin[1] == 0) {
                        maxAndMin[1] = rawData.get(pair.valley);
                    } else {
                        maxAndMin[1] = (short) Math.min(maxAndMin[0], rawData.get(pair.valley));
                    }
                }
            }
        }
        return maxAndMin;
    }

    /**
     * 添加极值点
     *
     * @param beforeSize 上次长度
     */
    private void getPeakPoint(int beforeSize) {
        // 判断上一组数据最后一个点是否为极值
        short last1 = beforeSize >= 1 ? rawData.get(beforeSize - 1) : NONE_POINT;
        short last2 = beforeSize >= 2 ? rawData.get(beforeSize - 2) : NONE_POINT;
        addPeakPosition(beforeSize - 1, last1, last2, rawData.get(beforeSize));
        // 判断第1个点
        addPeakPosition(beforeSize, rawData.get(beforeSize), last1, rawData.get(beforeSize + 1));
        // 判断第n-1个点
        int currentSize = rawData.size();
        for (int i = beforeSize + 1; i < currentSize - 1; i++) {
            addPeakPosition(i, rawData.get(i), rawData.get(i - 1), rawData.get(i + 1));
        }
    }

    /**
     * 筛选极值点对
     */
    private void addPair() {
        // 保留2个点，每有3个点，解析第一个点
        while (minPoints.size() >= 3 && maxPoints.size() >= 3) {
            // 波峰前无波谷,删掉第一个波峰
            if (maxPoints.getFirst() < minPoints.getFirst()) {
                maxPoints.removeFirst();
                tagI("addPair error: 0");
                continue;
            }
            // 第一个波谷-波峰距离错误，大于最大脉图长度1/2,删掉第一个波峰、波谷
//            if (maxPoints.getFirst() - minPoints.getFirst() > maxPointDistance / 2) {
//                tagI("addPair error: 1");
//
//                maxPoints.removeFirst();
//                minPoints.removeFirst();
//                continue;
//            }
            // 上升沿宽度比例错误
            if (maxPoints.getFirst() - minPoints.getFirst() > peakWidthRatio * maxPointDistance) {
                tagI("addPair error: 2");
                maxPoints.removeFirst();
                minPoints.removeFirst();
                continue;
            }
            if (rawData.get(maxPoints.getFirst()) > topRegion || rawData.get(minPoints.getFirst()) < bottomRegion){
                tagI("addPair error: 3" + rawData.get(maxPoints.getFirst()) + " " + rawData.get(minPoints.getFirst()));
                maxPoints.removeFirst();
                minPoints.removeFirst();
                continue;
            }
            // 有足够采样点标定起始点
            if (minPoints.getFirst() >= marginStart) {
                PointPair pointPair = new PointPair();
                pointPair.peak = maxPoints.getFirst();
                pointPair.valley = minPoints.getFirst();
                pairList.add(pointPair);
                tagE("addPair: " + arrayToString(pairList));
            }
            // 删掉第一个波峰、波谷，进入下轮
            maxPoints.removeFirst();
            minPoints.removeFirst();
        }
    }

    /**
     * 添加到脉图组
     */
    private void addSection() {
        while (!pairList.isEmpty()) {
            PointPair pair = pairList.poll();
            Section section = getIncompleteSection();
            int sectionSize = section.pairs.size();
            if (sectionSize == 0) {
                section.pairs.add(pair);
//                section.startPos = pair.valley - marginStart;
            } else {
                boolean error = false;
                PointPair lastPair = section.pairs.get(sectionSize - 1);
                // 峰峰值大于脉图最大长度
                int length = pair.peak - lastPair.peak;
                float hr = calculateHeartRate(length);
                if (length > maxPointDistance) {
                    tagE("error 0 " + length + "  " + maxPointDistance + " " + pair.peak + " " + lastPair.peak);
                    error = true;
                }
                // 相邻两个上升沿（峰-谷）宽度差大于阈值
                if (!error && sectionSize >= 1) {
                    int lastEdgeWidth = Math.abs(lastPair.peak - lastPair.valley);
                    int currentEdgeWidth = Math.abs(pair.peak - pair.valley);
                    if (currentEdgeWidth > lastEdgeWidth * riseEdgeWidthRatio ||
                            lastEdgeWidth > currentEdgeWidth * riseEdgeWidthRatio) {
                        tagE("error 1 " + lastEdgeWidth + ":" + currentEdgeWidth);
                        error = true;
                    }
                }
                // 相邻两个上升沿（峰-谷）高度差大于阈值
                if (!error && sectionSize >= 1) {
                    int lastEdgeHeight = Math.abs(rawData.get(lastPair.peak) - rawData.get(lastPair.valley));
                    int currentEdgeHeight = Math.abs(rawData.get(pair.peak) - rawData.get(pair.valley));
                    if (currentEdgeHeight > lastEdgeHeight * riseEdgeHeightRatio ||
                            lastEdgeHeight > currentEdgeHeight * riseEdgeHeightRatio) {
                        tagE("error 2 " + lastEdgeHeight + ":" + currentEdgeHeight);
                        error = true;
                    }
                }
                // 心率错误
                if (!error && !(hr <= hrMax && hr >= hrMin)) {
                    tagE("error 3");
                    error = true;
                }
                // 相邻心率差过大（需要至少2个脉图）
                if (!error && !isNeighborHeartRateCorrect(section, hr)) {
                    tagE("error 3.5");
                    error = true;
                }
//                // 脉图片段心率错误
//                if (!error && !isHeartRateCorrect(section, hr)) {
//                    tagE("error 4");
//                    error = true;
//                }
//                // 总心率错误
//                if (!error && !isTotalHeartRateCorrect(hr)) {
//                    tagE("error 5");
//                    error = true;
//                }
                if (error) {
                    // 处理错误
                    if (sectionSize - 1 >= sectionMinPwNumber + reservedPwNumberStart) {
                        // 连续脉图
                        section.isComplete = true;
                        PointPair startPair = section.pairs.get(reservedPwNumberStart);
                        section.startPos = startPair.peak - marginStart;
                        section.endPos = lastPair.peak;
                    } else {
                        // 无连续脉图，删除
                        tagE("remove section: " + resultData.get(resultData.size() - 1));
                        resultData.remove(resultData.size() - 1);
                        if (callback != null) {
                            callback.onSectionRemove();
                        }
                    }
                } else {
                    // 无错误
                    section.pairs.add(pair);
                    PointPair startPair = section.pairs.get(reservedPwNumberStart);
                    section.startPos = startPair.peak - marginStart;
                    section.endPos = pair.peak;
                    setSectionHeartRate(hr, section);
                    Log.e(TAG, "error right: " + section.pairs.size());

                }
            }
        }
    }


    /**
     * 是否识别完成
     *
     * @return 完成
     */
    private boolean isMissionComplete() {
        int totalPwPoint = 0;
        int totalPwNumber = 0;
        int sectionSize = resultData.size();
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            Log.e(TAG, "section pairs " + section.pairs.size());
            if ((i == sectionSize - 1 && section.pairs.size() - 1 >= sectionMinPwNumber + reservedPwNumberStart)
                    || i != sectionSize - 1) {
                totalPwNumber += section.pairs.size() - 1 - reservedPwNumberStart;
                totalPwPoint += section.endPos - section.startPos + 1;
            }
        }

        // 脉图时间少于30（参数可调）秒，且脉图数量达到30（参数可调）个；
//        if (totalPwNumber >= completePwMax && totalPwPoint <= completeTimeMax * sampleRate) {
//            return true;
//        }
        // 脉图时间超过30（参数可调）秒，且脉图数量大于15（参数可调）个。
//        if (totalPwPoint >= completePwMin && totalPwPoint >= completeTimeMax * sampleRate) {
//            return true;
//        }
        Log.e(TAG, "totalPwPoint: " + totalPwNumber + " " + completePwMax + " section size:" + sectionSize +
                "; points: " + totalPwPoint + " " + completeTimeMax * sampleRate);
        return (totalPwNumber >= completePwMin && totalPwPoint >= completeTimeMax * sampleRate) ||
                (totalPwNumber >= completePwMax && totalPwPoint <= completeTimeMax * sampleRate);
    }

    /**
     * 获取最后一个未完成的片段
     *
     * @return Section
     */
    private Section getIncompleteSection() {
        if (resultData.isEmpty() || resultData.get(resultData.size() - 1).isComplete) {
            resultData.add(new Section());
        }
        return resultData.get(resultData.size() - 1);
    }

    /**
     * 心率是否符合该组脉图需求
     *
     * @param section 该组脉图
     * @param hr      心率
     * @return boolean
     */
    private boolean isHeartRateCorrect(Section section, float hr) {

        return section.heartRate == Section.NO_HR || section.heartRate == 0 ||
                (Math.abs(hr - section.heartRate) / section.heartRate <= hrDifferRatio);
    }

    /**
     * 总心率是否符合该组脉图需求
     *
     * @param hr 心率
     * @return boolean
     */
    private boolean isTotalHeartRateCorrect(float hr) {
        tagE("total hr: " + getTotalHeartRate());

        float total = getTotalHeartRate();
        return total == Section.NO_HR || total == 0 || Math.abs(hr - total) / total <= hrDifferRatio;
    }

    /**
     * 相邻心率差是否正确
     *
     * @param section 脉图组
     * @param hr      测试心率
     * @return true正确，false错误
     */
    private boolean isNeighborHeartRateCorrect(Section section, float hr) {
        int size = section.pairs.size();
        if (size < 2) {
            return true;
        }
        int length = section.pairs.get(size - 1).peak - section.pairs.get(size - 2).peak;
        float lastHr = calculateHeartRate(length);
        boolean flag = Math.abs(lastHr - hr) <= hrDifferRatio * hr;
        if (!flag) {
            Log.e(TAG, "error 相邻心率错误: " + hr + " " + lastHr);
        }
        return flag;
    }

    /**
     * 计算总心率
     *
     * @return 心率
     */
    public float getTotalHeartRate() {
        float hr = Section.NO_HR;
        if (!resultData.isEmpty()) {
            int totalLength = 0;
            int totalPw = 0;
            for (Section section : resultData) {
                if (section.pairs.size() >= 2) {
                    totalLength += section.pairs.get(section.pairs.size() - 1).peak - section.pairs.get(0).peak;
                    totalPw += section.pairs.size() - 1;
                }
            }
            if (totalPw != 0) {
                hr = calculateHeartRate(totalLength / totalPw);
            }
        }
        return hr;
    }

    /**
     * 添加 hr到section
     *
     * @param hr      心率
     * @param section pw片段
     */
    private void setSectionHeartRate(float hr, Section section) {
        int size = section.pairs.size();
        if (size <= 2) {
            section.heartRate = hr;
            return;
        }
        section.heartRate = calculateHeartRate(Math.abs(section.pairs.get(1).peak -
                section.pairs.get(size - 1).peak) / (size - 2));
    }

    /**
     * 计算心率
     *
     * @param pointLength 数据长度
     * @return 心率
     */
    private float calculateHeartRate(int pointLength) {
        return 60 * sampleRate / pointLength;
    }

    /**
     * 是否为极小值
     *
     * @param p      待测点
     * @param before 前一点
     * @param after  后一点
     * @return boolean
     */
    private boolean isMin(short p, short before, short after) {
        return before != NONE_POINT && p != NONE_POINT && ((p < before && p <= after) || (p <= before && p < after));
    }

    /**
     * 是否为极大值
     *
     * @param p      待测点
     * @param before 前一点
     * @param after  后一点
     * @return boolean
     */
    private boolean isMax(short p, short before, short after) {
        return before != NONE_POINT && p != NONE_POINT && ((p >= before && p > after) || (p > before && p >= after));
    }

    /**
     * 添加极值点位置
     *
     * @param pos    待测点位置
     * @param p      待测点
     * @param before 前一点
     * @param after  后一点
     */
    private void addPeakPosition(int pos, short p, short before, short after) {
        if (isMin(p, before, after)) {
            addMinPosition(pos);
        } else if (isMax(p, before, after)) {
            addMaxPosition(pos);
        }
    }

    /**
     * 添加位置到极小值队列
     *
     * @param pos 位置
     */
    private void addMinPosition(int pos) {
        if (minPoints.isEmpty()) {
            // 空队列直接添加
            tagI("add min");
            minPoints.add(pos);
        } else {
            int lastPos = minPoints.getLast();
            if (pos - lastPos < minPointDistance) {
                // 在间隔范围内，之允许一个最小值，只保存更小的点，大小相同保存最后一个
                tagI("exchange min (" + pos + ")=" + rawData.get(pos));
                if (rawData.get(pos) <= rawData.get(lastPos)) {
                    minPoints.set(minPoints.size() - 1, pos);
                }
            } else {
                // 间隔范围合适，添加新点
                tagI("add min (" + pos + ")=" + rawData.get(pos));
                minPoints.add(pos);
                checkMinPoint();
            }
        }

    }

    /**
     * 添加位置到极大值队列
     *
     * @param pos 位置
     */
    private void addMaxPosition(int pos) {
        if (maxPoints.isEmpty()) {
            // 空队列直接添加
            tagI("add max");
            maxPoints.add(pos);
        } else {
            int lastPos = maxPoints.getLast();
            if (pos - lastPos < minPointDistance) {
                // 在间隔范围内，只允许一个最大值，只保存更大的点，大小相同保存最后一个
                tagI("exchange max (" + pos + ")=" + rawData.get(pos));
                if (rawData.get(pos) >= rawData.get(lastPos)) {
                    maxPoints.set(maxPoints.size() - 1, pos);
                }

            } else {
                // 间隔范围合适，添加新点
                tagI("add max (" + pos + ")=" + rawData.get(pos));
                maxPoints.add(pos);
                checkMaxPoint();
            }
        }


    }

    private void checkMinPoint() {
        // 最后两个极小值之间应存在1个极大值,否则删掉倒数第二个极小值
        if (minPoints.size() >= 2) {
            if (!hasPoint(minPoints.get(minPoints.size() - 2), minPoints.getLast(), maxPoints)) {
                tagI("------remove min 1");
                minPoints.remove(minPoints.size() - 2);
            }
        }
        // 倒数2、3个极小值之间应存在1个极大值，否则删除倒数第二个极小值
        if (minPoints.size() >= 3) {
            if (!hasPoint(minPoints.get(minPoints.size() - 3), minPoints.get(minPoints.size() - 2), maxPoints)) {
                tagI("------remove min 2");
                minPoints.remove(minPoints.size() - 2);
            }
        }
    }

    private void checkMaxPoint() {
        // 最后两个极大值之间应存在1个极小值,否则删掉倒数第2个极大值
        if (maxPoints.size() >= 2) {
            if (!hasPoint(maxPoints.get(maxPoints.size() - 2), maxPoints.getLast(), minPoints)) {
                maxPoints.remove(maxPoints.size() - 2);
                tagI("------remove max 1");
            }
        }
        // 倒数2、3个极大值之间应存在1个极小值，否则删除倒数第2个极大值
        if (maxPoints.size() >= 3) {
            if (!hasPoint(maxPoints.get(maxPoints.size() - 3), maxPoints.get(maxPoints.size() - 2), minPoints)) {
                maxPoints.remove(maxPoints.size() - 2);
                tagI("------remove max 2");
            }
        }
    }

    /**
     * 判断监测队列最后3个点中有没有点位于两点之间
     *
     * @param startPos 左边的点
     * @param endPos   右边的点
     * @param examList 检测的队列
     * @return boolean
     */
    private boolean hasPoint(int startPos, int endPos, List<Integer> examList) {
        if (examList.isEmpty()) {
            return false;
        }
        int listSize = examList.size();
        int examSize = Math.min(listSize, 3);
        for (int i = 0; i < examSize; i++) {
            int testPos = examList.get(listSize - 1 - i);
            if (testPos > startPos && testPos < endPos) {
                return true;
            }
        }
        return false;
    }

    private class PointPair {
        int peak;
        int valley;

        @Override
        public String toString() {
            return "{" +
                    peak +
                    ", " + valley +
                    '}';
        }
    }

    private class Section {
        private static final int NO_HR = -1;
        List<PointPair> pairs;
        int startPos;
        int endPos;
        float heartRate = NO_HR;
        boolean isComplete;
//        float hrMax = NO_HR;
//        float hrMin = NO_HR;

        Section() {
            this.pairs = new ArrayList<>();
            this.isComplete = false;
        }

        @Override
        public String toString() {
            return arrayToString(pairs);
        }
    }
}
