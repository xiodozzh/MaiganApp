package com.mgtech.domain.repository;


import com.mgtech.domain.entity.net.MedicationPlanEntity;
import com.mgtech.domain.entity.net.PersonalInfoEntity;
import com.mgtech.domain.entity.net.RequestEntity;
import com.mgtech.domain.entity.net.request.BindAccountRequestEntity;
import com.mgtech.domain.entity.net.request.BindDeviceRequestEntity;
import com.mgtech.domain.entity.net.request.CheckBraceletRequestEntity;
import com.mgtech.domain.entity.net.request.CheckPhoneInfoRequestEntity;
import com.mgtech.domain.entity.net.request.CheckPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.CheckServiceAuthorRequestEntity;
import com.mgtech.domain.entity.net.request.DealRelationRequestEntity;
import com.mgtech.domain.entity.net.request.DeleteEcgRequestEntity;
import com.mgtech.domain.entity.net.request.DeleteExceptionEntity;
import com.mgtech.domain.entity.net.request.DeletePwRequestEntity;
import com.mgtech.domain.entity.net.request.GetAllExceptionRequestEntity;
import com.mgtech.domain.entity.net.request.GetAppConfigRequestEntity;
import com.mgtech.domain.entity.net.request.GetBraceletConfigRequestEntity;
import com.mgtech.domain.entity.net.request.GetCustomMedicineRequestEntity;
import com.mgtech.domain.entity.net.request.GetDeviceBindInfoRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgListRequestEntity;
import com.mgtech.domain.entity.net.request.GetEcgRequestEntity;
import com.mgtech.domain.entity.net.request.GetFriendDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetFriendInfoRequestEntity;
import com.mgtech.domain.entity.net.request.GetHealthManagementDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetHomeDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetRelationRequestEntity;
import com.mgtech.domain.entity.net.request.GetRelationshipEntity;
import com.mgtech.domain.entity.net.request.GetCompanyAuthorCodeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRangeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStatisticPwByDateRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepStatisticsDataByTimeRequestEntity;
import com.mgtech.domain.entity.net.request.GetStepStatisticsDataRequestEntity;
import com.mgtech.domain.entity.net.request.GetUnreadMessageRequestEntity;
import com.mgtech.domain.entity.net.request.GetUserInfoRequestEntity;
import com.mgtech.domain.entity.net.request.GetVerificationCodeEntity;
import com.mgtech.domain.entity.net.request.GetWeeklyReportRequestEntity;
import com.mgtech.domain.entity.net.request.InviteFriendRequestEntity;
import com.mgtech.domain.entity.net.request.LoginByOtherAppRequestEntity;
import com.mgtech.domain.entity.net.request.LoginByPhoneRequestEntity;
import com.mgtech.domain.entity.net.request.MarkHealthKnowledgeReadRequestEntity;
import com.mgtech.domain.entity.net.request.MarkWeeklyReportReadRequestEntity;
import com.mgtech.domain.entity.net.request.MedicineAddEntity;
import com.mgtech.domain.entity.net.request.MedicineDeleteRequestEntity;
import com.mgtech.domain.entity.net.request.MedicineEditRequestEntity;
import com.mgtech.domain.entity.net.request.RefreshTokenRequestEntity;
import com.mgtech.domain.entity.net.request.RegisterRequestEntity;
import com.mgtech.domain.entity.net.request.RemindFriendMeasureRequestEntity;
import com.mgtech.domain.entity.net.request.RemoveRelationRequestEntity;
import com.mgtech.domain.entity.net.request.ResetPasswordRequestEntity;
import com.mgtech.domain.entity.net.request.SaveEcgRequestEntity;
import com.mgtech.domain.entity.net.request.SaveMobileInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SavePushIdRequestEntity;
import com.mgtech.domain.entity.net.request.SaveRawDataRequestEntity;
import com.mgtech.domain.entity.net.request.SaveStepRequestEntity;
import com.mgtech.domain.entity.net.request.SearchContactRequestEntity;
import com.mgtech.domain.entity.net.request.SearchPermissionRequestEntity;
import com.mgtech.domain.entity.net.request.SendRelationRequestRequestEntity;
import com.mgtech.domain.entity.net.request.SetBraceletInfoRequestEntity;
import com.mgtech.domain.entity.net.request.SetCustomerContactPermissionRequestEntity;
import com.mgtech.domain.entity.net.request.SetExceptionReadRequestEntity;
import com.mgtech.domain.entity.net.request.SetMedicationPlanRequestEntity;
import com.mgtech.domain.entity.net.request.SetMedicationRemindRequestEntity;
import com.mgtech.domain.entity.net.request.SetPwMarkRequestEntity;
import com.mgtech.domain.entity.net.request.SetRelationNoteNameRequestEntity;
import com.mgtech.domain.entity.net.request.UnbindBraceletRequestEntity;
import com.mgtech.domain.entity.net.request.UploadContactRequestEntity;
import com.mgtech.domain.entity.net.request.UserIdRequestEntity;
import com.mgtech.domain.entity.net.request.VerifyRequestEntity;
import com.mgtech.domain.entity.net.response.AllDiseaseEntity;
import com.mgtech.domain.entity.net.response.BindAccountResponseEntity;
import com.mgtech.domain.entity.net.response.BoughtServiceResponseEntity;
import com.mgtech.domain.entity.net.response.BraceletConfigEntity;
import com.mgtech.domain.entity.net.response.CheckBraceletResponseEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneInfoResponseEntity;
import com.mgtech.domain.entity.net.response.CheckPhoneLoginResponseEntity;
import com.mgtech.domain.entity.net.response.CheckServiceAuthorityResponseEntity;
import com.mgtech.domain.entity.net.response.ContactBean;
import com.mgtech.domain.entity.net.response.CountryCodeEntity;
import com.mgtech.domain.entity.net.response.CustomMedicineEntity;
import com.mgtech.domain.entity.net.response.EcgResponseEntity;
import com.mgtech.domain.entity.net.response.ExceptionResponseEntity;
import com.mgtech.domain.entity.net.response.FriendDataResponseEntity;
import com.mgtech.domain.entity.net.response.GetAllUnreadMessageResponseEntity;
import com.mgtech.domain.entity.net.response.GetAppConfigResponseEntity;
import com.mgtech.domain.entity.net.response.GetAuthorizedCompanyResponseEntity;
import com.mgtech.domain.entity.net.response.GetBindInfoResponseEntity;
import com.mgtech.domain.entity.net.response.GetCustomerContactPermissionResponseEntity;
import com.mgtech.domain.entity.net.response.GetFirstAidPhoneResponseEntity;
import com.mgtech.domain.entity.net.response.GetHealthManagementResponseEntity;
import com.mgtech.domain.entity.net.response.GetHomeDataResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceAuthCodeResponseEntity;
import com.mgtech.domain.entity.net.response.HealthKnowledgeResponseEntity;
import com.mgtech.domain.entity.net.response.IndicatorDescriptionResponseEntity;
import com.mgtech.domain.entity.net.response.LoginResponseEntity;
import com.mgtech.domain.entity.net.response.MedicationRemindResponseEntity;
import com.mgtech.domain.entity.net.response.MedicineResponseEntity;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.entity.net.response.ProvinceCodeEntity;
import com.mgtech.domain.entity.net.response.PwDataResponseEntity;
import com.mgtech.domain.entity.net.response.PwStatisticDataResponseEntity;
import com.mgtech.domain.entity.net.response.RefreshTokenResponse;
import com.mgtech.domain.entity.net.response.RegisterResponseEntity;
import com.mgtech.domain.entity.net.response.RelationRequestResponseEntity;
import com.mgtech.domain.entity.net.response.RelationshipResponseEntity;
import com.mgtech.domain.entity.net.response.SearchContactResponseEntity;
import com.mgtech.domain.entity.net.response.GetServiceResponseEntity;
import com.mgtech.domain.entity.net.response.SearchPermissionsResponseEntity;
import com.mgtech.domain.entity.net.response.ServiceCompanyResponseEntity;
import com.mgtech.domain.entity.net.response.StepDetailItemEntity;
import com.mgtech.domain.entity.net.response.StepStatisticsItemEntity;
import com.mgtech.domain.entity.net.response.ThresholdRequestEntity;
import com.mgtech.domain.entity.net.response.WXGetInfoResponseEntity;
import com.mgtech.domain.entity.net.response.WXLoginResponseEntity;
import com.mgtech.domain.entity.net.response.WeeklyReportResponseEntity;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.Single;

