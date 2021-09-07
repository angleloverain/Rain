package com.example.rain.net.callback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class StringCallBack(
    onSuccess: (call: Call<String>, response: Response<String>) -> Unit,
    onError: (call: Call<String>, t: Throwable) -> Unit
) : Callback<String> {

    var  onSuccess : (call: Call<String>, response: Response<String>) -> Unit = onSuccess
    var  onError : (call: Call<String>, t: Throwable) -> Unit = onError

    override fun onResponse(call: Call<String>, response: Response<String>) {
        onSuccess(call,response)
    }

    override fun onFailure(call: Call<String>, t: Throwable) {
        onError(call,t)
    }

}