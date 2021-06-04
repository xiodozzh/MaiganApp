package com.mgtech.blelib.entity;

public class PowerData {
    private int state;
    private int power;

    public PowerData(int state, int power) {
        this.state = state;
        this.power = power;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