/**
 * Created by zhaixiang on 2016/6/30.
 * ???net?????????Repository
 */
public interface NetRepository {
    interface StringNet {
        Observable<String> getFromNet(RequestEntity entity, int cacheType);
    }

    interface Account {
        /**
         * ??????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Single<NetResponseEntity<LoginResponseEntity>> login(LoginByPhoneRequestEntity entity);

        Single<NetResponseEntity<LoginResponseEntity>> loginByOther(LoginByOtherAppRequestEntity entity);

        Single<NetResponseEntity<BindAccountResponseEntity>> bindAccount(BindAccountRequestEntity entity);

        /**
         * ??????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity<RegisterResponseEntity>> register(RegisterRequestEntity entity);


        /**
         * ????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity> verify(VerifyRequestEntity entity);

        /**
         * ????????????
         *
         * @param entity ?????????
         * @return ??????
         */
        Observable<NetResponseEntity> resetPassword(ResetPasswordRequestEntity entity);

        /**
         * ???????????????
         *
         * @param entity ?????????
         * @return ??????????????????
         */
        Observable<NetResponseEntity> getVerificationCode(GetVerificationCodeEntity entity);


        Single<NetResponseEntity<RefreshTokenResponse>> refreshToken(RefreshTokenRequestEntity entity);


