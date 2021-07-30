package com.example.rain.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rain.base.BaseViewModel
import com.example.rain.bean.UserBean
import kotlin.reflect.KProperty

class MyViewModel : BaseViewModel() {

    /*  ViewModel 设计原则
       1.
       一个视图里面(activity或者某个fragment) 只有一个 ViewModel
       一个ViewModel 里面可以有多个 LiveData 来对应，相关组件的数据模型
       2.
         然后利用 ViewModel 作为桥接，确定数据和视图的逻辑关系，

       3.数据的获取，以及存储都放在 ViewModel 里面进行处理
         那么数据的初始和获取，在哪里处理，和获取呢？
     */

    private lateinit var users: MutableLiveData<UserBean>

    fun getUsers(): LiveData<UserBean> {
        return users
    }



}