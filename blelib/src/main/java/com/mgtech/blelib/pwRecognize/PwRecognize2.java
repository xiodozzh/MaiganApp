package com.mgtech.blelib.pwRecognize;

import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhaixiang on 2017/6/26.
 * 脉图识别
 */

public class PwRecognize2 implements IRecognize {
    private IRecognize.Callback callback;
    private static final float NO_HR = -1;
    private static final short NONE_POINT = -1;
    // 起始点预留比例
    private static final float MARGIN_START_DEFAULT = 0.5f;
    private static final float HEART_MIN_DEFAULT = 35;
    private static final float HEART_MAX_DEFAULT = 110;
    private static final float RISE_EDGE_HEIGHT_MIN_DEFAULT = 10;
    private static final float RISE_EDGE_WIDTH_MIN_DEFAULT = 0.05f;
    private static final float RISE_EDGE_WIDTH_MAX_DEFAULT = 0.3f;
    private static final float RISE_EDGE_WIDTH_RATIO_DEFAULT = 3f;
    private static final float RISE_EDGE_HEIGHT_DIFFER_RATIO_DEFAULT = 2f;
    private static final int SECTION_PAIR_NUM_MIN = 2;

    private static final float SAMPLE_RATE_DEFAULT = 128;
    private static final int COMPLETE_PW_MIN_DEFAULT = 10;
    private static final int COMPLETE_PW_MAX_DEFAULT = 15;
    private static final int COMPLETE_TIME_MAX_DEFAULT = 30;
    private static final int TIME_OUT_DEFAULT = 90;
    private static final float OUT_OF_REGION_RATIO = 0.0f;
//    private static final int ADC_MAX = 3650;
    private static final int ADC_MAX = 3590;
//    private static final int ADC_MAX = 4096;

    private List<Section> resultData;
    private List<Short> rawData;
    private LinkedList<Integer> maxPoints;

    private static final float peakPeakRatio = 2f;

    private int marginStart;
    private float hrMin;
    private float hrMax;
    private float edgeHeightMin;
    private float edgeWidthRationMin;
    private float edgeWidthRationMax;
    private float edgeWidthDifferRatio;
    private float edgeHeightDifferRatio;
    private float sampleRate;
    private float sectionMinPwNumber;

    private float maxPointDistance;
    private float minPointDistance;
    private float maxEdgeWidth;
    private float minEdgeWidth;

    private int completePwMin;
    private int completePwMax;
    private int completeTimeMax;
    private int timeOut;

    private int rPointer;
    private int breakPointer;

    private int topRegion;
    private int bottomRegion;

    public PwRecognize2() {
        this.rawData = new ArrayList<>();
        this.maxPoints = new LinkedList<>();
        this.hrMin = HEART_MIN_DEFAULT;
        this.hrMax = HEART_MAX_DEFAULT;
        this.edgeHeightMin = RISE_EDGE_HEIGHT_MIN_DEFAULT;
        this.edgeWidthRationMin = RISE_EDGE_WIDTH_MIN_DEFAULT;
        this.edgeWidthRationMax = RISE_EDGE_WIDTH_MAX_DEFAULT;
        this.edgeWidthDifferRatio = RISE_EDGE_WIDTH_RATIO_DEFAULT;
        this.edgeHeightDifferRatio = RISE_EDGE_HEIGHT_DIFFER_RATIO_DEFAULT;
        this.sectionMinPwNumber = SECTION_PAIR_NUM_MIN;
        this.sampleRate = SAMPLE_RATE_DEFAULT;
        this.topRegion = Math.round(ADC_MAX * (1 - OUT_OF_REGION_RATIO));
        this.bottomRegion = Math.round(ADC_MAX * OUT_OF_REGION_RATIO);

        this.maxPointDistance = 60 * sampleRate / HEART_MIN_DEFAULT;
        this.minPointDistance = 60 * sampleRate / HEART_MAX_DEFAULT;
        this.marginStart = Math.round(MARGIN_START_DEFAULT * sampleRate);

        this.completePwMax = COMPLETE_PW_MAX_DEFAULT;
        this.completePwMin = COMPLETE_PW_MIN_DEFAULT;
        this.completeTimeMax = COMPLETE_TIME_MAX_DEFAULT;
        this.timeOut = TIME_OUT_DEFAULT;
        this.maxEdgeWidth = sampleRate * edgeWidthRationMax;
        this.minEdgeWidth = sampleRate * edgeWidthRationMin;

        this.rPointer = 0;
        this.breakPointer = 0;

        initList();
    }