        /**
         * ????????????token
         *
         * @param appId  ??????appId
         * @param secret ??????secret
         * @param code   ???????????????????????????
         * @return ??????token
         */
        Single<WXLoginResponseEntity> getWXToken(String appId, String secret, String code);


        Single<WXGetInfoResponseEntity> getWXInfo(String token, String openId);


    }

    interface AppConfig {
        /**
         * ??????APP ????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<GetAppConfigResponseEntity>> getAppConfig(GetAppConfigRequestEntity entity);


        /**
         * ??????????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<Map<String,List<CountryCodeEntity>>>> getCountryList();

        /**
         * ??????????????????
         *
         * @param code ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>> getProvinceList(String code);

        Single<ResponseBody> downloadFirmwareFile(String url);

    }

    interface PersonalInformation {

        /**
         * ????????????????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity<PersonalInfoEntity>> setInfo(PersonalInfoEntity entity);


        Observable<NetResponseEntity<PersonalInfoEntity>> getInfo(GetUserInfoRequestEntity entity, int useCache);

        /**
         * ??????????????????
         *
         * @return ??????
         */
        Observable<NetResponseEntity<AllDiseaseEntity>> getDiseaseList();
    }

    interface Data {


        /**
         * ??????????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>> getIndicatorDescription(int cacheType);

        /**
         * ?????????????????????
         *
         * @return ??????????????????
         */
        Observable<NetResponseEntity<PwDataResponseEntity>> calculatePwData(SaveRawDataRequestEntity entity);


        /**
         * ????????????????????????
         *
         * @param entity ??????body
         * @return ????????????
         */
        Observable<NetResponseEntity<PwDataResponseEntity>> calculateAutoData(SaveRawDataRequestEntity entity);

        /**
         * ?????????????????????
         *
         * @param cacheType ????????????
         * @return ??????????????????
         */
        Observable<NetResponseEntity<PwDataResponseEntity>> getLastData(String userId,int cacheType);


        Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDate(GetStatisticPwByDateRequestEntity entity,
                                                                                          int cacheType);

        Observable<NetResponseEntity<List<PwDataResponseEntity>>> getPwByDateRange(GetPwByDateRangeRequestEntity entity,
                                                                                   int cacheType);

