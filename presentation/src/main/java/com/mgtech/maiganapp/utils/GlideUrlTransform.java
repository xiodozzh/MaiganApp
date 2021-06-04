package com.mgtech.maiganapp.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.mgtech.domain.utils.HttpApi;

import java.net.MalformedURLException;
import java.net.URL;

public class GlideUrlTransform extends GlideUrl {
    private String url;
    private String baseUrl;

    public GlideUrlTransform(String url) {
        super(url);
    }

    @Override
    public String getCacheKey() {
        return super.getCacheKey();
    }

//    @Override
//    public URL toURL() throws MalformedURLException {
//        return HttpApi.API_DOWNLOAD_IMG + "?=" ;
//    }
}
