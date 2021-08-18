package com.example.rain.net.retrofit

import com.example.rain.bean.BaseBean
import com.example.rain.net.call.BeanRequest
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class RetrofitB : BaseRetrofit {

    private var call: BeanRequest? = null

    constructor() {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(initClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        call = retrofit?.create(BeanRequest::class.java)
    }


    fun GET(path: String?): Call<BaseBean?>? {
        return call?.GET(path)
    }

    fun GET(path: String?, from: Map<String?, String?>?): Call<BaseBean?>? {
        return call?.GET(path, from)
    }

    fun GET_PART(path: String?, params: Map<String?, RequestBody?>?): Call<BaseBean?>? {
        return call?.GET_PART(path, params)
    }

    fun POST(path: String?): Call<BaseBean?>? {
        return call?.POST(path)
    }

    fun POST(path: String?, from: Map<String?, String?>?): Call<BaseBean?>? {
        return call?.POST(path, from)
    }


    fun POST_BODY(path: String?, body: Map<String?, String?>?): Call<BaseBean?>? {
        return call?.POST_BODY(path, body)
    }

    fun POST_PART(path: String?, params: Map<String?, RequestBody?>?): Call<BaseBean?>? {
        return call?.POST_PART(path, params)
    }


    fun POST(
        path: String?,
        form: Map<String?, String?>?,
        body: Map<String?, String?>?
    ): Call<BaseBean?>? {
        return call?.POST(path, form, body)
    }


    fun DELETE(path: String?, params: Map<String?, RequestBody?>?): Call<BaseBean?>? {
        return call?.DELETE(path, params)
    }

    fun PUT(path: String?, params: Map<String?, RequestBody?>?): Call<BaseBean?>? {
        return call?.PUT(path, params)
    }

    // 上传
    fun UPLOAD(path: String?, params: Map<String?, RequestBody?>?): Call<ResponseBody?>? {
        return call?.UPLOAD(path, params)
    }

    // 下载
    fun DOWNLOAD(path: String?): Call<ResponseBody?>? {
        return call?.DOWNLOAD(path)
    }
}