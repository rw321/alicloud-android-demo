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
            appKey = "335189575"
            appVersion = BuildConfig.VERSION_NAME
            appSecret = "4a120826c1e7497d9d0e24983c2dd65e"
            channel = "mqc_test"
            userNick = null
            application = this@MyApplication
            context = applicationContext
            isAliyunos = false
            rsaPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDzCnEudGjuyEOG6tgGZPMmEih6mtFYATgPry8VpdnDI5kuN2ZW0GSb8SGoWUsrbe2Dv99rtZRW3xTqlKSB/nv5K2Hy5tAdlRa9Fr23h7sg2WZevImWLO+8EIVmCcECI2HRJQ0IZ8IXC/Vene9cpkXnrXnculxiOLoPRqfawQEZcwIDAQAB"
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