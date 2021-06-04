package com.mgtech.maiganapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mgtech.maiganapp.R;
import com.mgtech.maiganapp.viewmodel.DefaultViewModel;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author zhaixiang
 * 意见反馈
 */

public class ProtocolActivity extends BaseActivity<DefaultViewModel> {
    @Bind(R.id.tv_protocol)
    TextView tvProtocol;

    public static Intent getCallingIntent(Context context){
        return new Intent(context,ProtocolActivity.class);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        hideActionbar();
        loadText();
    }

    private void loadText() {
        Single.create(new Single.OnSubscribe<String>(){

            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
//                boolean isChinese = "zh".equals(Locale.getDefault().getLanguage());
                Source source = Okio.source(getApplication().getResources().openRawResource(R.raw.user_protocol_ch));
                BufferedSource buffer = Okio.buffer(source);
                try {
                    String content = buffer.readString(Charset.defaultCharset()).replace("\\n","\n");
                    singleSubscriber.onSuccess(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        tvProtocol.setText(s);
                    }
                });
    }

    @OnClick(R.id.btn_back)
    void back(){
        finish();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_setting_protocol;
    }

}
