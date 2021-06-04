package com.mgtech.blelib.biz;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.mgtech.blelib.entity.BluetoothOrder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncBleCenter implements IBizController {
    private BleCenter bleCenter;
    private ExecutorService service;
    private Handler handler;

    public AsyncBleCenter(Context context) {
        bleCenter = new BleCenter(context);
        service = Executors.newSingleThreadExecutor();
//        HandlerThread thread = new HandlerThread("bleCenter");
//        thread.start();
//        handler = new Handler(thread.getLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        };
    }

    @Override
    public boolean linkIfAvailable() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.linkIfAvailable();
            }
        });
        return false;
    }

    @Override
    public void pairInit() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.pairInit();
            }
        });
    }

    @Override
    public void link() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.link();
            }
        });
    }

    @Override
    public void pair() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.pair();
            }
        });
    }

    @Override
    public void disconnect() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.disconnect();
            }
        });
    }

    @Override
    public void stopScan() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.stopScan();
            }
        });
    }

    @Override
    public void disconnectIfNotWorking() {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.disconnectIfNotWorking();
            }
        });
    }

    @Override
    public void addOrder(final BluetoothOrder order) {
        service.execute(new Runnable() {
            @Override
            public void run() {
                bleCenter.addOrder(order);
            }
        });
    }

    @Override
    public BleResponse getResponse() {
        return bleCenter.getResponse();
    }

    @Override
    public BleRequest getRequest() {
        return bleCenter.getRequest();
    }

    @Override
    public BleState getState() {
        return bleCenter.getState();
    }
}
