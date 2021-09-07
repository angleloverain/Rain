package com.example.rain.base

import android.util.Log
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Response

open class BaseViewModel<T> : ViewModel(){
    // 这里面要个有网络工作管理者 用于获取数据
    // 数据库管理者 用于缓存数据

   open fun onResponse(call: Call<T>, response: Response<String>) {
        Log.i("dddd","回调成功方法 ： " + response.body())
    }

   open fun onFailure(call: Call<T>, t: Throwable) {

    }
}