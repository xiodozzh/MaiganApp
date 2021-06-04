package com.mgtech.domain.utils;

/**
 *
 * @author zhaixiang
 * @date 2016/5/11
 * upload api
 * 上传文件或缓存的json数据到服务器
 */
public interface HttpApi {
//    String API_URL = "http://maiganapp.mystrace.com/";
    String API_URL = "https://api.maigantech.com/";
//    String API_URL_LOCAL = "http://maiganapptest.mystrace.com/";
//    String API_URL_LOCAL = "http://120.132.8.33:9000/";
    String API_URL_LOCAL = "https://apitest.maigantech.com/";

    String API_SOCKET_TEST = "ws://sockettest.maigantech.com:11113/";
    String API_SOCKET_PRODUCE = "ws://socket.maigantech.com:11113/";


    String API_BASE_URL = MyConstant.IS_PRODUCE_NET ? API_URL : API_URL_LOCAL;
//    String API_SOCKET_URL =BleConstant.IS_NET ? "ws://socket.mystrace.com:8888":"ws://socket-test.mystrace.com:8888";
    String API_SOCKET_URL = MyConstant.IS_PRODUCE_NET ? API_SOCKET_PRODUCE : API_SOCKET_TEST;
    String API_LOGIN_URL =API_BASE_URL+"api/Account/LoginByPhone";
    String API_LOGIN_BY_OTHER_APP_URL =API_BASE_URL+"api/Account/LoginByOtherApp";
    String API_BIND_ACCOUNT =API_BASE_URL+"api/Account/BindAccount";

    String API_REFRESH_TOKEN =API_BASE_URL+"api/Account/RefreshAccessToken";

    String API_REGISTER_URL = API_BASE_URL+"api/Account/RegisterAccount";
    String API_MESSAGE_VERIFICATION_URL = API_BASE_URL + "api/Register/MessageValidate";
    String API_RESET_PASSWORD_URL = API_BASE_URL + "api/Account/ForgetPassword";

    /**
     * 设置个人信息
     */
    String API_SET_PERSONAL_INFO_URL = API_BASE_URL + "api/Account/SaveUserInfo";
    /**
     * 获取个人信息
     */
    String API_GET_PERSONAL_INFO_URL = API_BASE_URL + "api/Account/GetUserInfo";



    /**
     * 查询监护请求
     */
    String API_GET_REQUEST = API_BASE_URL + "api/Relation/GetRequests";
    /**
     * 删除监护
     */
    String API_REMOVE_RELATION = API_BASE_URL + "api/Relation/RemoveRelation";

    /**
     * 发送监护请求
     */
    String API_SEND_RELATION_REQUEST = API_BASE_URL + "api/Relation/SendRequest";
    /**
     * 处理监护请求
     */
    String API_SET_RELATION = API_BASE_URL + "api/Relation/DealRequest";
    /**
     * 检查用户是否注册
     */
    String API_CHECK_USER_BY_PHONE = API_BASE_URL + "api/Relation/CheckUsers";

    /**
     * 设置好友昵称
     */
    String API_SET_RELATION_NOTE_NAME = API_BASE_URL + "api/Relation/SetPermissionAndNoteName";

    /**
     * 邀请好友
     */
    String API_INVITE_FRIEND = API_BASE_URL + "api/Relation/Invite";

    String API_SET_SEARCH_PERMISSION = API_BASE_URL + "api/Relation/SetSearchPermissions";

    String API_GET_SEARCH_PERMISSION = API_BASE_URL + "api/Relation/GetSearchPermissions";

    String API_UPLOAD_CONTACT = API_BASE_URL + "api/Relation/UploadContacts";
    String API_GET_CONTACT = API_BASE_URL + "api/Relation/GetContacts";
    String API_SEARCH_CONTACT = API_BASE_URL + "api/Relation/SearchContact";
    String API_GET_FRIEND_INFO = API_BASE_URL + "api/Relation/GetFriendInfo";

