package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.request.MedicineAddEntity;
import com.mgtech.domain.entity.net.request.MedicineDeleteRequestEntity;
import com.mgtech.domain.entity.net.request.MedicineEditRequestEntity;
import com.mgtech.domain.entity.net.response.CustomMedicineEntity;
import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author zhaixiang
 */
public interface MedicationApi {
    @GET(HttpApi.API_GET_ALL_MEDICINE)
    Observable<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> getMedicineList(@Query("accountGuid") String userId);

    @GET(HttpApi.API_GET_MEDICATION_PLAN_LIST)
    Observable<NetResponseEntity<List<MedicationPlanEntity>>> getMedicationPlanList(@Query("accountGuid") String userId);

    @GET(HttpApi.API_GET_COMPLETED_MEDICATION_PLAN_LIST)
    Observable<NetResponseEntity<List<MedicationPlanEntity>>> getCompletedMedicationPlanList(@Query("accountGuid") String userId);

    @POST(HttpApi.API_ADD_MEDICATION_PLAN)
    Observable<NetResponseEntity<MedicationPlanEntity>> addMedicationPlan(@Body MedicationPlanEntity entity);

    @POST(HttpApi.API_UPDATE_MEDICATION_PLAN)
    Observable<NetResponseEntity<MedicationPlanEntity>> updateMedicationPlan(@Body MedicationPlanEntity entity);

    @GET(HttpApi.API_STOP_MEDICATION_PLAN)
    Observable<NetResponseEntity> stopMedicationPlan(@Query("accountGuid") String userId, @Query("planGuid") String planId);

    @GET(HttpApi.API_DELETE_MEDICATION_PLAN)
    Observable<NetResponseEntity> deleteMedicationPlan(@Query("accountGuid") String userId, @Query("planGuid") String planId);

    @GET(HttpApi.API_GET_MEDICATION_REMIND_LIST)
    Observable<NetResponseEntity<MedicationRemindResponseEntity>> getMedicationRemindList(@Query("accountGuid") String userId);

    @GET(HttpApi.API_RECORD_MEDICATION_REMIND)
    Observable<NetResponseEntity> setMedicationRemind(
            @Query("accountGuid") String userId,
            @Query("planGuid") String planId,
            @Query("remindGuid") String remindId,
            @Query("status") int state);

    @POST(HttpApi.API_MEDICINE_ADD)
    Observable<NetResponseEntity<CustomMedicineEntity>> addMedicine(@Body MedicineAddEntity entity);

    @GET(HttpApi.API_MEDICINE_DELETE)
    Observable<NetResponseEntity> deleteMedicine(@Query("drugGuid") String medicineId,@Query("accountGuid") String userId);

    @POST(HttpApi.API_MEDICINE_EDIT)
    Observable<NetResponseEntity<CustomMedicineEntity>> editMedicine(@Body MedicineEditRequestEntity entity);

    @GET(HttpApi.API_MEDICINE_GET_CUSTOM_LIST)
    Observable<NetResponseEntity<List<CustomMedicineEntity>>>getCustomMedicineList(@Query("accountGuid")String userId,@Query("page")int page,
                                                                                   @Query("pageSize")int pageSize);
}
