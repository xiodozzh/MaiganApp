package com.mgtech.maiganapp.data.model;

import java.util.Arrays;

public class PwExampleItem {
    private String text;
    private int mainPicResource;
    private String[] description;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMainPicResource() {
        return mainPicResource;
    }

    public void setMainPicResource(int mainPicResource) {
        this.mainPicResource = mainPicResource;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PwExampleItem{" +
                "text='" + text + '\'' +
                ", mainPicResource=" + mainPicResource +
                ", description=" + Arrays.toString(description) +
                '}';
    }
}
