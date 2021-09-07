package com.example.rain.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.example.rain.base.BaseViewModel
import com.example.rain.bean.BaseBean
import com.example.rain.net.retrofit.RetrofitB
import com.example.rain.objectbox.bean.UserBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class MyViewModel : BaseViewModel<String>() {

    /*  ViewModel 设计原则
       1.
        一个视图里面(activity或者某个fragment) 只有一个 ViewModel
        一个ViewModel 里面可以有多个 LiveData 来对应，相关组件的数据模型

       2.然后利用 ViewModel 作为桥接，确定数据和视图的逻辑关系，

       3.数据的获取，以及存储都放在 ViewModel 里面进行处理
         那么数据的初始和获取，在哪里处理，和获取呢？
     */

    // 直接出初始化 liveData
    private var users: MutableLiveData<UserBean> =  MutableLiveData<UserBean>()

    // 直接初始化 stateFlow
    private var selected = MutableStateFlow(false)
    // 可以用赋值的方式，进行发送数据
    fun doSomeThing(value: Boolean) {
        selected.value = value
    }


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
                var call =  RetrofitB.POST("hell").execute()
                if (call.isSuccessful){
                    Log.i("dddd"," 成功 ")
                    var baseBean = call.body()
                    Log.i("dddd"," data " + baseBean?.data)
                    var userBean = JSON.parseObject(baseBean?.data,UserBean::class.java)
                    emit(userBean)
                }else{
                    Log.i("dddd"," 请求异常 ")
                }
            }.catch {
                // 当出现异常的时候
            }.flowOn(Dispatchers.IO).collect {
                Log.i("dddd","Thread name 2 : " + Thread.currentThread().name)
                Log.i("dddd","收集:${it.name}")
                users.postValue(it)
            }

            selected.collect{
                // 这里是主线程
                // 修改UI的变化，
            }


            flow<LatestNewsUiState> { // 这个是在子线程工作
                emit(LatestNewsUiState.Success(emptyList()))
            }.catch{e -> emit(LatestNewsUiState.Error(e))
            }.flowOn(Dispatchers.IO).collect {
                when(it){
                    is LatestNewsUiState.Success -> showFavoriteNews(it.news)
                    is LatestNewsUiState.Error -> showError(it.exception)
                }
            }

        }

    }

    fun showFavoriteNews(news: List<BaseBean>){

    }

    fun showError(exception: Throwable){

    }

    val uiState = MutableStateFlow(LatestNewsUiState.Success(emptyList()))

    // Represents different states for the LatestNews screen
    // 这实际上就是一个枚举内型
    sealed class LatestNewsUiState {
        data class Success(var news: List<BaseBean>): LatestNewsUiState()
        data class Error(var exception: Throwable): LatestNewsUiState()
    }

}