        Observable<NetResponseEntity<PwStatisticDataResponseEntity>> getStatisticPwByDateRange(
                GetStatisticPwByDateRangeRequestEntity entity, int cacheType);

        Observable<NetResponseEntity> deletePw(DeletePwRequestEntity entity);


        Observable<NetResponseEntity<GetHomeDataResponseEntity>> getHomePageData(int cacheType,
                                                                                 GetHomeDataRequestEntity entity);

        Observable<NetResponseEntity<GetHealthManagementResponseEntity>> getHealthManagementData(int cache
                , GetHealthManagementDataRequestEntity entity);

        Observable<NetResponseEntity> setPwRemark(SetPwMarkRequestEntity entity);

        Observable<NetResponseEntity<PwDataResponseEntity>>getDataById(String measureId,String userId);

    }

    interface Relation {

        /**
         * ???????????????????????????
         *
         * @param entity ???????????????
         * @return ??????
         */
        Observable<NetResponseEntity<List<CheckPhoneLoginResponseEntity>>> checkPhone(CheckPhoneRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity> sendRelationRequest(SendRelationRequestRequestEntity entity);


        /**
         * ??????????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity> dealRelationship(DealRelationRequestEntity entity);

        /**
         * ????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity> removeRelationship(RemoveRelationRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ??????
         * @return ????????????
         */
        Observable<NetResponseEntity<List<RelationRequestResponseEntity>>> getRelationRequest(GetRelationRequestEntity entity);

        /**
         * ???????????????????????????
         *
         * @param entity ??????
         * @return ??????
         */
        Observable<NetResponseEntity<List<RelationshipResponseEntity>>> getRelationship(GetRelationshipEntity entity, int cacheType);

        Observable<NetResponseEntity> inviteFriend(InviteFriendRequestEntity entity);

        Observable<NetResponseEntity> setAuthority(SetRelationNoteNameRequestEntity entity);

        Observable<NetResponseEntity<FriendDataResponseEntity>> getFriendData(GetFriendDataRequestEntity entity, int cacheType);

        Observable<NetResponseEntity<Map<String, List<ContactBean>>>> uploadContact(UploadContactRequestEntity entity);

        Observable<NetResponseEntity<SearchContactResponseEntity>> searchContact(SearchContactRequestEntity entity, int cacheType);

        Observable<NetResponseEntity<Map<String, List<ContactBean>>>> getContact(UserIdRequestEntity entity, int cacheType);

        Single<NetResponseEntity<RelationshipResponseEntity>> getFriendInfo(GetFriendInfoRequestEntity entity,int cacheType);

        Single<NetResponseEntity> remindFriendMeasure(RemindFriendMeasureRequestEntity entity);

        Single<NetResponseEntity> setSearchPermissions(SearchPermissionRequestEntity entity);

        Single<NetResponseEntity<SearchPermissionsResponseEntity>> getSearchPermissions(String userId);
    }

    interface Medicine {
        /**
         * ??????????????????
         *
         * @return ????????????????????????????????????
         */
        Observable<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> getMedicineList(String userId, int cacheType);

