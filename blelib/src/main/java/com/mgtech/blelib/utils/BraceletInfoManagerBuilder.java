package com.mgtech.blelib.utils;

import android.content.Context;

import com.mgtech.blelib.biz.IBraceletInfoManager;

/**
 *
 * @author zhaixiang
 * @date 2018/1/16
 * 生成 默认的 IBraceletInfoManager
 */

public class BraceletInfoManagerBuilder {
    private IBraceletInfoManager manager;
    private Context context;

    public BraceletInfoManagerBuilder(Context context) {
        this.context = context;
    }

    private void setManager(IBraceletInfoManager manager){
        this.manager = manager;
    }

    public IBraceletInfoManager create(){
        if (manager == null){
            manager = new BraceletInfoMMKVManager(context);
        }
        return manager;
    }
}
