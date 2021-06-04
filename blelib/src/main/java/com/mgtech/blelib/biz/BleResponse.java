package com.mgtech.blelib.biz;

import android.os.Handler;
import android.os.Looper;

import androidx.core.util.Pair;

import com.mgtech.blelib.entity.BroadcastData;
import com.mgtech.blelib.entity.CheckDeviceData;
import com.mgtech.blelib.entity.CurrentStepData;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.entity.FirmStateData;
import com.mgtech.blelib.entity.FirmwareInfoData;
import com.mgtech.blelib.entity.HeightWeightData;
import com.mgtech.blelib.entity.LinkStatusEvent;
import com.mgtech.blelib.entity.LookForBraceletEvent;
import com.mgtech.blelib.entity.ManualEcgData;
import com.mgtech.blelib.entity.ManualEcgError;
import com.mgtech.blelib.entity.ManualPwData;
import com.mgtech.blelib.entity.ManualPwError;
import com.mgtech.blelib.entity.ReminderData;
import com.mgtech.blelib.entity.SampleCompleteData;
import com.mgtech.blelib.entity.StatusDataEvent;
import com.mgtech.blelib.entity.StepHistory;
import com.mgtech.blelib.entity.StopMeasureData;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

public class BleResponse {
    public static final int STATUS = 1;
    public static final int BROADCAST = 2;
    public static final int PW_DATA = 3;
    public static final int PW_MANUAL_COMPLETE = 4;
    public static final int PW_ERROR = 5;
    public static final int MEASURE_STOP = 6;
    public static final int ECG_DATA = 7;
    public static final int ECG_ERROR = 8;
    public static final int DISPLAY = 9;
    public static final int REMIND = 10;
    public static final int HEIGHT_WEIGHT = 11;
    public static final int FIRMWARE_DATA = 12;
    public static final int FIRMWARE_INFO = 13;
    public static final int CHECK_DEVICE = 14;
    public static final int LINK_STATUS = 15;
    public static final int FIND = 16;
    public static final int STEP_HISTORY = 17;
    public static final int STEP_CURRENT = 18;
    public static final int PW_AUTO_COMPLETE = 19;

    private List<AbstractBleResponseCallback> callbacks = new ArrayList<>();
    //    private AbstractBleResponseCallback callback;
    private static Handler handler = new Handler(Looper.getMainLooper());
    private PublishSubject<Pair<Integer,Object>> observable;

    public BleResponse() {
        observable = PublishSubject.create();
    }

    public Subscription setCallback(AbstractBleResponseCallback callback) {
//        this.callback = callback;
//        callbacks.add(callback);
        return observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public void removeCallback(Subscription subscription) {
//        if (callback != this.callback){
//            this.callback = null;
//        }
        if (subscription != null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void status(final StatusDataEvent statusDataEvent) {
//        if (callback != null) {
//            final int status = statusDataEvent.getStatus();
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Logger.i("status"+ statusDataEvent.getStatus() + (callback == null));
//                    if (callback != null) {
//                        callback.onBluetoothStatusReceived(status);
//                    }
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(STATUS,statusDataEvent.getStatus()));
    }

     void broadcast(final BroadcastData broadcastData) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onBroadcastReceived(broadcastData);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(BROADCAST,broadcastData));
    }

     void manualPwData(final ManualPwData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onManualPWDataReceived(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(PW_DATA,data));
    }

     void manualPwComplete(final SampleCompleteData data) {
        observable.onNext(new Pair<Integer, Object>(PW_MANUAL_COMPLETE,data));
    }

    void autoPwComplete(final SampleCompleteData data) {
        observable.onNext(new Pair<Integer, Object>(PW_AUTO_COMPLETE,data));
    }

     void manualPwError(final ManualPwError data) {
        observable.onNext(new Pair<Integer, Object>(PW_ERROR,data));
    }

     void manualStop(StopMeasureData data) {
        observable.onNext(new Pair<Integer, Object>(MEASURE_STOP,data));
    }

     void manualEcgData(final ManualEcgData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onManualECGDataReceived(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(ECG_DATA,data));
    }

     void manualEcgError(final ManualEcgError data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onManualECGErrorDataReceived(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(ECG_ERROR,data));
    }

//    public void warning(WarningData data) {
//        if (callback != null) {
//            callback.onWarningReceived(data.getCode());
//        }
//    }

     void displayPage(final DisplayPage displayPage) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onDisplayPage(displayPage);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(DISPLAY,displayPage));
    }

     void alertReminder(final ReminderData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onAlertReminders(reminderData);
//
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(REMIND,data));
    }

     void heightAndWeight(final HeightWeightData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onHeightAndWeight(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(HEIGHT_WEIGHT,data));
    }

     void firm(final FirmStateData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onFirmwareUpgrade(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(FIRMWARE_DATA,data));
    }

    public void firmwareVersion(final FirmwareInfoData data) {

        observable.onNext(new Pair<Integer, Object>(FIRMWARE_INFO,data));
    }

    public void device(final CheckDeviceData data) {

        observable.onNext(new Pair<Integer, Object>(CHECK_DEVICE,data));
    }


     void blueLinkStatus(final LinkStatusEvent data) {

        observable.onNext(new Pair<Integer, Object>(LINK_STATUS,data.isLink()));
    }

     void blueFindBracelet(final LookForBraceletEvent event) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.findResult(event.getErrorCode());
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(FIND,event.getErrorCode()));
    }

     void currentStepData(final CurrentStepData data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onCurrentStepReceived(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(STEP_CURRENT,data));
    }

     void stepHistory(final StepHistory data) {
//        if (callback != null) {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    callback.onStep(data);
//                }
//            });
//        }
        observable.onNext(new Pair<Integer, Object>(STEP_HISTORY,data));
    }


}
