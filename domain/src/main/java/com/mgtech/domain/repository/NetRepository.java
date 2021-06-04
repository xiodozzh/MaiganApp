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
 * 与net相关的Repository
 */
public interface NetRepository {
    interface StringNet {
        Observable<String> getFromNet(RequestEntity entity, int cacheType);
    }

    interface Account {
        /**
         * 登录
         *
         * @param entity 登录信息
         * @return 登录结果
         */
        Single<NetResponseEntity<LoginResponseEntity>> login(LoginByPhoneRequestEntity entity);

        Single<NetResponseEntity<LoginResponseEntity>> loginByOther(LoginByOtherAppRequestEntity entity);

        Single<NetResponseEntity<BindAccountResponseEntity>> bindAccount(BindAccountRequestEntity entity);

        /**
         * 注册
         *
         * @param entity 注册信息
         * @return 登录结果
         */
        Observable<NetResponseEntity<RegisterResponseEntity>> register(RegisterRequestEntity entity);


        /**
         * 短信验证
         *
         * @param entity 验证信息
         * @return 验证结果
         */
        Observable<NetResponseEntity> verify(VerifyRequestEntity entity);

        /**
         * 重置密码
         *
         * @param entity 新密码
         * @return 结果
         */
        Observable<NetResponseEntity> resetPassword(ResetPasswordRequestEntity entity);

        /**
         * 获取验证码
         *
         * @param entity 手机号
         * @return 是否发送成功
         */
        Observable<NetResponseEntity> getVerificationCode(GetVerificationCodeEntity entity);


        Single<NetResponseEntity<RefreshTokenResponse>> refreshToken(RefreshTokenRequestEntity entity);


        /**
         * 获取微信token
         *
         * @param appId  微信appId
         * @param secret 微信secret
         * @param code   用户同意后的权限码
         * @return 微信token
         */
        Single<WXLoginResponseEntity> getWXToken(String appId, String secret, String code);


        Single<WXGetInfoResponseEntity> getWXInfo(String token, String openId);


    }

    interface AppConfig {
        /**
         * 获取APP 配置信息
         *
         * @return 配置信息
         */
        Observable<NetResponseEntity<GetAppConfigResponseEntity>> getAppConfig(GetAppConfigRequestEntity entity);


        /**
         * 获取国家列表
         *
         * @return 国家列表
         */
        Observable<NetResponseEntity<Map<String,List<CountryCodeEntity>>>> getCountryList();

        /**
         * 获取省份列表
         *
         * @param code 国家编号
         * @return 省份列表
         */
        Observable<NetResponseEntity<Map<String,List<ProvinceCodeEntity>>>> getProvinceList(String code);

        Single<ResponseBody> downloadFirmwareFile(String url);

    }

    interface PersonalInformation {

        /**
         * 修改用户个人信息
         *
         * @param entity 个人信息
         * @return 结果
         */
        Observable<NetResponseEntity<PersonalInfoEntity>> setInfo(PersonalInfoEntity entity);


        Observable<NetResponseEntity<PersonalInfoEntity>> getInfo(GetUserInfoRequestEntity entity, int useCache);

        /**
         * 获取疾病列表
         *
         * @return 疾病
         */
        Observable<NetResponseEntity<AllDiseaseEntity>> getDiseaseList();
    }

    interface Data {


        /**
         * 获取参数说明
         *
         * @return 说明列表
         */
        Observable<NetResponseEntity<List<IndicatorDescriptionResponseEntity>>> getIndicatorDescription(int cacheType);

        /**
         * 上传数据并计算
         *
         * @return 计算后的数据
         */
        Observable<NetResponseEntity<PwDataResponseEntity>> calculatePwData(SaveRawDataRequestEntity entity);


        /**
         * 上传自动采样数据
         *
         * @param entity 数据body
         * @return 上传结果
         */
        Observable<NetResponseEntity<PwDataResponseEntity>> calculateAutoData(SaveRawDataRequestEntity entity);

        /**
         * 获取最近的数据
         *
         * @param cacheType 缓存策略
         * @return 计算后的数据
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
         * 检查手机号是否注册
         *
         * @param entity 手机号信息
         * @return 结果
         */
        Observable<NetResponseEntity<List<CheckPhoneLoginResponseEntity>>> checkPhone(CheckPhoneRequestEntity entity);

        /**
         * 发送关系请求
         *
         * @param entity 请求信息
         * @return 结果
         */
        Observable<NetResponseEntity> sendRelationRequest(SendRelationRequestRequestEntity entity);


