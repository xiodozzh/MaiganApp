package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.SaveEcgRequestEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhaixiang
 */
public interface EcgDataApi {
    /**
     * 保存ecg数据
     *
     * @param entity ecg数据
     * @return 保存结果
     */
    @POST(HttpApi.API_SAVE_ECG)
    Observable<NetResponseEntity<EcgResponseEntity>> saveEcg(@Body SaveEcgRequestEntity entity);

    /**
     * 获取ecg列表
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     * @return 结果列表
     */
    @GET(HttpApi.API_GET_ECG_LIST_BY_DATE)
    Observable<NetResponseEntity<List<EcgResponseEntity>>> getEcgList(@Query("startTime") long startTime,
                                                                      @Query("endTime") long endTime,
                                                                      @Query("accountGuid") String id,
                                                                      @Query("rawDataLength") int dataLength);

    /**
     * 根据id获取ecg详细数据
     *
     * @return ecg详情
     */
    @GET(HttpApi.API_GET_ECG_BY_ID)
    Observable<NetResponseEntity<EcgResponseEntity>> getEcgById(@Query("measureGuid") String measureId,
                                                                @Query("accountGuid") String userId);

    /**
     * 删除一条ecg数据
     *
     * @param id ecg数据
     * @return 删除结果
     */
    @GET(HttpApi.API_DELETE_ECG)
    Observable<NetResponseEntity> deleteEcg(@Query("measureGuid") String id);
}
