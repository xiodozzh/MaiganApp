package com.mgtech.maiganapp.data.wrapper;

import android.text.TextUtils;

import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.maiganapp.data.model.IndicatorStatisticDataModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author zhaixiang
 */
public class IndicatorStatisticDataWrapper {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static IndicatorStatisticDataModel getModelFromPwStatisticDataResponseEntity(PwStatisticDataResponseEntity entity) {
        IndicatorStatisticDataModel model = new IndicatorStatisticDataModel();
        PwStatisticDataResponseEntity.ScoreBean scoreBean = entity.getScore();
        if (scoreBean != null) {
            model.hrScore = scoreBean.getHr();
            model.psScore = scoreBean.getPs();
            model.pdScore = scoreBean.getPd();
            model.v0Score = scoreBean.getV0();
            model.coScore = scoreBean.getCo();
            model.tprScore = scoreBean.getTpr();
            model.pmScore = scoreBean.getPm();
        }
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        PwStatisticDataResponseEntity.Param param = entity.getParamInfos();
        if (param != null) {
            List<PwStatisticDataResponseEntity.ShortPwBean> hrList = param.getHr();
            List<PwStatisticDataResponseEntity.ShortPwBean> psList = param.getPs();
            List<PwStatisticDataResponseEntity.ShortPwBean> pdList = param.getPd();
            List<PwStatisticDataResponseEntity.ShortPwBean> coList = param.getCo();
            List<PwStatisticDataResponseEntity.ShortPwBean> v0List = param.getV0();
            List<PwStatisticDataResponseEntity.ShortPwBean> pmList = param.getPm();
            List<PwStatisticDataResponseEntity.ShortPwBean> tprList = param.getTpr();
            Calendar calendar = Calendar.getInstance();
            DataComparator comparator = new DataComparator();
            if (hrList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : hrList) {
                    model.hrList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.hrList, comparator);
            }

            if (psList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : psList) {
                    model.psList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.psList, comparator);

            }

            if (pdList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : pdList) {
                    model.pdList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.pdList, comparator);

            }

            if (pmList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : pmList) {
                    model.pmList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.pmList, comparator);

            }

            if (tprList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : tprList) {
                    model.tprList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.tprList, comparator);

            }

            if (v0List != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : v0List) {
                    model.v0List.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.v0List, comparator);

            }

            if (coList != null) {
                for (PwStatisticDataResponseEntity.ShortPwBean bean : coList) {
                    model.coList.add(getDataFromBean(bean, dateFormat, calendar));
                }
                Collections.sort(model.coList, comparator);
            }


        }
        return model;
    }

    private static IndicatorStatisticDataModel.Data getDataFromBean(PwStatisticDataResponseEntity.ShortPwBean bean,
                                                                    DateFormat dateFormat, Calendar calendar) {
        IndicatorStatisticDataModel.Data data = new IndicatorStatisticDataModel.Data();
        data.time = bean.getDate();
        data.value = bean.getValue();
        return data;
    }

    private static class DataComparator implements Comparator<IndicatorStatisticDataModel.Data> {
        @Override
        public int compare(IndicatorStatisticDataModel.Data o1, IndicatorStatisticDataModel.Data o2) {
            return Long.compare(o1.time, o2.time);
        }
    }
}
