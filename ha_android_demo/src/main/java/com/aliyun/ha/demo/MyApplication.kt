package com.aliyun.ha.demo

import android.app.Application
import android.util.Log
import com.alibaba.ha.adapter.AliHaAdapter
import com.alibaba.ha.adapter.AliHaConfig
import com.alibaba.ha.adapter.Plugin
import com.alibaba.ha.adapter.service.tlog.TLogLevel
import com.alibaba.ha.adapter.service.tlog.TLogService
import com.alibaba.sdk.android.networkmonitor.NetworkMonitorManager
import com.alibaba.sdk.android.networkmonitor.utils.Logger

/**
 * @author ren
 * @date 2024-11-18
 */
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initMonitor()
    }

    private fun initMonitor(){
        val config = AliHaConfig().apply {
            appKey = "335156672"
            appVersion = BuildConfig.VERSION_NAME
            appSecret = "c7ccbf3f45434f1ba17974fb000caa06"
            channel = "mqc_test"
            userNick = null
            application = this@MyApplication
            context = applicationContext
            isAliyunos = false
            rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCT45cUkm4ZnW9vgUEtrKAjRY+kzyDdPtfhIinYcJCIiNLECLrecycBUK6thxnIHrrnqWFn05TCoLgF+XtnlKY+GGPwAKGwc5zrkqzfrN1tto3o6yRbYPAeePDAwfJyZHz5ZJkIouwN/2xA3q4yYfGy6STD5rhBBaSPqNrAEJ0hJwIDAQAB"
        }

        AliHaAdapter.getInstance().addCustomInfo("custom", "value")
        AliHaAdapter.getInstance().setErrorCallback{
            HashMap()
        }
        AliHaAdapter.getInstance().addPlugin(Plugin.crashreporter) //崩溃分析，如不需要可注释掉

        AliHaAdapter.getInstance().addPlugin(Plugin.apm) //性能监控，如不需要可注释掉

        AliHaAdapter.getInstance().addPlugin(Plugin.tlog) //移动日志，如不需要可注释掉

        AliHaAdapter.getInstance().openDebug(true) //调试日志开关

        TLogService.updateLogLevel(TLogLevel.VERBOSE)

        AliHaAdapter.getInstance().start(config) //启动

        NetworkMonitorManager.getInstance().addLogger(object : Logger {
            override fun logd(s: String, s1: String) {
                Log.d(s, s1)
            }

            override fun logi(s: String, s1: String) {
                Log.i(s, s1)
            }

            override fun logw(s: String, s1: String) {
                Log.w(s, s1)
            }
        })

    }
}