        /**
         * 处理关注请求
         *
         * @param entity 关注请求
         * @return 处理结果
         */
        Observable<NetResponseEntity> dealRelationship(DealRelationRequestEntity entity);

        /**
         * 取消关注
         *
         * @param entity 取消请求
         * @return 处理结果
         */
        Observable<NetResponseEntity> removeRelationship(RemoveRelationRequestEntity entity);

        /**
         * 获取关注请求
         *
         * @param entity 请求
         * @return 处理结果
         */
        Observable<NetResponseEntity<List<RelationRequestResponseEntity>>> getRelationRequest(GetRelationRequestEntity entity);

        /**
         * 获取关注和被关注人
         *
         * @param entity 请求
         * @return 结果
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
         * 获取药品列表
         *
         * @return 药品列表（按首字母排序）
         */
        Observable<NetResponseEntity<Map<String, List<MedicineResponseEntity>>>> getMedicineList(String userId, int cacheType);

        /**
         * 获取用药计划
         *
         * @return 用药计划
         */
        Observable<NetResponseEntity<List<MedicationPlanEntity>>> getMedicationPlanList(String userId, int cacheType);

        /**
         * 获取已完成的用药计划
         *
         * @return 用药计划
         */
        Observable<NetResponseEntity<List<MedicationPlanEntity>>> getCompletedMedicationPlanList(String userId, int cacheType);

        /**
         * 添加用药计划
         *
         * @param entity 计划
         * @return 添加的计划
         */
        Observable<NetResponseEntity<MedicationPlanEntity>> addMedicationPlan(MedicationPlanEntity entity);

        /**
         * 修改用药计划
         *
         * @param entity 新计划
         * @return 修改后的计划
         */
        Observable<NetResponseEntity<MedicationPlanEntity>> updateMedicationPlan(MedicationPlanEntity entity);

        /**
         * 停止用药计划
         *
         * @param entity 计划id
         * @return 是否成功
         */
        Observable<NetResponseEntity> stopMedicationPlan(SetMedicationPlanRequestEntity entity);

        /**
         * 删除用药计划
         *
         * @param entity 计划id
         * @return 是否成功
         */
        Observable<NetResponseEntity> deleteMedicationPlan(SetMedicationPlanRequestEntity entity);

        /**
         * 获取药物提醒列表
         *
         * @return 提醒列表
         */
        Observable<NetResponseEntity<MedicationRemindResponseEntity>> getMedicationRemindList(String userId, int cacheType);

        /**
         * 记录药物提醒
         *
         * @param entity 设置完成或者忽略
         * @return 是否成功
         */
        Observable<NetResponseEntity> setMedicationRemind(SetMedicationRemindRequestEntity entity);

        Observable<NetResponseEntity<CustomMedicineEntity>> addMedicine( MedicineAddEntity entity);

        Observable<NetResponseEntity> deleteMedicine(MedicineDeleteRequestEntity entity);

        Observable<NetResponseEntity<CustomMedicineEntity>> editMedicine(MedicineEditRequestEntity entity);

        Observable<NetResponseEntity<List<CustomMedicineEntity>>>getCustomMedicineList(GetCustomMedicineRequestEntity entity);
    }

    interface Abnormal {
        /**
         * 获取用户和好友异常提醒
         *
         * @param entity 查询信息
         * @return 提醒信息
         */
        Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getAllException(GetAllExceptionRequestEntity entity, int cacheType);

        /**
         * 获取用户异常提醒
         *
         * @param entity 查询信息
         * @return 提醒信息
         */
        Observable<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity, int cacheType);

        Single<NetResponseEntity<List<ExceptionResponseEntity>>> getException(GetAllExceptionRequestEntity entity);

        /**
         * 标记异常已读
         *
         * @param entity 信息已读
         * @return 是否成功
         */
        Observable<NetResponseEntity> markRead(SetExceptionReadRequestEntity entity);

        /**
         * 删除异常
         *
         * @param entity 删除
         * @return 是否成功
         */
        Observable<NetResponseEntity> deleteException(DeleteExceptionEntity entity);

        /**
         * 设置异常阈值
         *
         * @param entity 阈值
         * @return 设置好的阈值
         */
        Observable<NetResponseEntity<ThresholdRequestEntity>> setThreshold(ThresholdRequestEntity entity);

        /**
         * 获取异常阈值
         *
         * @param entity id
         * @return 设置好的阈值
         */
        Observable<NetResponseEntity<ThresholdRequestEntity>> getThreshold(UserIdRequestEntity entity, int cacheType);
    }

