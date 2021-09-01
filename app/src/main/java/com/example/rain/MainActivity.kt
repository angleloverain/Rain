package com.example.rain

import android.Manifest
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.rain.base.BaseActivity
import com.example.rain.bean.BaseBean
import com.example.rain.databinding.ActivityMainBinding
import com.example.rain.dlg.InputDialog
import com.example.rain.model.MyViewModel
import com.example.rain.net.retrofit.RetrofitS
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding, MyViewModel>(){

    // kotlin 所有变量都被初始化
    // 当变量定义为nullable类型时，在使用其变量时需要加上操作符 ? 或者 !! 。
    // "?"加在变量名后，系统在任何情况不会报它的空指针异常。
    // "!!"加在变量名后，如果对象为null，那么系统一定会报异常！
    private var img : String? = null
    // 使用 lateinit 关键字，变量在定义时不需要初始化
    // 可以 和 java  一样的使用
    private lateinit var tv :TextView
    // lateinit 只用于变量 var，而 lazy 只用于常量 val
    // val nameTextView by lazy { findViewById<TextView>(R.id.text) }

    // Custom getter setter  这个还不太明白   ******
    // 每次anotherTextView变量被使用时，都会调用findViewById函数。
    // val anotherTextView: TextView get() = findViewById(R.id.text)

    // private lateinit var binding:ActivityMainBinding

    /**
     * 使用官方库的 MainScope()获取一个协程作用域用于创建协程
     */
    private val mScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tv = findViewById(R.id.text)
        tv.let { it.setText("jump to game Activity") }
        requestPermissions()
        layout.text.setOnClickListener(View.OnClickListener {
            InputDialog().show(supportFragmentManager,"input")
        })


        // 注册监听回调
        model.getUsers().observe(this, Observer {
            layout.text.setText(it.name)
        })

        model.loadData()

        test()
    }

    // kotlin
    fun test() {
    }

    fun httpTest(){

        RetrofitS.POST_BODY("hell", mapOf(Pair("hell","hell"))).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })
    }

    // 先学习一下，kotlin的协成使用，然后封装一下，给java代码提供使用
    fun fetchDocs() {
        val job1 = mScope.launch {
            // 这里就是协程体
            // 延迟1000毫秒  delay是一个挂起函数
            // 在这1000毫秒内该协程所处的线程不会阻塞
            // 协程将线程的执行权交出去，该线程该干嘛干嘛，到时间后会恢复至此继续向下执行
            delay(1000)
        }

        // 创建一个指定了调度模式的协程，该协程的运行线程为IO线程
        val job2 = mScope.launch(Dispatchers.IO) {
            // 此处是IO线程模式
            // 切线程 将协程所处的线程环境切至指定的调度模式Main
            withContext(Dispatchers.Main) {
                // 现在这里就是Main线程了  可以在此进行UI操作了
            }
        }

        // 下面直接看一个例子： 从网络中获取数据  并更新UI
        // 该例子不会阻塞主线程
        mScope.launch(Dispatchers.IO) {
            // 执行getUserInfo方法时会将线程切至IO去执行
            val userInfo = get("hellword")
            // 获取完数据后 切至Main线程进行更新UI
            withContext(Dispatchers.Main) {
                // 更新UI
            }
        }
    }

    fun asyncTest() {
        mScope.launch {
            // 开启一个IO模式的线程 并返回一个Deferred，Deferred可以用来获取返回值
            // 代码执行到此处时会新开一个协程 然后去执行协程体  父协程的代码会接着往下走
            val deferred = async(Dispatchers.IO) {
                // 模拟耗时
                delay(2000)
                // 返回一个值
                "Quyunshuo"
            }
            // 等待async执行完成获取返回值 此处并不会阻塞线程  而是挂起 将线程的执行权交出去
            // 等到async的协程体执行完毕后  会恢复协程继续往下执行
            Log.i("dddd","开始等待")
            val date = deferred.await()
            Log.i("dddd", "执行结束 ： $date")
        }
    }

    // 协程的并发能力
    fun asyncTest2() {
        mScope.launch {
            // 此处有一个需求  同时请求5个接口  并且将返回值拼接起来

            val job1 = async {
                // 请求1
                delay(5000)
                "1"
            }
            val job2 = async {
                // 请求2
                delay(5000)
                "2"
            }
            val job3 = async {
                // 请求3
                delay(5000)
                "3"
            }
            val job4 = async {
                // 请求4
                delay(5000)
                "4"
            }
            val job5 = async {
                // 请求5
                delay(5000)
                "5"
            }

            // 代码执行到此处时  5个请求已经同时在执行了
            // 等待各job执行完 将结果合并
            Log.d(
                "TAG",
                "asyncTest2: ${job1.await()} ${job2.await()} ${job3.await()} ${job4.await()} ${job5.await()}"
            )
            // 因为我们设置的模拟时间都是5000毫秒  所以当job1执行完时  其他job也均执行完成
        }

        //创建作用域
        val scope = CoroutineScope(Dispatchers.Main)
        //启动一个协程
        val job = scope.launch {
            //TODO
        }
        //协程job1将会被取消，而另一个job2则不受任何影响
        val job1 = scope.launch {
            //TODO
        }
        val job2 = scope.launch {
            //TODO
        }

        // 取消单个工作
        job.cancel()
        // 作用域取消,将会取消所有工作
        scope.cancel()
    }



    // 这些语法，真的很奇怪
    suspend fun get(url: String) : Int{
        return withContext(Dispatchers.IO){
            delay(2000)
            555 // 这地要填返回值，这逻辑不太理解
        }
    }

    private fun requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, 101)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // 取消协程 防止协程泄漏  如果使用lifecycleScope则不需要手动取消
        mScope.cancel()
    }
}
