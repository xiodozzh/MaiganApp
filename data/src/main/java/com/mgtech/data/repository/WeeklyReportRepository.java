package com.mgtech.data.repository;

import android.content.Context;

import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.data.net.retrofit.WeeklyReportApi;
import com.mgtech.domain.entity.net.request.GetWeeklyReportRequestEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import rx.Single;

public class WeeklyReportRepository implements NetRepository.WeeklyReport {
    private Context context;
    private WeeklyReportApi api;

    public WeeklyReportRepository(Context context) {
        this.context = context;
        this.api = new RetrofitFactory()
                .setUseToken(true)
                .setBaseUrl(HttpApi.API_BASE_URL)
                .build(context)
                .create(WeeklyReportApi.class);
    }

    @Override
    public Single<NetResponseEntity<List<WeeklyReportResponseEntity>>> getWeeklyReportList(GetWeeklyReportRequestEntity entity,
                                                                                           int cacheType) {
        return api.getWeeklyReportList(entity.getId(),entity.getPwDataType()).doOnError(new DoOnTokenErrorAction());
    }
}
