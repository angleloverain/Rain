package com.example.rain.net.call

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

open interface BaseRequest {

    var token :String

    @Multipart
    @POST("/{path}")
    fun UPLOAD(   // 上传文件
        @Path("path") path: String?,
        @PartMap params: Map<String?, RequestBody?>?): Call<ResponseBody?>?

    @GET("/{path}")  // 下载文件
    fun DOWNLOAD(@Path("path") path: String?): Call<ResponseBody?>?

}