package com.example.rain.model

import android.renderscript.Sampler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rain.base.BaseViewModel
import com.example.rain.objectbox.bean.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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

    // 直接出初始化
    private var users: MutableLiveData<UserBean> =  MutableLiveData<UserBean>()


    fun getUsers(): MutableLiveData<UserBean> {
        return users
    }

    // 提供一个加载数据的方法
    fun loadData(){
        viewModelScope.launch { // 这里只是开启了协程空间
            Log.i("dddd","Thread name : " + Thread.currentThread().name)
            // 在这里执行的协程，还是主线程上，不能进行耗时操作,
            flow {
                Log.i("dddd","Thread name 1 : " + Thread.currentThread().name)
                for (i in 1..3) {
                    delay(2000)
                    var user = UserBean();
                    user.name = "view model$i"
                    emit(user)  //2.发出数据
                }
            }.flowOn(Dispatchers.IO).collect {
                Log.i("dddd","Thread name 2 : " + Thread.currentThread().name)
                Log.i("dddd","收集:${it.name}")
                users.postValue(it)
            }
        }

    }

}