    /**
     * 提醒好友测量
     */
    String API_REMIND_FRIEND_MEASURE = API_BASE_URL + "api/Relation/RemindFriendMeasure";

    String API_GET_INDICATOR_DATA = API_BASE_URL + "api/data/GetCalculateData";
    String API_GET_REFRESH_DATA = API_BASE_URL + "api/data/RefreshData";
    String API_GET_DAY_DATA = API_BASE_URL + "api/Data/QueryCurrentData";
    String API_GET_AVG_DATA = API_BASE_URL + "api/Data/QueryAvgData";



    String API_GET_FIRMWARE = API_BASE_URL + "api/Values/GetFirmware";
    String API_MARK_DATA = API_BASE_URL + "api/Data/MarkData";

    /**
     * 获取app配置信息
     */
    String API_GET_APP_CONFIG = API_BASE_URL + "api/Config/GetAppConfiguration";


    /**
     * 根据MG_AnalyzeData的 guid 删除
     */
    String API_DELETE_DATA = API_BASE_URL + "api/Data/DeleteData";

    String API_GET_VERIFICATION_CODE = API_BASE_URL + "api/Account/GetVerificationCode";
    String API_DELETE_MEDICINE = API_BASE_URL + "api/Data/DeleteMedicine";
    /**
     * 获取监护人和被监护人
     */
    String API_GET_RELATIONSHIP = API_BASE_URL + "api/Relation/GetRelationList";
//    String API_QUERY_ASYNC_DATA = API_BASE_URL + "api/Data/QueryAsyncData";

    String API_GET_FRIEND_DATA = API_BASE_URL + "api/Relation/GetFriendData";
    /**
     * 计步功能
     */
    String API_SAVE_STEP_DATA = API_BASE_URL + "api/step/SaveStepData";
    String API_GET_STEP_DETAIL_BY_DATE = API_BASE_URL + "api/step/GetStepDataByTime";
    String API_GET_STATISTICS_STEP_INFO = API_BASE_URL + "api/step/QueryStatisticsInfo";
    String API_GET_STATISTICS_STEP_BY_TIME = API_BASE_URL + "api/step/QueryStatisticsInfoByDate";

    /**
     * 首页查询
     */
    String API_GET_HOME_PAGE_DATA = API_BASE_URL + "api/PW/GetHomeData";

    /**
     * 校验 Mac 地址和手环的 GUID
     */
    String API_CHECK_BRACELET_COPYRIGHT = API_BASE_URL + "api/Bracelet/CheckCopyright";
    /**
     * 绑定手环
     */
    String API_BIND_BRACELET = API_BASE_URL + "api/Bracelet/Bind";
    /**
     * 解绑手环
     */
    String API_UNBIND_BRACELET = API_BASE_URL + "api/Bracelet/UnBind";


    /**
     * 获取手环
     */
    String API_GET_BRACELET = API_BASE_URL + "api/Bracelet/GetBracelet";

    /**
     * 获取设备配置信息
     */
    String API_GET_BRACELET_CONFIG = API_BASE_URL + "api/Bracelet/GetBraceletConfig";

    /**
     * 保存设备配置信息
     */
    String API_SET_BRACELET_CONFIG = API_BASE_URL + "api/Bracelet/SaveBraceletConfig";

    /**
     * 保存其它信息
     */
    String API_SET_BRACELET_INFORMATION = API_BASE_URL + "api/Bracelet/SetBraceletInformation";

    /**
     * SSO 单点登陆 保存手机信息
     */
    String API_SAVE_PHONE_INFO = API_BASE_URL + "api/Account/SetPhoneInfo";

    String API_CHECK_PHONE = API_BASE_URL + "api/Account/CheckSignInValidation";

    /**
     * 保存用户自定义药物
     */
    String API_SAVE_MEDICINE_BY_USER = API_BASE_URL + "api/drug/save_user_defined";
    /**
     * 参数说明
     */
    String API_GET_INDICATOR_PARAMETER_DESCRIPTION = API_BASE_URL + "api/PW/GetParameterExplain";