        /**
         * ??????????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<List<MedicationPlanEntity>>> getMedicationPlanList(String userId, int cacheType);

        /**
         * ??????????????????????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<List<MedicationPlanEntity>>> getCompletedMedicationPlanList(String userId, int cacheType);

        /**
         * ??????????????????
         *
         * @param entity ??????
         * @return ???????????????
         */
        Observable<NetResponseEntity<MedicationPlanEntity>> addMedicationPlan(MedicationPlanEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ?????????
         * @return ??????????????????
         */
        Observable<NetResponseEntity<MedicationPlanEntity>> updateMedicationPlan(MedicationPlanEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ??????id
         * @return ????????????
         */
        Observable<NetResponseEntity> stopMedicationPlan(SetMedicationPlanRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ??????id
         * @return ????????????
         */
        Observable<NetResponseEntity> deleteMedicationPlan(SetMedicationPlanRequestEntity entity);

        /**
         * ????????????????????????
         *
         * @return ????????????
         */
        Observable<NetResponseEntity<MedicationRemindResponseEntity>> getMedicationRemindList(String userId, int cacheType);

        /**
         * ??????????????????
         *
         * @param entity ????????????????????????
         * @return ????????????
         */
        Observable<NetResponseEntity> setMedicationRemind(SetMedicationRemindRequestEntity entity);

        Observable<NetResponseEntity<CustomMedicineEntity>> addMedicine( MedicineAddEntity entity);

        Observable<NetResponseEntity> deleteMedicine(MedicineDeleteRequestEntity entity);

        Observable<NetResponseEntity<CustomMedicineEntity>> editMedicine(MedicineEditRequestEntity entity);

        Observable<NetResponseEntity<List<CustomMedicineEntity>>>getCustomMedicineList(GetCustomMedicineRequestEntity entity);
    }

    interface Abnormal {
        /**
         * ?????????????????????????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getAllException(GetAllExceptionRequestEntity entity, int cacheType);

        /**
         * ????????????????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity, int cacheType);

        Single<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ????????????
         * @return ????????????
         */
        Observable<NetResponseEntity> markRead(SetExceptionReadRequestEntity entity);

        /**
         * ????????????
         *
         * @param entity ??????
         * @return ????????????
         */
        Observable<NetResponseEntity> deleteException(DeleteExceptionEntity entity);

        /**
         * ??????????????????
         *
         * @param entity ??????
         * @return ??????????????????
         */
        Observable<NetResponseEntity<ThresholdRequestEntity>> setThreshold(ThresholdRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity id
         * @return ??????????????????
         */
        Observable<NetResponseEntity<ThresholdRequestEntity>> getThreshold(UserIdRequestEntity entity, int cacheType);
    }

//    interface Firmware {
//        /**
//         * ??????????????????
//         *
//         * @param entity ????????????
//         * @return ?????????????????????
//         */
//        Observable<NetResponseEntity<GetFirmwareResponseEntity>> getFirmware(GetFirmwareRequestEntity entity);
//
//        /**
//         * ??????????????????
//         *
//         * @param url ??????
//         * @return ??????????????????
//         */
//        Observable<String> getFirmwareFile(String url);
//    }

    interface Ecg {
        /**
         * ??????ECG
         *
         * @param entity ????????????
         * @return ?????????
         */
        Observable<NetResponseEntity<EcgResponseEntity>> saveEcg(SaveEcgRequestEntity entity);

        /**
         * ??????ECG??????
         *
         * @param entity ????????????
         * @return ECG id ??????
         */
        Observable<NetResponseEntity<List<EcgResponseEntity>>> getEcgList(GetEcgListRequestEntity entity, int cacheType);

        /**
         * ????????????ECG
         *
         * @param entity ECG id
         * @return ECG ????????????
         */
        Observable<NetResponseEntity<EcgResponseEntity>> getEcgById(GetEcgRequestEntity entity, int cacheType);

        /**
         * ??????ECG
         *
         * @param entity ?????????id
         * @return ????????????
         */
        Observable<NetResponseEntity> deleteEcg(DeleteEcgRequestEntity entity);

    }

    interface Step {
        /**
         * ????????????
         *
         * @param entity ??????
         * @return ????????????
         */
        Observable<NetResponseEntity> saveStep(SaveStepRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param cacheType ????????????
         * @param entity    ????????????
         * @return ??????
         */
        Observable<NetResponseEntity<List<StepDetailItemEntity>>> getStepDetail(int cacheType, GetStepRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param cacheType ????????????
         * @param entity    ??????
         * @return ??????
         */
        Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfo(int cacheType, GetStepStatisticsDataRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param cacheType ????????????
         * @param entity    ??????
         * @return ??????
         */
        Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfoByTime(int cacheType, GetStepStatisticsDataByTimeRequestEntity entity);
    }

    interface Device {
        /**
         * ??????????????????
         *
         * @param entity CheckPhoneRequestEntity
         * @return NetResponseEntity<CheckBraceletResponseEntity>
         */
        Observable<NetResponseEntity<CheckBraceletResponseEntity>> checkBraceletCopyright(CheckBraceletRequestEntity entity);

        /**
         * ????????????
         *
         * @param entity ???????????????mac???key???vector???pairCode???
         * @return ??????
         */
        Observable<NetResponseEntity<BraceletConfigEntity>> bindDevice(BindDeviceRequestEntity entity);

        /**
         * ????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity> unbindDevice(UnbindBraceletRequestEntity entity);

        /**
         * ??????????????????
         *
         * @param entity    ????????????
         * @param cacheType ????????????
         * @return ??????
         */
        Observable<NetResponseEntity<GetBindInfoResponseEntity>> getBracelet(GetDeviceBindInfoRequestEntity entity, int cacheType);

        /**
         * ????????????????????????
         *
         * @param entity    ??????
         * @param cacheType ????????????
         * @return ??????
         */
        Observable<NetResponseEntity<BraceletConfigEntity>> getConfig(GetBraceletConfigRequestEntity entity, int cacheType);

        /**
         * ????????????????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity> setConfig(BraceletConfigEntity entity);

        /**
         * ????????????????????????
         *
         * @param entity ??????
         * @return ??????
         */
        Observable<NetResponseEntity> setInfo(SetBraceletInfoRequestEntity entity);
    }

    interface SingleSignOn {
        /**
         * ??????????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity> savePhoneInfo(SaveMobileInfoRequestEntity entity);

        /**
         * ??????????????????????????????
         *
         * @param entity ????????????
         * @return ??????
         */
        Observable<NetResponseEntity<CheckPhoneInfoResponseEntity>> checkPhoneInfo(CheckPhoneInfoRequestEntity entity);
    }

    interface Notification {
        Single<NetResponseEntity> savePushId(SavePushIdRequestEntity entity);
    }

    interface WeeklyReport {
        Single<NetResponseEntity<List<WeeklyReportResponseEntity>>> getWeeklyReportList(GetWeeklyReportRequestEntity entity, int cacheType);
    }

    interface HealthManagement {
        Single<NetResponseEntity<List<HealthKnowledgeResponseEntity>>> getWeeklyReportList(int cacheType);
    }

    interface DealFile {
        Single<NetResponseEntity<String>> uploadFile(File file);

        Single<ResponseBody> downloadFile(String fileId);

        Single<List<Float>> getPwRawData(String fileId);
    }

    interface Message{
        Single<NetResponseEntity<GetAllUnreadMessageResponseEntity>>getAllUnreadMessage(GetUnreadMessageRequestEntity entity);

        Single<NetResponseEntity>markHealthKnowledgeRead(MarkHealthKnowledgeReadRequestEntity entity);

        Single<NetResponseEntity>markWeeklyReportRead(MarkWeeklyReportReadRequestEntity entity);
    }

    interface Serve{
        Single<NetResponseEntity<List<GetServiceResponseEntity>>>getAllServices(UserIdRequestEntity entity);

        Single<NetResponseEntity<CheckServiceAuthorityResponseEntity>>checkCompanyAuthor(CheckServiceAuthorRequestEntity entity);

        Single<NetResponseEntity<GetServiceAuthCodeResponseEntity>>getCompanyAuthorCode(GetCompanyAuthorCodeRequestEntity entity);

        Single<NetResponseEntity<List<ServiceCompanyResponseEntity>>>getCompanies(String userId);

        Single<NetResponseEntity<List<BoughtServiceResponseEntity>>>getBoughtServices(String userId);

        Single<NetResponseEntity<GetFirstAidPhoneResponseEntity>>getFirstAidPhone(String userId);

        Single<NetResponseEntity>setCustomerContactPermission(SetCustomerContactPermissionRequestEntity entity);

        Single<NetResponseEntity<GetCustomerContactPermissionResponseEntity>> getCustomerContactPermission(String userId);

        Single<NetResponseEntity> cancelServiceAuth(String userId,String companyId);

        Single<NetResponseEntity<List<GetAuthorizedCompanyResponseEntity>>> getAuthorizedCompanies(String userId);
    }
}
