package com.mgtech.data.net.retrofit;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.utils.HttpApi;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Single;

public interface FileApi {
    @Multipart
    @POST(HttpApi.API_UPLOAD_FILE)
    Single<NetResponseEntity<String>> uploadFile(@Part MultipartBody.Part file);

    @GET(HttpApi.API_DOWNLOAD_IMG)
    Single<ResponseBody> downloadFile(@Query("fileId") String fileId);

    @GET(HttpApi.API_DOWNLOAD_IMG)
    Single<List<Float>> getPwRawData(@Query("fileId") String fileId);
}