    /**
     * 获取国家列表
     */
    String API_GET_COUNTRY_LIST = API_BASE_URL + "api/Others/GetCountryList";
    /**
     * 获取省份列表
     */
    String API_GET_PROVINCE_LIST = API_BASE_URL + "api/Others/GetProvinceList";

    /**
     * 获取疾病列表
     */
    String API_GET_ALL_DISEASE_INFO = API_BASE_URL + "api/Account/GetAllDiseaseInfo";


    /**
     * 计算血压
     */
    String API_SAVE_RAW_DATA = API_BASE_URL + "api/PW/SavePWRawData";

    /**
     * 通过id获取血压结果
     */
    String API_GET_PW_DATA_BY_ID = API_BASE_URL + "api/PW/GetPWDataByMeasureGuid";

    /**
     * 获取最近一次血压值数据
     */
    String API_GET_LAST_PW_DATA = API_BASE_URL + "api/PW/GetLastPWData";

    /**
     * 获取某天血压数据
     */
    String API_GET_STATISTIC_PW_BY_DATE = API_BASE_URL + "api/PW/GetPWDataByDate";

    /**
     * 获取一段时间的血压数据
     */
    String API_GET_PW_BY_DATE_RANGE = API_BASE_URL + "api/PW/GetPWCalculateDataByDate";


    /**
     * 按日期获取血压平均数据
     */
    String API_GET_STATISTIC_PW_BY_DATE_RANGE = API_BASE_URL + "api/PW/GetPWAvgDataByDate";

    /**
     * 删除某条血压数据
     */
    String API_DELETE_PW = API_BASE_URL + "api/PW/DeletePWData";

    /**
     * 保存并计算ECG
     */
    String API_SAVE_ECG = API_BASE_URL + "api/ECG/SaveEcgRawData";
    /**
     * 根据日期获取ECG
     */
    String API_GET_ECG_LIST_BY_DATE = API_BASE_URL + "api/ECG/GetECGDataByDate";
    /**
     * 根据id获取ECG
     */
    String API_GET_ECG_BY_ID = API_BASE_URL + "api/ECG/GetECGDataBymeasureGuid";
    /**
     * 删除ECG
     */
    String API_DELETE_ECG = API_BASE_URL + "api/ECG/DeleteECGData";





    /**
     * 药物信息
     */
    String API_GET_ALL_MEDICINE = API_BASE_URL + "api/Medication/GetAllMedicationList";

    /**
     * 添加用药计划
     */
    String API_ADD_MEDICATION_PLAN = API_BASE_URL + "api/Medication/AddMedicationPlan";
    /**
     * 更新用药计划
     */
    String API_UPDATE_MEDICATION_PLAN = API_BASE_URL + "api/Medication/UpdateMedicationPlan";

    /**
     * 停止用药计划
     */
    String API_STOP_MEDICATION_PLAN = API_BASE_URL + "api/Medication/StopMedicationPlan";
    /**
     * 删除用药计划
     */
    String API_DELETE_MEDICATION_PLAN = API_BASE_URL + "api/Medication/DeleteMedicationPlan";

    /**
     * 获取用药提醒列表
     */
    String API_GET_MEDICATION_REMIND_LIST= API_BASE_URL + "api/Medication/GetMedicationRemindList";
    /**
     * 设置提醒状态
     */
    String API_RECORD_MEDICATION_REMIND = API_BASE_URL + "api/Medication/UpdateMedicationRemindState";

    /**
     * 添加药物
     */
    String API_MEDICINE_ADD = API_BASE_URL + "api/drug/custom/add";

    String API_MEDICINE_DELETE = API_BASE_URL + "api/drug/custom/delete";

    String API_MEDICINE_EDIT = API_BASE_URL + "api/drug/custom/edit";

    String API_MEDICINE_GET_CUSTOM_LIST = API_BASE_URL + "api/drug/custom/list";

    /**
     * 获取用药记录
     */
    String API_GET_COMPLETED_MEDICATION_PLAN_LIST = API_BASE_URL + "api/Medication/GetUseMedicationRecord";

