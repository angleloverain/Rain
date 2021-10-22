package com.example.rain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class TickHandler(
    private var externalScope: CoroutineScope,
    private var tickIntervalMs: Long = 2000,
    private var testint : Int = 0
) {
    // 这三参数，就是shardFlow 与 stateFlow最大的区别，其他都一样
    val sharedFlow = MutableSharedFlow<Int>(
        5                       // 参数一：当新的订阅者Collect时，发送几个已经发送过的数据给它
        , 3           // 参数二：减去replay，MutableSharedFlow还缓存多少数据
        , BufferOverflow.DROP_LATEST  // 参数三：缓存策略，三种 丢掉最新值、丢掉最旧值和挂起
    )

    init {
        // 这里代码块，会在创建该类之后执行
        externalScope.launch {
            while(true) {
                sharedFlow.emit(testint++)
                delay(tickIntervalMs)
            }
        }
    }

    companion object {

        init {
            // 这里的代码，会在创建该类之前执行
        }

        // 在这里面，就类似，java 的静态块
        fun hell(){}
        var index = 5
    }
}