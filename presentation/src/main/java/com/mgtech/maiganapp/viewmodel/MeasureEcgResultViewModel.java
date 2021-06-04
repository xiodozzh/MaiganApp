package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.annotation.NonNull;
import android.text.format.DateFormat;

import com.mgtech.domain.utils.MyConstant;
import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.data.model.EcgModel;

/**
 * @author zhaixiang
 */
public class MeasureEcgResultViewModel extends BaseViewModel {
    public final ObservableField<String> resultString = new ObservableField<>("");
    public final ObservableField<String> timeString = new ObservableField<>("");
    public final ObservableField<String> hrString = new ObservableField<>("");
    public final ObservableField<String> walkSpeedString = new ObservableField<>("");
    public final ObservableBoolean resultDataLoad = new ObservableBoolean(false);
    public float[] ecgData;
    public EcgModel model;

    public MeasureEcgResultViewModel(@NonNull Application application) {
        super(application);
    }


    public void setModel(EcgModel model){
        this.model = model;
        timeString.set(DateFormat.format(MyConstant.FULL_DATETIME_FORMAT,model.measureTime).toString());
        resultString.set(model.ecgResult == 0 ?getApplication().getString(R.string.ecg_normal):
                getApplication().getString(R.string.ecg_abnormal));
        ecgData = model.ecgData;
        if (model.hr != 0) {
            hrString.set(String.valueOf(Math.round(model.hr)));
        }else{
            hrString.set("-");
        }
        if (model.sampleRate != 0){
            walkSpeedString.set("25");
        }else{
            walkSpeedString.set("-");
        }
        resultDataLoad.set(!resultDataLoad.get());
    }


}
