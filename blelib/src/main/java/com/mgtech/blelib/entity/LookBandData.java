package com.mgtech.blelib.entity;

public class LookBandData {
    public static final int OFF = 0;
    public static final int SCREEN_ON = 1;
    public static final int VIBRATE_ON = 1;

    private int screen;
    private int vibrate;

    public LookBandData(int screen, int vibrate) {
        this.screen = screen;
        this.vibrate = vibrate;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    @Override
    public String toString() {
        return "LookBandData{" +
                "screenOn=" + screen +
                ", vibrateOn=" + vibrate +
                '}';
    }
}