//    interface Firmware {
//        /**
//         * 获取固件信息
//         *
//         * @param entity 请求信息
//         * @return 固件信息及地址
//         */
//        Observable<NetResponseEntity<GetFirmwareResponseEntity>> getFirmware(GetFirmwareRequestEntity entity);
//
//        /**
//         * 下载固件文件
//         *
//         * @param url 地址
//         * @return 保存的文件名
//         */
//        Observable<String> getFirmwareFile(String url);
//    }

    interface Ecg {
        /**
         * 保存ECG
         *
         * @param entity 保存信息
         * @return 返回值
         */
        Observable<NetResponseEntity<EcgResponseEntity>> saveEcg(SaveEcgRequestEntity entity);

        /**
         * 获取ECG列表
         *
         * @param entity 查询参数
         * @return ECG id 列表
         */
        Observable<NetResponseEntity<List<EcgResponseEntity>>> getEcgList(GetEcgListRequestEntity entity, int cacheType);

        /**
         * 获取单条ECG
         *
         * @param entity ECG id
         * @return ECG 具体信息
         */
        Observable<NetResponseEntity<EcgResponseEntity>> getEcgById(GetEcgRequestEntity entity, int cacheType);

        /**
         * 删除ECG
         *
         * @param entity 删除的id
         * @return 删除结果
         */
        Observable<NetResponseEntity> deleteEcg(DeleteEcgRequestEntity entity);

    }

    interface Step {
        /**
         * 保存步数
         *
         * @param entity 步数
         * @return 是否成功
         */
        Observable<NetResponseEntity> saveStep(SaveStepRequestEntity entity);

        /**
         * 获取具体数据
         *
         * @param cacheType 缓存方式
         * @param entity    访问参数
         * @return 结果
         */
        Observable<NetResponseEntity<List<StepDetailItemEntity>>> getStepDetail(int cacheType, GetStepRequestEntity entity);

        /**
         * 获取统计数据
         *
         * @param cacheType 缓存方式
         * @param entity    参数
         * @return 结果
         */
        Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfo(int cacheType, GetStepStatisticsDataRequestEntity entity);

        /**
         * 获取统计数据
         *
         * @param cacheType 缓存方式
         * @param entity    参数
         * @return 结果
         */
        Observable<NetResponseEntity<List<StepStatisticsItemEntity>>> getStepStatisticsInfoByTime(int cacheType, GetStepStatisticsDataByTimeRequestEntity entity);
    }

    interface Device {
        /**
         * 是否检查成功
         *
         * @param entity CheckPhoneRequestEntity
         * @return NetResponseEntity<CheckBraceletResponseEntity>
         */
        Observable<NetResponseEntity<CheckBraceletResponseEntity>> checkBraceletCopyright(CheckBraceletRequestEntity entity);

        /**
         * 绑定手环
         *
         * @param entity 绑定信息（mac、key、vector、pairCode）
         * @return 结果
         */
        Observable<NetResponseEntity<BraceletConfigEntity>> bindDevice(BindDeviceRequestEntity entity);

        /**
         * 解绑手环
         *
         * @param entity 解绑信息
         * @return 结果
         */
        Observable<NetResponseEntity> unbindDevice(UnbindBraceletRequestEntity entity);

        /**
         * 获取绑定信息
         *
         * @param entity    绑定信息
         * @param cacheType 缓存方式
         * @return 结果
         */
        Observable<NetResponseEntity<GetBindInfoResponseEntity>> getBracelet(GetDeviceBindInfoRequestEntity entity, int cacheType);

        /**
         * 获取手环配置信息
         *
         * @param entity    请求
         * @param cacheType 缓存方式
         * @return 结果
         */
        Observable<NetResponseEntity<BraceletConfigEntity>> getConfig(GetBraceletConfigRequestEntity entity, int cacheType);

        /**
         * 保存手环配置信息
         *
         * @param entity 手环信息
         * @return 结果
         */
        Observable<NetResponseEntity> setConfig(BraceletConfigEntity entity);

        /**
         * 保存手环其它信息
         *
         * @param entity 信息
         * @return 结果
         */
        Observable<NetResponseEntity> setInfo(SetBraceletInfoRequestEntity entity);
    }

    interface SingleSignOn {
        /**
         * 保存手机信息
         *
         * @param entity 手机信息
         * @return 结果
         */
        Observable<NetResponseEntity> savePhoneInfo(SaveMobileInfoRequestEntity entity);

        /**
         * 检查手机信息是否一致
         *
         * @param entity 手机信息
         * @return 结果
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
