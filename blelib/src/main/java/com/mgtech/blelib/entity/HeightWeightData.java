package com.mgtech.blelib.entity;

public class HeightWeightData {
    private int height = 160;
    private int weight = 100;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "HeightWeightData{" +
                "height=" + height +
                ", weight=" + weight +
                '}';
    }
}