    private void initList() {
        this.resultData = new ArrayList<>();
        this.maxPoints = new LinkedList<>();
        this.rawData = new ArrayList<>();
    }

    @Override
    public int addData(short[] data) {
        int beforeSize = rawData.size();
        for (short i : data) {
            rawData.add(i);
        }
        addPeakPoint(beforeSize);
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
     * 是否识别完成
     * @return true识别完成
     */
    private boolean isMissionComplete() {
        int totalPwPoint = 0;
        int totalPwNumber = 0;
        int sectionSize = resultData.size();
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            int pwNumber = section.pairs.size() - 1;
            if (pwNumber < sectionMinPwNumber) {
                continue;
            }
            if (i == sectionSize - 1) {
                if (pwNumber -1 >= sectionMinPwNumber) {
                    totalPwNumber += pwNumber - 1;
                    totalPwPoint += section.pairs.get(pwNumber - 1).peak - section.pairs.get(0).peak + marginStart;
                }
            } else {
                totalPwNumber += pwNumber;
                totalPwPoint += section.pairs.get(pwNumber).peak - section.pairs.get(0).peak + marginStart;
            }
        }
        // 脉图时间少于30（参数可调）秒，且脉图数量达到30（参数可调）个；
        // 脉图时间超过30（参数可调）秒，且脉图数量大于15（参数可调）个。
        return (totalPwNumber >= completePwMin && totalPwPoint >= completeTimeMax * sampleRate) ||
                (totalPwNumber >= completePwMax && totalPwPoint <= completeTimeMax * sampleRate);
    }

