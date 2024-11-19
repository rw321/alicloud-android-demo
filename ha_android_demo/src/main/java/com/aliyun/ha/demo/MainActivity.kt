package com.aliyun.ha.demo

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.aliyun.ha.demo.databinding.MainBinding


/**
 * main activity
 * @author ren
 * @date 2024/11/18
 */
class MainActivity : AppCompatActivity() {

    private var mBackKeyPressedTime = 0L

    private lateinit var binding:MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //浸入状态栏
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        controller.isAppearanceLightStatusBars = true
        //设置状态栏透明
        window.statusBarColor = Color.TRANSPARENT

        binding = MainBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - mBackKeyPressedTime > 2000) {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.monitor_toast_exit),
                Toast.LENGTH_SHORT
            ).show()
            mBackKeyPressedTime = System.currentTimeMillis()
        } else {
            finish()
        }
    }
}