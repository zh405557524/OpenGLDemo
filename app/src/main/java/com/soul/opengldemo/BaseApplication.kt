package com.soul.opengldemo

import android.app.Application
import com.soul.lib.BuildConfig
import com.soul.lib.Global

/**
 * Description: 程序入口
 * Author: 祝明
 * CreateDate: 2024/6/6 18:06
 * UpdateUser:
 * UpdateDate: 2024/6/6 18:06
 * UpdateRemark:
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Global.init(this, true)
    }
}