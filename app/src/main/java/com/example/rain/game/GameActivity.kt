package com.example.rain.game

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentManager
import com.example.rain.R
import com.example.rain.base.BaseActivity
import com.example.rain.base.BaseViewModel
import com.example.rain.databinding.ActivityGameBinding

class GameActivity : BaseActivity<ActivityGameBinding, BaseViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layout.apply {

            btnSetting.setOnClickListener(View.OnClickListener {
                // 设置
                var fragment  = GameFragment()
                var fragmentManager : FragmentManager = supportFragmentManager
                var fragmentT  = fragmentManager.beginTransaction()
                fragmentT.add(R.id.game_frame,fragment,"hell")
                fragmentT.commit()
            })

            btnTakePlayGame.setOnClickListener(View.OnClickListener {
                // 接管对话

            })

            btnReconPlayGame.setOnClickListener(View.OnClickListener {
                // 重连游戏

            })

            start.setOnClickListener(View.OnClickListener {
                // 启动新游戏
            })
        }
    }
}