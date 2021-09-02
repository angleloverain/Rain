package com.example.rain.net.call;


import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface BaseRequest {
    /** 固定参数返回类型 以及API **/
    String token = "";

    @Multipart
    @POST("/{path}") // 上传文件
    Call<ResponseBody> UPLOAD(@Path("path") String path,
                              @PartMap Map<String, RequestBody> params);

    @GET("/{path}") // 下载文件
    Call<ResponseBody> DOWNLOAD(@Path("path") String path);
}
