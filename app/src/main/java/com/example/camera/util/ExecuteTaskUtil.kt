package com.example.camera.util

import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class ExecuteTaskUtil {

    private val TAG = "ExecuteTaskUtilLog"
    private var scheduleTaskExecutor: ScheduledExecutorService? = null

    constructor() {
        scheduleTaskExecutor = getInstance()
    }

    private fun getInstance(): ScheduledExecutorService?{
        return Executors.newSingleThreadScheduledExecutor()
    }

    fun start(callBack: CallBack, time: Long) {

        if (scheduleTaskExecutor?.isShutdown == true)
            scheduleTaskExecutor = getInstance()

        scheduleTaskExecutor?.scheduleAtFixedRate({
            Log.e(TAG, "TAKS: " + DateUtil().getHours())
            callBack.tasks()
        }, 0, time, TimeUnit.MINUTES)
    }

    fun stop() {
        if (scheduleTaskExecutor !== null) {
            Log.e(TAG, "Finish tasks")
            scheduleTaskExecutor?.shutdown()
        }
    }
}

interface CallBack {
    fun tasks()
}
