package com.mgtech.domain.rx;

import com.mgtech.domain.exception.RefreshTokenErrorException;

import org.greenrobot.eventbus.EventBus;

import rx.functions.Action1;

/**
 * @author zhaixiang
 */
public class DoOnTokenErrorAction implements Action1<Throwable> {
    @Override
    public void call(Throwable e) {
        if (e instanceof RefreshTokenErrorException){
            EventBus.getDefault().postSticky(new GoToLoginEvent());
        }
    }
}
