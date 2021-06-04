package com.mgtech.domain.interactor;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.TypedValue;

import com.mgtech.domain.entity.net.response.NetResponseEntity;
import com.mgtech.domain.repository.NetRepository;
import com.mgtech.domain.utils.CacheUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * @author zhaixiang
 */
public class FileUseCase {
    private NetRepository.DealFile fileRepository;
    private static final String FILE_PATH ="avatar";

    public FileUseCase(NetRepository.DealFile fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Single<NetResponseEntity<String>> getSaveFileObservable(File file) {
        return fileRepository.uploadFile(file);
    }

    public Single<ResponseBody> getFile(String url){
        return fileRepository.downloadFile(url);
    }

    public Single<List<Float>> getPwRawData(String url){
        return fileRepository.getPwRawData(url);
    }

//    public void saveAvatar(final Context context, final Uri uri, Subscriber<NetResponseEntity<String>> subscriber){
//        Single.create(new Single.OnSubscribe<String>() {
//            @Override
//            public void call(SingleSubscriber<? super String> singleSubscriber) {
//                // URI 查找实际文件
//                singleSubscriber.onSuccess(getImagePath(context, uri));
//            }
//        }).map(new Func1<String, File>() {
//            @Override
//            public File call(String s) {
//                // 读取图片并压缩
//                Bitmap bitmap = decodeSampleBitmapFromPath(s,dp2px(100));
//                FileOutputStream fos = null;
//                ByteArrayOutputStream bos = null;
//                File file = null;
//                try {
//                    File dir = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH);
//                    if (!dir.exists()){
//                        dir.mkdir();
//                    }
//                    file = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,"temp.jpg");
//                    if (file.exists()){
//                        file.delete();
//                    }
//                    bos = new ByteArrayOutputStream();
//                    fos = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
//                    fos.write(bos.toByteArray());
//                    fos.flush();
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (fos != null) {
//                            fos.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return file;
//            }
//        })
//                .flatMap(new Func1<File, Single<NetResponseEntity<String>>>() {
//            @Override
//            public Single<NetResponseEntity<String>> call(File file) {
//                // 文件上传服务器
//                return fileRepository.uploadFile(file);
//            }
//        })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

    public void setAvatarFromFile(File file, Subscriber<NetResponseEntity<String>> subscriber){
        fileRepository.uploadFile(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 压缩图片
     *
     * @param path  图片路径
     * @param size 需要的大小
     * @return 压缩后的图片
     */
    private static Bitmap decodeSampleBitmapFromPath(String path, int size) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int scale = Math.min(options.outWidth / size, options.outHeight / size);
        if (scale < 1) {
            scale = 1;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


//    public void getAvatar(final Context context, final String fileId, Subscriber<File> subscriber){
//        Single.just(fileId)
//                .observeOn(Schedulers.io())
//                .flatMap(new Func1<String, Single<File>>() {
//                    @Override
//                    public Single<File> call(String s) {
//                        File file = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,s);
//                        if (file.exists()) {
//                            return Single.just(file);
//                        }else{
//                            return fileRepository.downloadFile(s)
//                                    .observeOn(Schedulers.io())
//                                    .map(new Func1<ResponseBody, File>() {
//                                        @Override
//                                        public File call(ResponseBody responseBody) {
//                                            copyFileToCache(context,fileId,responseBody.source());
//                                            return  new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,fileId);
//                                        }
//                                    });
//                        }
//                    }
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }

//    public void loadAvatar(final Context context, final String fileId){
//        if (TextUtils.isEmpty(fileId)){
//            return;
//        }
//        File file = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,fileId);
//        if (!file.exists() && file.length() < 20) {
//            return;
//        }
//        fileRepository.downloadFile(fileId)
//                .observeOn(Schedulers.io())
//                .map(new Func1<ResponseBody, File>() {
//                    @Override
//                    public File call(ResponseBody responseBody) {
//                        copyFileToCache(context,fileId,responseBody.source());
//                        return  new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,fileId);
//                    }
//                }).subscribeOn(Schedulers.io())
//                .subscribe();
//    }

    private  String getImagePath(Context context,Uri uri) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }


    /**
     * 将文件复制到cache文件夹中
     * @param context 上下文
     * @param cacheName 复制后的文件名（fileId）
     * @param source 文件源
     */
    private void copyFileToCache(Context context,String cacheName, BufferedSource source){
        BufferedSink sink = null;
        try {
            cacheName = decodeImgPathFromNet(cacheName);
            File dir = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH);
            if (dir.exists()){
                dir.delete();
            }
            dir.createNewFile();
            File file = new File(CacheUtil.getCachePath(context) + File.separator + FILE_PATH,cacheName);

            sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(source);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (sink != null) {
                    sink.close();
                    source.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * 处理网络图片地址
     * @param path 上传后保存的地址
     * @return 去掉/的地址，用于缓存
     */
    public static String decodeImgPathFromNet(String path){
        return path.replaceAll("/","");
    }

}
