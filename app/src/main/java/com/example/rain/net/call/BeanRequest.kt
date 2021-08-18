package com.example.rain.net.call

import com.example.rain.bean.BaseBean
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

open interface BeanRequest :BaseRequest {

    //    /**  基础访问方法 **/
    @GET("/{path}")
    fun GET(@Path("path") path: String?): Call<BaseBean?>?

    @GET("/{path}")
    fun GET(
        @Path("path") path: String?,  // 路径
        @QueryMap from: Map<String?, String?>?
    ): Call<BaseBean?>? // from表单，就是拼接在后的参数


    @Multipart
    @GET("/{path}")
    fun GET_PART(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<BaseBean?>?

    @POST("/{path}")
    fun POST(@Path("path") path: String?): Call<BaseBean?>?

    @POST("/{path}")
    fun POST(
        @Path("path") path: String?,
        @QueryMap from: Map<String?, String?>?
    ): Call<BaseBean?>?

    @POST("/{path}")
    @FormUrlEncoded
    fun  // 使用该注解强制加入body不允许表单
            POST_BODY(
        @Path("path") path: String?,
        @FieldMap body: Map<String?, String?>?
    ): Call<BaseBean?>?

    @POST("/{path}")
    @Multipart
    fun POST_PART(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<BaseBean?>?


    @POST("/{path}")
    fun POST(
        @Path("path") path: String?,
        @QueryMap form: Map<String?, String?>?,
        @FieldMap body: Map<String?, String?>?
    ): Call<BaseBean?>?

    @Multipart
    @DELETE("/{path}")
    fun DELETE(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<BaseBean?>?

    @Multipart
    @PUT("/{path}")
    fun PUT(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<BaseBean?>?

}