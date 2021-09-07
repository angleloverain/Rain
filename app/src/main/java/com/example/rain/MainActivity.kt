package com.example.rain

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.rain.base.BaseActivity
import com.example.rain.bean.BaseBean
import com.example.rain.databinding.ActivityMainBinding
import com.example.rain.dlg.InputDialog
import com.example.rain.model.MyViewModel
import com.example.rain.net.retrofit.RetrofitB
import com.example.rain.net.retrofit.RetrofitS
import com.example.rain.objectbox.bean.UserBean
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import com.example.rain.net.callback.StringCallBack as StringCallBack

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

    // kotlin  理解 lambda 到底是如何玩的
    // 还有高阶函数没能搞懂
    fun test() {
    }

    fun hell(a :Int,b : Int) : Int{
        return a+b
    }

    fun gaoJieHanSuTest(){

        val sum : (Int, Int) -> Int = { x: Int, y: Int -> x + y }

        val sum1 = { x: Int, y: Int -> x + y }

        val sum2 = { x: Int, y: Int, z : Int -> x + y + z}

        var items = listOf(1,2,3)
        // 在 Kotlin 中有一个约定：如果函数的最后一个参数是函数，那么作为相应参数传入的 lambda 表达式可以放在圆括号之外
        // fold 函数，第一个参数为初始的第一个值
        val product = items.fold(1) { abb, c -> abb * c }

        myFold(5){a,b -> a + b}
        myFold(5){a,b -> hell(a,b)}
    }

    public fun <R> myFold(initial: R,method :(a:R,R) -> R) : R{
        // 这里是传入一个简单的方法，或者一个lambda表达式
        // 然后处理这个方法中的，内部数据，然后给出，相应的结果
        var ok = method(initial,initial) // 这里类型不确定，很多操作都搞不了
        return initial
    }

     fun onResponse(call: Call<String>, response: Response<String>) {
         Log.i("dddd","回调成功方法 ： " + response.body())
    }

     fun onFailure(call: Call<String>, t: Throwable) {

    }


    //  Retrofit  要与协程一起使用才会变得简洁和简单
    fun httpTest(){

        RetrofitS.POST_BODY("hell", mapOf(Pair("hell","hell"))).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
            }
        })

        // 运用回调方式，就进行请求，就特么的麻烦
        RetrofitS.POST_BODY("hell", mapOf(Pair("hell","hell"))).enqueue(
            StringCallBack({a,b -> onResponse(a,b)},{a,b -> onFailure(a,b)}))

        // 这就同步，不能在主线程请求,这里就可以拿到返回结果
        // 但是这里可能会出现IO异常，网络异常，等等问题，需要自己封装，
        // 还是比较麻烦的
        // 这里是获得json 字符串，还需自己解析，
        var res = RetrofitS.POST_BODY("hell", mapOf(Pair("hell","hell"))).execute()
        // 这里会返回已个BaseBean,最终data数据在字段data里，需要做二次解析
        var userBean = RetrofitB.POST("hell").execute().body()
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

    // 使用 launch 不获得到返回值，  async 可以获取到返回，通过 await() 函数得到
    // 都可以等等待该任务结束，在执行下个一个任务
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

    // 协程channel,协程之间的通信使用
    fun channalTest(){
        // 这不管有没有接受者，都会发送数据
        val channel = Channel<Int>()
        mScope.launch {
            // 1. 创建 Channel
            val channel = produce<Int> {
                for (i in 1..3) {
                    delay(100)
                    send(i)//发送数据
                }
            }

            // 2. 接收数据
            launch {
                for (value in channel) { //for 循环打印接收到的值（直到渠道关闭）
                    print("接收 $value")
                }
            }
        }

    }

    // 协程flow的使用，
    fun FlowTest(){
        // 消费数据
        // flow的代码块只有调用collected()才开始运行
        mScope.launch {
            //1.创建一个Flow
            flow<Int> {
                for (i in 1..3) {
                    delay(200)
                    emit(i)  //2.发出数据
                }
            }.collect {
                // 当这个方法调用的时候，才开始执行flow闭包，
                // 所以可以，flow 执行到其他线程，
                // 让这方法执行到，主线程，那到数据，就更新到UI界面
                print("收集:$it")
            }
        }

        val flow = flow<Int> {
            for (i in 1..3) {
                delay(200)
                emit(i)//从流中发出值
            }
        }   // 以下都是上流操作符
            .filter { true }  // 用于过滤数据，返回一个bool类型
            .map { }          // 只做相关数据操作，最后返回发送的自己
            .transform {      // 用转换发送的数据类型，根据相关的逻辑
                emit("hell")
            }
            .catch {
                // 在这里可以捕获Flow的异常
            }
            .flowOn(Dispatchers.IO)  // 通过这个方法，可以切换数据发送线程
            .onCompletion {}      // 这个方法是数据发送完成回调
            .conflate()           // 保留最新值,
            .take(2)        //  限制获取的个数，比如现在是只获取2个
            .buffer(100)  //  添加缓存，可以存储更多没有处理的数据

        mScope.launch {
            // 以下就是末端操作符，也就是搜集工作
            // 且只能在协程中使用
            flow.collect {} // 基础的末端操作符
            var sum = flow.reduce{a, b -> a + b}  // 求和（末端操作符）
            flow.single() // 只发送一个值
            flow.first()  // 获取发送的第一个值
            var list = flow.toList() // 获取一个 list 集合
            var set = flow.toSet()   // 获取一个 set 集合
        }

        // 这里可以发送 JavaBean 对象
        val flow2 = flow<UserBean>{
            // 这里是一个网络请求，是耗时操作
            // 这flow 执行是在什么时候被执行，走这个代码
            var call = RetrofitS.POST_BODY("hell", mapOf(Pair("hell","hell"))).execute()
            if (call.isSuccessful){
                var name = call.body()
            }
            emit(UserBean())
        }


        mScope.async {
            // 与channel 一样，只能在协程里使用
            flow.collect {
                // 在其他协程，消费数据
            }

            flow.collectLatest{
                // 用这消费数据，只用最新发送的数据
            }

            flow2.collect {
                print(it.age)
            }
        }
    }

    // 协程的并发能力
    suspend fun asyncTest2() {
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
    // 闭包使用详解
    fun bibao(){
        // 改闭包循环执行三个
        repeat(3){}

        var user = BaseBean()
        // 指定T作为闭包的receiver,在函数范围内，可以任意调用该对象的方法，可以返回想返回的对象类型
        with(user){
            code = "000"
        }

        // 默认当前这个对象作为闭包的it参数，返回值是函数里最后一行或者指定return
        user.let{
           it.code = "000"
        }

        // 调用某对象的apply后，在函数范围内，可以任意调用该对象的方法，返回this
        var user2 = user.apply{
            code = "000"
        }

        // 与apply类似，返回同with，可以返回想返回的对象类型
        var r = "".run {
            1
        }

        // block 最后一行为返回值
        // 不是extension，执行block，返回block的返回
        var date = run {
            Date()
        }

        // 满足block中的条件，返回this，否则返回null，最后一行返回值需是Boolean类型
        var end1 = user.takeIf {
            1 > 2
        }

        // 与takeIf相反
        var end2 = user.takeUnless {
            1 > 2
        }
    }
    // 协程区间使用
    suspend fun guaqiHans(){
        // 挂起函数，只能在协程中调用
        // 只有在挂起函数中，才使用这两个作用域，
        coroutineScope {
            // 当其中任何一个job 发生异常，而未捕获的时候，异常会一向上，向下传播，停止整个协程的运行
        }

        supervisorScope{
           // 当其中任何一个job 发生异常，而未捕获的时候，异常只会向下传递，并停止当前的工作
        }
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
