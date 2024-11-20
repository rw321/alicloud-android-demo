package com.aliyun.ha.demo

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

class PageSkateTestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //浸入状态栏
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        controller.isAppearanceLightStatusBars = true
        //设置状态栏透明
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.monitor_activity_page_skate)

        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }
    }

}