    /**
     * 获取用药计划列表
     */
    String API_GET_MEDICATION_PLAN_LIST = API_BASE_URL + "api/Medication/GetMedicationPlanList";


    /**
     * 获取慢病管理页面数据
     */
    String API_GET_HEALTH_MANAGEMENT_DATA = API_BASE_URL + "api/PW/GetHealthManagementData";

    String API_PW_REMARK = API_BASE_URL + "api/PW/AddPWRemark";


    String API_PUSH_EXCEPTION = API_BASE_URL + "api/Push/ExceptionPush";
    /**
     * 获取异常列表
     */
    String API_GET_ALL_EXCEPTIONS = API_BASE_URL + "api/Abnormal/GetUserAndFriendsAbnormalRemindList";
    /**
     * 获取某个用户的异常
     */
    String API_GET_EXCEPTIONS_BY_ID = API_BASE_URL + "api/Abnormal/GetUserAbnormalRemindList";

    /**
     * 标记异常信息已读
     */
    String API_MARK_EXCEPTION_READ = API_BASE_URL + "api/Abnormal/AbnormalRemindMarkRead";

    /**
     * 删除异常信息
     */
    String API_DELETE_EXCEPTION = API_BASE_URL + "api/Abnormal/DeleteAbnormalRemind";
    /**
     * 设置异常提醒阈值
     */
    String API_SET_EXCEPTION_THRESHOLD = API_BASE_URL + "api/Abnormal/SetAbnormalRemindThreshold";
    /**
     * 获取异常提醒阈值
     */
    String API_GET_EXCEPTION_THRESHOLD = API_BASE_URL + "api/Abnormal/GetAbnormalRemindThreshold";

    String API_GET_WEEKLY_REPORT= API_BASE_URL + "api/WeekReport/GetWeekReportList";

    String API_GET_KNOWLEDGE_LIST= API_BASE_URL + "api/HealthManagement/GetHealthKnowledgeList";

    String API_UPLOAD_FILE = API_BASE_URL + "api/upload/file";

    String API_DOWNLOAD_IMG = API_BASE_URL + "api/download/img";

    String API_DOWNLOAD_FILE = API_BASE_URL + "api/download/file";

    String API_SAVE_PUSH_ID = API_BASE_URL + "api/Notification/SaveRegistrationId";


    String API_GET_ALL_UNREAD_MESSAGE = API_BASE_URL + "api/Notification/GetAllUnreadNotifications";

    String API_MARK_HEALTH_KNOWLEDGE_READ = API_BASE_URL + "api/Notification/HealthKnowledgeMarkRead";

    String API_MARK_WEEKLY_REPORT_READ = API_BASE_URL + "api/Notification/WeekReportMarkRead";

    String API_GET_SERVICE_LIST = API_BASE_URL + "api/Service/list";

    String API_GET_COMPANY_LIST = API_BASE_URL + "api/Service/GetServiceCompanies";

    String API_GET_BOUGHT_SERVICE_LIST = API_BASE_URL + "api/Service/GetBoughtService";

    String API_GET_FIRST_AID_PHONE = API_BASE_URL + "api/Service/GetFirstAidPhone";

    String API_CHECK_SERVICE_AUTHOR = API_BASE_URL + "api/Account/AuthorizedStatus";

    String API_GET_SERVICE_AUTHOR_CODE = API_BASE_URL + "api/Account/GetAuthCode";

    String API_SET_CUSTOMER_CONTACT_PERMISSION = API_BASE_URL + "api/Account/SetCustomerContactPermission";

    String API_GET_CUSTOMER_CONTACT_PERMISSION = API_BASE_URL + "api/Account/GetCustomerContactPermission";

    String API_CANCEL_SERVICE_AUTH = API_BASE_URL + "api/Account/AuthCancel";

    String API_GET_AUTHORIZED_COMPANIES = API_BASE_URL + "api/Service/GetAuthorizedCompanies";

}
