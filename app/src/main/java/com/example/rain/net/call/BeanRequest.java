package com.example.rain.net.call;



import com.example.rain.bean.BaseBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface BeanRequest extends BaseRequest{

    //    /**  基础访问方法 **/
    @GET("/{path}")
    Call<BaseBean> GET(@Path("path") String path);

    @GET("/{path}")
    Call<BaseBean> GET(@Path("path") String path, // 路径
                       @QueryMap Map<String, String> from); // from表单，就是拼接在后的参数

    @Multipart
    @GET("/{path}")
    Call<BaseBean> GET_PART(@Path("path") String path,
                            @PartMap Map<String, RequestBody> params);

    @POST("/{path}")
    Call<BaseBean> POST(@Path("path") String path);

    @POST("/{path}")
    Call<BaseBean> POST(@Path("path") String path,
                        @QueryMap Map<String, String> from);

    @POST("/{path}")
    @FormUrlEncoded
        // 使用该注解强制加入body不允许表单
    Call<BaseBean> POST_BODY(@Path("path") String path,
                             @FieldMap Map<String, String> body);

    @POST("/{path}")
    @Multipart
    Call<BaseBean> POST_PART(@Path("path") String path,
                             @PartMap Map<String, RequestBody> params);


    @POST("/{path}")
    Call<BaseBean> POST(@Path("path") String path,
                        @QueryMap Map<String, String> form,
                        @FieldMap Map<String, String> body);

    @Multipart
    @DELETE("/{path}")
    Call<BaseBean> DELETE(@Path("path") String path,
                          @PartMap Map<String, RequestBody> params);

    @Multipart
    @PUT("/{path}")
    Call<BaseBean> PUT(@Path("path") String path,
                       @PartMap Map<String, RequestBody> params);

}
