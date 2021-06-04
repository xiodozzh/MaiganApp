package com.mgtech.maiganapp.activity;

import java.util.Calendar;

public interface HistoryActivityCallback {
    void back();

    void goToGraph();

    void goToList();

    void setCalendar(Calendar calendar);

    Calendar getCalendar();

    String getUserId();

    boolean isMan();
}
