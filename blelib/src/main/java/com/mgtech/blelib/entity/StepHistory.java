package com.mgtech.blelib.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zhaixiang
 * @date 2017/12/20
 * 计步历史数据
 */

public class StepHistory {
    private int number;
    private List<HourData> hourDataList;


    private StepHistory(Builder builder){
        number = builder.number;
        hourDataList = builder.hourDataList;
    }


    public int getNumber() {
        return number;
    }

    public List<HourData> getHourDataList() {
        return hourDataList;
    }


    @Override
    public String toString() {
        return "StepHistory{" +
                "number=" + number +
                ", hourDataList=" + hourDataList +
                '}';
    }

    public static class HourData {
        private int startHour;
        private int stepInQuarter1;
        private int stepInQuarter2;
        private int stepInQuarter3;
        private int stepInQuarter4;
        private int sportInQuarter1;
        private int sportInQuarter2;
        private int sportInQuarter3;
        private int sportInQuarter4;

        public int getStartHour() {
            return startHour;
        }

        public void setStartHour(int startHour) {
            this.startHour = startHour;
        }

        public int getStepInQuarter1() {
            return stepInQuarter1;
        }

        public void setStepInQuarter1(int stepInQuarter1) {
            this.stepInQuarter1 = stepInQuarter1;
        }

        public int getStepInQuarter2() {
            return stepInQuarter2;
        }

        public void setStepInQuarter2(int stepInQuarter2) {
            this.stepInQuarter2 = stepInQuarter2;
        }

        public int getStepInQuarter3() {
            return stepInQuarter3;
        }

        public void setStepInQuarter3(int stepInQuarter3) {
            this.stepInQuarter3 = stepInQuarter3;
        }

        public int getStepInQuarter4() {
            return stepInQuarter4;
        }

        public void setStepInQuarter4(int stepInQuarter4) {
            this.stepInQuarter4 = stepInQuarter4;
        }

        public int getSportInQuarter1() {
            return sportInQuarter1;
        }

        public void setSportInQuarter1(int sportInQuarter1) {
            this.sportInQuarter1 = sportInQuarter1;
        }

        public int getSportInQuarter2() {
            return sportInQuarter2;
        }

        public void setSportInQuarter2(int sportInQuarter2) {
            this.sportInQuarter2 = sportInQuarter2;
        }

        public int getSportInQuarter3() {
            return sportInQuarter3;
        }

        public void setSportInQuarter3(int sportInQuarter3) {
            this.sportInQuarter3 = sportInQuarter3;
        }

        public int getSportInQuarter4() {
            return sportInQuarter4;
        }

        public void setSportInQuarter4(int sportInQuarter4) {
            this.sportInQuarter4 = sportInQuarter4;
        }

        @Override
        public String toString() {
            return "HourData{" +
                    "startHour=" + startHour +
                    ", stepInQuarter1=" + stepInQuarter1 +
                    ", stepInQuarter2=" + stepInQuarter2 +
                    ", stepInQuarter3=" + stepInQuarter3 +
                    ", stepInQuarter4=" + stepInQuarter4 +
                    ", sportInQuarter1=" + sportInQuarter1 +
                    ", sportInQuarter2=" + sportInQuarter2 +
                    ", sportInQuarter3=" + sportInQuarter3 +
                    ", sportInQuarter4=" + sportInQuarter4 +
                    '}';
        }
    }

    public static class Builder{
        private int number;
        private List<HourData> hourDataList;

        public Builder(){
            hourDataList = new ArrayList<>();
        }

        public void setInProfile330(byte[] data){
            int number = data[2] & 0xFF;
            for (int i = 0; i < number; i++) {
                StepHistory.HourData hourData = new StepHistory.HourData();
                hourData.startHour = (data[3 + i * 20] & 0xFF) + ((data[4 + i * 20] & 0xFF) << 8) +
                        ((data[5 + i * 20] & 0xFF) << 16) + ((data[6 + i * 20] & 0xFF) << 24);
                hourData.stepInQuarter1 = (data[7 + i * 20] & 0xFF) + ((data[8 + i * 20] & 0xFF) << 8) +
                        ((data[9 + i * 20] & 0xFF) << 16) + ((data[10 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter1 = (data[10 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter2 = (data[11 + i * 20] & 0xFF) + ((data[12 + i * 20] & 0xFF) << 8) +
                        ((data[13 + i * 20] & 0xFF) << 16) + ((data[14 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter2 = (data[14 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter3 = (data[15 + i * 20] & 0xFF) + ((data[16 + i * 20] & 0xFF) << 8) +
                        ((data[17 + i * 20] & 0xFF) << 16) + ((data[18 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter3 = (data[18 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter4 = (data[19 + i * 20] & 0xFF) + ((data[20 + i * 20] & 0xFF) << 8) +
                        ((data[21 + i * 20] & 0xFF) << 16) + ((data[22 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter4 = (data[22 + i * 20] >>> 4) & 0xF;
                hourDataList.add(hourData);
            }
            this.number += number;
        }

        public void setInProfile400(byte[] data){
            int number = data[3] & 0xFF;
            for (int i = 0; i < number; i++) {
                StepHistory.HourData hourData = new StepHistory.HourData();
                hourData.startHour = (data[4 + i * 20] & 0xFF) + ((data[5 + i * 20] & 0xFF) << 8) +
                        ((data[6 + i * 20] & 0xFF) << 16) + ((data[7 + i * 20] & 0xFF) << 24);
                hourData.stepInQuarter1 = (data[8 + i * 20] & 0xFF) + ((data[9 + i * 20] & 0xFF) << 8) +
                        ((data[10 + i * 20] & 0xFF) << 16) + ((data[11 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter1 = (data[11 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter2 = (data[12 + i * 20] & 0xFF) + ((data[13 + i * 20] & 0xFF) << 8) +
                        ((data[14 + i * 20] & 0xFF) << 16) + ((data[15 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter2 = (data[15 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter3 = (data[16 + i * 20] & 0xFF) + ((data[17 + i * 20] & 0xFF) << 8) +
                        ((data[18 + i * 20] & 0xFF) << 16) + ((data[19 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter3 = (data[19 + i * 20] >>> 4) & 0xF;

                hourData.stepInQuarter4 = (data[20 + i * 20] & 0xFF) + ((data[21 + i * 20] & 0xFF) << 8) +
                        ((data[22 + i * 20] & 0xFF) << 16) + ((data[23 + i * 20] & 0xF) << 24);
                hourData.sportInQuarter4 = (data[23 + i * 20] >>> 4) & 0xF;
                hourDataList.add(hourData);
            }
            this.number += number;
        }

        public StepHistory create(){
            return new StepHistory(this);
        }
    }
}
