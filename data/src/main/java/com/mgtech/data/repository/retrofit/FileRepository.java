package com.mgtech.data.repository.retrofit;

import android.content.Context;
import android.graphics.Bitmap;

import com.mgtech.data.net.retrofit.FileApi;
import com.mgtech.data.net.retrofit.RetrofitFactory;
import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.rx.DoOnTokenErrorAction;
import com.mgtech.domain.utils.HttpApi;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Single;

/**
 * @author zhaixiang
 */
public class FileRepository implements NetRepository.DealFile {
    private FileApi api;
    public FileRepository(Context context) {
        this.api = new RetrofitFactory()
                .setBaseUrl(HttpApi.API_BASE_URL)
                .setUseToken(true)
                .build(context).create(FileApi.class);
    }

    @Override
    public Single<NetResponseEntity<String>> uploadFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),requestFile);
        return api.uploadFile(body).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<ResponseBody> downloadFile(String fileId) {
        return api.downloadFile(fileId).doOnError(new DoOnTokenErrorAction());
    }

    @Override
    public Single<List<Float>> getPwRawData(String fileId) {
        return api.getPwRawData(fileId).doOnError(new DoOnTokenErrorAction());
    }
}
