package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.request.BindDeviceRequestEntity;
import com.mgtech.domain.entity.net.request.SetBraceletInfoRequestEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.CheckBraceletResponseEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhaixiang
 */
public interface DeviceApi {
    /**
     * 检查手环是否正版
     *
     * @param deviceId 手环id
     * @param mac      手环mac
     * @return 是否正版
     */
    @GET(HttpApi.API_CHECK_BRACELET_COPYRIGHT)
    Observable<NetResponseEntity<CheckBraceletResponseEntity>> checkBraceletCopyright(@Query("deviceGuid") String deviceId,
                                                                                      @Query("macAddress") String mac);

    /**
     * 绑定手环
     *
     * @param entity 绑定信息
     * @return 绑定结果
     */
    @POST(HttpApi.API_BIND_BRACELET)
    Observable<NetResponseEntity<BraceletConfigEntity>> bindDevice(@Body BindDeviceRequestEntity entity);

    /**
     * 解绑手环
     *
     * @param mac 手环mac
     * @return 结果
     */
    @GET(HttpApi.API_UNBIND_BRACELET)
    Observable<NetResponseEntity> unbindDevice(@Query("macAddress") String mac,@Query("accountGuid")String id);

    /**
     * 获取手环绑定信息
     *
     * @param id 用户id
     * @return 手环列表
     */
    @GET(HttpApi.API_GET_BRACELET)
    Observable<NetResponseEntity<GetBindInfoResponseEntity>> getBracelet(@Query("type") int type,@Query("accountGuid")String id);

    /**
     * 获取手环配置信息
     *
     * @param type 手环类型
     * @return 手环配置信息
     */
    @GET(HttpApi.API_GET_BRACELET_CONFIG)
    Observable<NetResponseEntity<BraceletConfigEntity>> getConfig(@Query("type") int type,@Query("accountGuid")String id);

    /**
     * 保存手环配置信息
     *
     * @param entity 配置信息
     * @return 结果
     */
    @POST(HttpApi.API_SET_BRACELET_CONFIG)
    Observable<NetResponseEntity> setConfig(@Body BraceletConfigEntity entity);


    @POST(HttpApi.API_SET_BRACELET_INFORMATION)
    Observable<NetResponseEntity> setInfo(@Body SetBraceletInfoRequestEntity entity);
}
