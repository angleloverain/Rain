package com.example.rain

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.example.rain.base.BaseActivity
import com.example.rain.databinding.ActivityMainBinding
import com.example.rain.dlg.InputDialog
import com.example.rain.model.MyViewModel
import com.example.rain.net.retrofit.RetrofitS
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

    fun test() {
        var map: Map<String, String> = HashMap();
        map.apply {
            plus(Pair("hell", "hell"))
            plus(Pair("hell", "hell"))
            plus(Pair("hell", "hell"))
        }
        RetrofitS().POST_BODY("hell",map).enqueue(Callback<String>{

        })
    }

    private fun requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            var permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, 101)
        }
    }
}