package com.mgtech.maiganapp.widget;

import android.os.Handler;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.concurrent.BlockingQueue;

/**
 * Created by zhaixiang on 2016/5/23.
 *
 */
public class GraphDrawingRunnable implements Runnable {
    private static final int TIME = 200;
    private LineGraphSeries<DataPoint> series;
    private BlockingQueue<Integer> queue;
    private int dataPointNr;
    private boolean drawing;
    private Handler handler;

    public GraphDrawingRunnable(LineGraphSeries<DataPoint> series, BlockingQueue<Integer> queue, Handler handler) {
        this.series = series;
        this.queue = queue;
        this.handler = handler;
    }

    @Override
    public void run() {

        if (!queue.isEmpty()) {
            int time = Math.min(queue.size(), TIME);
            for (int i = 0; i < time; i++) {
                draw();
            }
            handler.postDelayed(this, 50);
        } else {
            drawing = false;
        }
    }

    private void draw() {
        series.appendData(new DataPoint(dataPointNr, queue.poll()), true, 600);
        dataPointNr++;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean isDrawing) {
        this.drawing = isDrawing;
    }

    public void reset() {
        this.drawing = false;
        dataPointNr = 0;
        DataPoint[] dataPoints = new DataPoint[600];
        for (int i = 0; i < dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(-1,-1);
        }
        series.resetData(dataPoints);
    }

}
