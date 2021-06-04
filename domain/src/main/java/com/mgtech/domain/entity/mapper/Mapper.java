package com.mgtech.domain.entity.mapper;

import com.mgtech.domain.entity.CleanIndicator;
import com.mgtech.domain.entity.net.response.IndicatorItemResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorMap;
import com.mgtech.domain.entity.net.response.ResultEntity;
import com.mgtech.domain.entity.net.response.SaveRawDataResponseItemEntity;
import com.mgtech.domain.utils.MyConstant;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaixiang on 2017/2/7.
 * 类型转换
 */

public class Mapper {
    /**
     * 将 IndicatorMap 转化为 CleanIndicator
     *
     * @param indicatorMap IndicatorMap
     * @return CleanIndicator
     */
    public static CleanIndicator indicatorMapToClean(IndicatorMap indicatorMap) {
        CleanIndicator indicator = new CleanIndicator();
        Calendar date = indicatorMap.getDate();
        if (date != null) {
            indicator.setTime(date.getTimeInMillis());
        }
        if (indicatorMap.getRowId() != null) {
            indicator.setId(indicatorMap.getRowId());
        }
        if (indicatorMap.getIsSport() != null) {
            try {
                int sport = Integer.parseInt(indicatorMap.getIsSport());
                indicator.setIsSport(sport);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Map<String, IndicatorItemResponseEntity> map = indicatorMap.getMap();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, IndicatorItemResponseEntity> entry : map.entrySet()) {
                IndicatorItemResponseEntity entity = entry.getValue();
                if (entity == null) {
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_AC)) {
                    indicator.setAc(entity.getValue());
                    indicator.setAcJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_ALK)) {
                    indicator.setAlk(entity.getValue());
                    indicator.setAlkJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_ALT)) {
                    indicator.setAlt(entity.getValue());
                    indicator.setAltJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_BV)) {
                    indicator.setBv(entity.getValue());
                    indicator.setBvJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_CO)) {
                    indicator.setCo(entity.getValue());
                    indicator.setCoJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_HR)) {
                    indicator.setHr(entity.getValue());
                    indicator.setHrJudge(entity.getResultDescribe());
                }
                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_PD)) {
                    indicator.setPd(entity.getValue());
                    indicator.setPdJudge(entity.getResultDescribe());
                }
                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_PM)) {
                    indicator.setPm(entity.getValue());
                    indicator.setPmJudge(entity.getResultDescribe());
                }
                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_PS)) {
                    indicator.setPs(entity.getValue());
                    indicator.setPsJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_SI)) {
                    indicator.setSi(entity.getValue());
                    indicator.setSiJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_SV)) {
                    indicator.setSv(entity.getValue());
                    indicator.setSvJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_TM)) {
                    indicator.setTm(entity.getValue());
                    indicator.setTmJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_TPR)) {
                    indicator.setTpr(entity.getValue());
                    indicator.setTprJudge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_V0)) {
                    indicator.setV0(entity.getValue());
                    indicator.setV0Judge(entity.getResultDescribe());
                }

                if (entry.getKey().equalsIgnoreCase(MyConstant.NAME_K)) {
                    indicator.setK(entity.getValue());
                    indicator.setkJudge(entity.getResultDescribe());
                }
            }
        }
        return indicator;
    }

    public static CleanIndicator indicatorMapToClean(ResultEntity<SaveRawDataResponseItemEntity> resultEntity) {
        CleanIndicator indicator = new CleanIndicator();
        indicator.setResult(resultEntity.getResult());
        indicator.setMessage(resultEntity.getMessage());
        List<SaveRawDataResponseItemEntity> list = resultEntity.getData();
        if (list == null){
            return indicator;
        }
        for (SaveRawDataResponseItemEntity entity : list) {
            if (entity == null) {
                continue;
            }
            String name = entity.getName();
            if (name == null || name.isEmpty()) {
                continue;
            }
            if (name.equalsIgnoreCase(MyConstant.NAME_AC)) {
                indicator.setAc(entity.getValue());
                indicator.setAcJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_ALK)) {
                indicator.setAlk(entity.getValue());
                indicator.setAlkJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_ALT)) {
                indicator.setAlt(entity.getValue());
                indicator.setAltJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_BV)) {
                indicator.setBv(entity.getValue());
                indicator.setBvJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_CO)) {
                indicator.setCo(entity.getValue());
                indicator.setCoJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_HR)) {
                indicator.setHr(entity.getValue());
                indicator.setHrJudge(entity.getResultDescribe());
            }
            if (name.equalsIgnoreCase(MyConstant.NAME_PD)) {
                indicator.setPd(entity.getValue());
                indicator.setPdJudge(entity.getResultDescribe());
            }
            if (name.equalsIgnoreCase(MyConstant.NAME_PM)) {
                indicator.setPm(entity.getValue());
                indicator.setPmJudge(entity.getResultDescribe());
            }
            if (name.equalsIgnoreCase(MyConstant.NAME_PS)) {
                indicator.setPs(entity.getValue());
                indicator.setPsJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_SI)) {
                indicator.setSi(entity.getValue());
                indicator.setSiJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_SV)) {
                indicator.setSv(entity.getValue());
                indicator.setSvJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_TM)) {
                indicator.setTm(entity.getValue());
                indicator.setTmJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_TPR)) {
                indicator.setTpr(entity.getValue());
                indicator.setTprJudge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_V0)) {
                indicator.setV0(entity.getValue());
                indicator.setV0Judge(entity.getResultDescribe());
            }

            if (name.equalsIgnoreCase(MyConstant.NAME_K)) {
                indicator.setK(entity.getValue());
                indicator.setkJudge(entity.getResultDescribe());
            }
        }
        return indicator;
    }

}