    /**
     * 添加极值点
     *
     * @param beforeSize 上次长度
     */
    private void addPeakPoint(int beforeSize) {
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
     * 添加极值点位置
     *
     * @param pos    待测点位置
     * @param p      待测点
     * @param before 前一点
     * @param after  后一点
     */
    private void addPeakPosition(int pos, short p, short before, short after) {
        rPointer = pos;
        if (isMax(p, before, after)) {
            if (maxPoints.isEmpty()) {
                // 空队列直接添加
                maxPoints.add(pos);
            } else {
                int maxPointSize = maxPoints.size();
                int lastPos = maxPoints.get(maxPointSize - 1);
                if (pos - lastPos < minPointDistance) {
                    // 在间隔范围内，只允许一个最大值，只保存更大的点，大小相同保存最后一个
                    if (rawData.get(pos) >= rawData.get(lastPos)) {
                        maxPoints.set(maxPointSize - 1, pos);
//                        print();
                    }
                } else {
                    // 间隔范围合适，添加新点
                    maxPoints.add(pos);
                }
            }
            addSection(true);
        }
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
        boolean isOutOfRange = p > topRegion || p < bottomRegion;
        return before != NONE_POINT && p != NONE_POINT && ((p >= before && p > after) || (p > before && p >= after)) && !isOutOfRange;
    }

    /**
     * 将peak点配成上升沿加入section
     *
     * @param needRetain 是否需要保留最后一个peak
     */
    private void addSection(boolean needRetain) {
        int margin = (int) Math.max(marginStart, maxEdgeWidth);
        while (maxPoints.size() > (needRetain ? 1 : 0)) {
            int peak = maxPoints.removeFirst();
            final int peakValue = rawData.get(peak);
            // 如果前面没有足够预留点，删除
            if (peak - breakPointer <= margin) {
                Log.e(TAG, "addSection: 没有足够预留点");
                continue;
            }
            // 遍历上升沿，找出最小值
            boolean error = false;
            int minValue = peakValue;
            int minIndex = peak;
            for (int i = 1; i <= maxEdgeWidth; i++) {
                int currentValue = rawData.get(peak - i);
                if (peakValue < currentValue) {
                    error = true;
                    Log.e(TAG, "addSection: + error " + peakValue + " " + currentValue);
                    break;
                }
                if (currentValue <= minValue) {
                    minValue = currentValue;
                    minIndex = peak - i;
                }
            }
            Log.e(TAG, "addSection: ");
            if (!error && peakValue - minValue > edgeHeightMin && minIndex <= peak - minEdgeWidth) {
                PointPair pair = new PointPair();
                pair.peak = peak;
                pair.valley = minIndex;
                addPair(pair);
            }
        }
        if (!needRetain) {
            breakSection();
        }
    }

    /**
     * 储存上升沿
     *
     * @param pair 上升沿对
     */
    private void addPair(PointPair pair) {
        Section section = getIncompleteSection();
        int size = section.pairs.size();
        Log.e(TAG, "addPair: " + size);
        if (size == 0) {
            // 没有上升沿，直接添加
            section.pairs.add(pair);
        } else {
            PointPair lastPair = section.pairs.get(size - 1);
            // 比较和上一个上升沿宽、高比是否在正常范围内
            if (isNeighbourCorrect(lastPair, pair)) {
                // 合理，添加
                section.pairs.add(pair);
            } else {
                // 不合理，并多于1个，删除最后一个，并添加新的
                section.pairs.remove(size - 1);
                section.pairs.add(pair);
            }
            checkPair();
        }
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
     * 相邻是否正确
     *
     * @param pointPair1 相邻上升沿
     * @param pointPair2 相邻上升沿
     * @return 是否正确
     */
    private boolean isNeighbourCorrect(PointPair pointPair1, PointPair pointPair2) {
        return pointPair1.getEdgeHeight() < pointPair2.getEdgeHeight() * edgeHeightDifferRatio
                && pointPair1.getEdgeHeight() * edgeHeightDifferRatio > pointPair2.getEdgeHeight()
                && pointPair1.getEdgeWidth() < pointPair2.getEdgeWidth() * edgeWidthDifferRatio
                && pointPair1.getEdgeWidth() * edgeHeightDifferRatio > pointPair2.getEdgeWidth();
    }

    /**
     * 检查上升沿对是否正确
     */
    private void checkPair() {
        Section section = getIncompleteSection();
        int size = section.pairs.size();
        if (size == 1) {
            int peak = section.pairs.get(0).peak;
            if (rPointer - peak > maxPointDistance * 2) {
                section.pairs.remove(0);
            }
        } else if (size == 2) {
            int pp = section.pairs.get(1).peak - section.pairs.get(0).peak;
            if (pp > maxPointDistance * 2) {
                section.pairs.remove(0);
                if (callback != null) {
                    callback.onSectionRemove();
                }
            } else {
                if (!isDownEdgeCorrect(section.pairs.get(0).peak, section.pairs.get(1).valley)) {
                    Log.e(TAG, "checkPair: 下降沿错误1");
                    section.pairs.remove(0);
                }
            }
        } else if (size > 2) {
            int pp = section.pairs.get(size - 1).peak - section.pairs.get(size - 2).peak;
            int ppLast = section.pairs.get(size - 2).peak - section.pairs.get(size - 3).peak;
            if (pp > peakPeakRatio * ppLast || pp > maxPointDistance * peakPeakRatio) {
                sectionComplete(section);
            } else if (!isDownEdgeCorrect(section.pairs.get(size - 2).peak, section.pairs.get(size - 1).valley)) {
                Log.e(TAG, "checkPair: 下降沿错误2");
                sectionComplete(section);
            }
        }
    }


    /**
     * 下降沿是否正确
     *
     * @param peak   下降沿峰的位置
     * @param valley 下降沿谷的位置
     * @return true正确
     */
    private boolean isDownEdgeCorrect(int peak, int valley) {
        short[] maxAndMin = getPeakAndValleyInRegion(peak, valley);
        boolean isOutOfRange = maxAndMin[0] > topRegion || maxAndMin[1] < bottomRegion;
        return !isOutOfRange && !(maxAndMin[0] > rawData.get(peak) ||
                maxAndMin[0] - maxAndMin[1] > edgeHeightDifferRatio * (rawData.get(peak) - rawData.get(valley)));
    }

    /**
     * 一段脉图中的最大最小值
     *
     * @param start 起始
     * @param end   结束
     * @return [0]最大，[1]最小
     */
    private short[] getPeakAndValleyInRegion(int start, int end) {
        short[] result = new short[2];
        for (int i = start; i <= end; i++) {
            if (result[0] == 0) {
                result[0] = rawData.get(i);
            } else if (result[0] < rawData.get(i)) {
                result[0] = rawData.get(i);
            }
            if (result[1] == 0) {
                result[1] = rawData.get(i);
            } else if (result[1] > rawData.get(i)) {
                result[1] = rawData.get(i);
            }
        }
        return result;
    }

    /**
     * 强制打断脉图，如果条件不满足则删除
     */
    private void breakSection() {
        Log.e(TAG, "breakSection: ");
        if (callback != null) {
            callback.onSectionRemove();
        }
        checkPair();
        Section section = getIncompleteSection();
        int size = section.pairs.size();
        if (size - 1 >= sectionMinPwNumber) {
            section.isComplete = true;
        } else {
            resultData.remove(resultData.size() - 1);
        }
    }

    /**
     * 上组脉图结束
     *
     * @param section 需要结束的组
     */
    private void sectionComplete(Section section) {
        breakPointer = section.pairs.get(section.pairs.size() - 2).peak;
        PointPair lastPair = section.pairs.remove(section.pairs.size() - 1);
        section.isComplete = true;
        Section newSection = getIncompleteSection();
        newSection.pairs.add(lastPair);
    }

    @Override
    public void addError() {
        errorOccurred();
    }

    private void errorOccurred() {
        breakPointer = rawData.size() - 1;
        addSection(false);
    }

    @Override
    public short[] getMaxAndMinPoint() {
        int sectionSize = resultData.size();
        short[] maxAndMin = new short[2];
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            if (section.pairs.size() - 1 >= sectionMinPwNumber) {
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

    @Override
    public List<Short> getRawData() {
        List<Short> result = new ArrayList<>();
        for (short i : rawData) {
            result.add(i);
        }
        return result;
    }

    @Override
    public List<Object> getResult() {
        List<Object> result = new ArrayList<>();
        result.add(sampleRate);
        result.add(getTotalHeartRate());
        result.add(65535);
        int size = resultData.size();
        for (int i = 0; i < size; i++) {
            Section section = resultData.get(i);
            int sectionSize = section.pairs.size();
            if (sectionSize - 1 < sectionMinPwNumber) {
                continue;
            }
            int end;
            int start = section.pairs.get(0).peak - marginStart;
            boolean isLastSection = i == size - 1;
            if (isLastSection) {
                end = section.pairs.get(sectionSize - 2).peak;
            } else {
                end = section.pairs.get(sectionSize - 1).peak;
            }
            for (int j = start; j <= end; j++) {
                result.add(rawData.get(j));
            }
            result.add(65535);
            result.add(getSectionHeartRate(section));
            result.add(65535);
        }
        initList();
        return result;
    }

    private float getSectionHeartRate(Section section) {
        int size = section.pairs.size();
        if (size <= 2) {
            return 0;
        }
        return calculateHeartRate(Math.abs(section.pairs.get(1).peak -
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

    @Override
    public int getResultSize() {
        int totalPwNumber = 0;
        int sectionSize = resultData.size();
        for (int i = 0; i < sectionSize; i++) {
            Section section = resultData.get(i);
            int pwNumber = section.pairs.size() - 1;
            if (pwNumber < sectionMinPwNumber) {
                continue;
            }
            if (i == sectionSize - 1) {
                totalPwNumber += pwNumber - 1;
            } else {
                totalPwNumber += pwNumber;
            }
        }
        return totalPwNumber;
    }

    @Override
    public float getTotalHeartRate() {
        float hr = NO_HR;
        if (!resultData.isEmpty()) {
            int totalLength = 0;
            int totalPw = 0;
            for (Section section : resultData) {
                if (section.pairs.size() >= 2) {
                    totalLength += section.pairs.get(section.pairs.size() - 1).peak - section.pairs.get(0).peak;
                    totalPw += section.pairs.size() - 1;
                }
            }
            if (totalPw != 0 && totalLength / totalPw != 0) {
                hr = calculateHeartRate(totalLength / totalPw);
            }
        }
        return hr;
    }

    @Override
    public void setSampleRate(float sampleRate) {
        this.sampleRate = sampleRate;
        this.maxPointDistance = 60 * sampleRate / hrMin;
        this.minPointDistance = 60 * sampleRate / hrMax;
        this.marginStart = Math.round(MARGIN_START_DEFAULT * sampleRate);
        this.maxEdgeWidth = sampleRate * edgeWidthRationMax;
        this.minEdgeWidth = sampleRate * edgeWidthRationMin;
    }

    @Override
    public void setComplete(int completePwMin, int completePwMax) {
        this.completePwMin = completePwMin;
        this.completePwMax = completePwMax;
    }

    @Override
    public void setReservedPwNumber(int reservedNumber) {
    }

    @Override
    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void clear() {
        rawData.clear();
        resultData.clear();
        maxPoints.clear();
    }


    private class Section {
        List<PointPair> pairs;
        boolean isComplete;

        Section() {
            this.pairs = new ArrayList<>();
            this.isComplete = false;
        }
    }

    private class PointPair {
        int peak;
        int valley;

        @Override
        public String toString() {
            return "{" +
                    peak + ", " + valley +
                    '}';
        }

        int getEdgeHeight() {
            return rawData.get(peak) - rawData.get(valley);
        }

        int getEdgeWidth() {
            return peak - valley;
        }
    }
}
