package com.example.rain.net.callback

import com.example.rain.bean.BaseBean
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class BaseBeanCallBack : Callback<BaseBean> {

    override fun onResponse(call: Call<BaseBean>, response: Response<BaseBean>) {

    }

    override fun onFailure(call: Call<BaseBean>, t: Throwable) {

    }
}