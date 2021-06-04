package com.mgtech.maiganapp.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.databinding.ObservableField;

import com.mgtech.domain.entity.CleanIndicator;
import com.mgtech.domain.entity.IndicatorDetailItem;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.domain.utils.MyConstant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhaixiang on 2017/4/27.
 * 截图 view-model
 */

public class CaptureViewModel extends BaseViewModel {
    private Context context;
    private CleanIndicator indicator;
    public List<IndicatorDetailItem> data;
    public long time;
    public final ObservableField<String> title = new ObservableField<>("");

    public ObservableField<Boolean> judged = new ObservableField<>(false);

    public CaptureViewModel(Application context) {
        super(context);
        this.context = context;
        this.data = new ArrayList<>();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            IndicatorDetailItem item = new IndicatorDetailItem();
            item.setType(i);
//            item.setTitle(IndicatorUtils.getTitle(context,i));
//            item.setUnit(IndicatorUtils.getUnit(context,i));
            this.data.add(item);
        }
        String name = UserInfo.getLocalUserInfo(context).getName();
        title.set(name);
    }

    public void setIndicator(CleanIndicator indicator) {
        this.indicator = indicator;
        this.time = indicator.getTime();
        if (time == 0){
            time = Calendar.getInstance().getTimeInMillis();
        }
        data.get(MyConstant.RANK_HR).setValue(indicator.getHr());
        data.get(MyConstant.RANK_PS).setValue(indicator.getPs());
        data.get(MyConstant.RANK_PD).setValue(indicator.getPd());
        data.get(MyConstant.RANK_PM).setValue(indicator.getPm());
        data.get(MyConstant.RANK_SV).setValue(indicator.getSv());
        data.get(MyConstant.RANK_SI).setValue(indicator.getSi());
        data.get(MyConstant.RANK_CO).setValue(indicator.getCo());
        data.get(MyConstant.RANK_TPR).setValue(indicator.getTpr());
        data.get(MyConstant.RANK_AC).setValue(indicator.getAc());
        data.get(MyConstant.RANK_ALK).setValue(indicator.getAlk());
        data.get(MyConstant.RANK_ALT).setValue(indicator.getAlt());
        data.get(MyConstant.RANK_TM).setValue(indicator.getTm());
        data.get(MyConstant.RANK_BV).setValue(indicator.getBv());
        data.get(MyConstant.RANK_V0).setValue(indicator.getV0());
        data.get(MyConstant.RANK_K).setValue(indicator.getK());
        for (int i = 0; i < 15; i++) {
//            data.get(i).setLevel(IndicatorUtils.getLevel(i,data.get(i).getValue()));
        }
        judged.set(!judged.get());
    }
}
