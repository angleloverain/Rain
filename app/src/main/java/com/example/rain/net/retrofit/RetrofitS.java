package com.example.rain.net.retrofit;


import com.example.rain.net.call.StringRequest;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitS extends BaseRetrofit {

    private static StringRequest call;

    static  {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://os.zhijierongxing.com")
                .client(initClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        call = retrofit.create(StringRequest.class);
    }


    public static Call<String> GET(String path){
        return call.GET(path);
    }

    public static Call<String> GET(String path, Map<String, String> from){
        return call.GET(path,from);
    }

    public static Call<String> GET_PART(String path, Map<String, RequestBody> params){
        return call.GET_PART(path,params);
    }

    public static Call<String> POST(String path){
        return call.POST(path);
    }

    public static Call<String> POST(String path, Map<String, String> from){
        return call.POST(path,from);
    }


    public static  Call<String> POST_BODY( String path, Map<String, String> body){
        return call.POST_BODY(path,body);
    }

    public static Call<String> POST_PART(String path, Map<String, RequestBody> params){
        return call.POST_PART(path,params);
    }


    public static Call<String> POST(String path, Map<String, String> form, Map<String, String> body){
        return  call.POST(path,form,body);
    }


    public static Call<String> DELETE( String path, Map<String, RequestBody> params){
        return  call.DELETE(path,params);
    }

    public static Call<String> PUT( String path, Map<String, RequestBody> params){
        return  call.PUT(path,params);
    }

    // 上传
    public static Call<ResponseBody> UPLOAD(String path, Map<String, RequestBody> params){
        return call.UPLOAD(path,params); }

    // 下载
    public static  Call<ResponseBody> DOWNLOAD(String path){
        return call.DOWNLOAD(path);
    }

}
