package com.example.rain.net.call

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

open interface StringRequest : BaseRequest {

    /**  基础访问方法  */
    @GET("/{path}")
    fun GET(@Path("path") path: String?): Call<String?>?

    @GET("/{path}")
    fun GET( // from表单，就是拼接在后的参数
        @Path("path") path: String?,  // 路径
        @QueryMap from: Map<String?, String?>?
    ): Call<String?>?


    @Multipart
    @GET("/{path}")
    fun GET_PART(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<String?>?

    @POST("/{path}")
    fun POST(@Path("path") path: String?): Call<String?>?

    @POST("/{path}")
    fun POST(
        @Path("path") path: String?,
        @QueryMap from: Map<String?, String?>?
    ): Call<String?>?

    @POST("/{path}")
    @FormUrlEncoded
    fun POST_BODY( // 使用该注解强制加入body不允许表单
        @Path("path") path: String?,
        @FieldMap body: Map<String?, String?>?
    ): Call<String?>?

    @POST("/{path}")
    @Multipart
    fun POST_PART(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<String?>?


    @POST("/{path}")
    fun POST(
        @Path("path") path: String?,
        @QueryMap form: Map<String?, String?>?,
        @FieldMap body: Map<String?, String?>?
    ): Call<String?>?

    @Multipart
    @DELETE("/{path}")
    fun DELETE(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<String?>?

    @Multipart
    @PUT("/{path}")
    fun PUT(
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?
    ): Call<String?>?

}