package com.mgtech.maiganapp.window;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.mgtech.blelib.biz.IBraceletInfoManager;
import com.mgtech.blelib.entity.DisplayPage;
import com.mgtech.blelib.utils.BraceletInfoManagerBuilder;
import com.mgtech.domain.entity.UserInfo;
import com.mgtech.maiganapp.R;

import java.util.Locale;

import static com.mgtech.blelib.entity.DisplayPage.CHINESE;
import static com.mgtech.blelib.entity.DisplayPage.ENGLISH;
import static com.mgtech.blelib.entity.DisplayPage.OFF;
import static com.mgtech.blelib.entity.DisplayPage.ON;


/**
 *
 * @author zhaixiang
 * @date 2018/1/5
 * 设置手环显示页面
 */

public class SetDisplayConfigPopupWindow extends PopupWindow {
    private CheckBox cbPower;
    private CheckBox cbHeat;
    private CheckBox cbDistance;
    private CheckBox cbStep;
    private CheckBox cbBp;
    private CheckBox cbV0;
    private CheckBox cbTime;


    public interface Callback {
        void submit(DisplayPage data);
    }

    public SetDisplayConfigPopupWindow(final Context context, final Callback callback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window_set_bracelet_display_config, null);
        cbBp = view.findViewById(R.id.sw_bp);
        cbHeat = view.findViewById(R.id.sw_heat);
        cbDistance = view.findViewById(R.id.sw_distance);
        cbStep = view.findViewById(R.id.sw_step);
        cbPower = view.findViewById(R.id.sw_power);
        cbTime = view.findViewById(R.id.sw_date);
        cbV0 = view.findViewById(R.id.sw_v0);
        initCheckbox(context);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayPage data = getConfig(context);
                callback.submit(data);
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //实例化一个ColorDrawable颜色为半透明
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popup_window);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private DisplayPage getConfig(Context context) {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        DisplayPage data = manager.getDisplayPage();
        data.setPowerPageDisplay(ON);
        data.setHeatPageDisplay(cbHeat.isChecked() ? ON : OFF);
        data.setDistancePageDisplay(cbDistance.isChecked() ? ON : OFF);
        data.setStepPageDisplay(cbStep.isChecked() ? ON : OFF);
        data.setBpPageDisplay(cbBp.isChecked() ? ON : OFF);
        data.setV0PageDisplay(cbV0.isChecked() ? ON : OFF);
        int lang = "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? CHINESE : ENGLISH;
        data.setLang(lang);
        return data;
    }


    private void initCheckbox(Context context) {
        IBraceletInfoManager manager = new BraceletInfoManagerBuilder(context).create();
        DisplayPage data = manager.getDisplayPage();
        Log.i("tag", "initCheckbox: "+ data);
        cbTime.setChecked(data.getDatePageDisplay() == ON);
        cbHeat.setChecked(data.getHeatPageDisplay() == ON);
        cbPower.setChecked(data.getPowerPageDisplay() == ON);
        cbBp.setChecked(data.getBpPageDisplay() == ON);
        cbV0.setChecked(data.getV0PageDisplay() == ON);
        cbDistance.setChecked(data.getDistancePageDisplay() == ON);
        cbStep.setChecked(data.getStepPageDisplay() == ON);
    }
}
