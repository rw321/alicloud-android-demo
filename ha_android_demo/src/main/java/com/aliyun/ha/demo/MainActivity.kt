package com.aliyun.ha.demo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.alibaba.ha.adapter.AliHaAdapter
import com.alibaba.ha.adapter.service.tlog.TLogService
import com.aliyun.ha.demo.databinding.MainBinding
import com.aliyun.ha.demo.databinding.TestStepBinding
import com.aliyun.ha.demo.databinding.UpdateNickNameBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import crashreporter.motu.alibaba.com.tbcrashreporter4androiddemo.NativeCrashTest
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.NullPointerException


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

        binding.tvJavCrash.setOnClickListener {
            clickJavaCrash()
        }

        binding.tvNativeCrash.setOnClickListener {
            clickNativeCrash()
        }

        binding.tvMainThread.setOnClickListener {
            clickMainThreadBlock()
        }

        binding.tvCustomMistake.setOnClickListener {
            clickCustomMistake()
        }

        binding.tvIOException.setOnClickListener {
            clickIOException()
        }

        binding.tvPageLoad.setOnClickListener {
            clickPageLoad()
        }

        binding.tvPageScroll.setOnClickListener {
            clickPageScroll()
        }

        binding.tvNetRequest.setOnClickListener {
            clickNetRequest()
        }

        binding.tvPrintLog.setOnClickListener {
            clickPrintLog()
        }

        binding.tvUpdateNickName.setOnClickListener {
            clickUpdateNickName()
        }

        binding.tvTestStep.setOnClickListener {
            showTestStepDialog()
        }

    }

    /**
     * java crash
     */
    private fun clickJavaCrash(){
        throw NullPointerException()
    }

    /**
     * Native Crash
     */
    private fun clickNativeCrash(){
        val nativeCrashTest = NativeCrashTest()
        nativeCrashTest.TestNativeCrashMethod(1)
    }

    /**
     * 主线程卡顿
     */
    private fun clickMainThreadBlock(){
        try {
            Thread.sleep(20000)
        }catch (e: Exception) {
            Log.e("MainActivity", "main thread is blocking")
        }
    }

    /**
     * 自定义异常
     */
    private fun clickCustomMistake(){
        AliHaAdapter.getInstance().reportCustomError(NullPointerException("Custom Error"))
    }

    /**
     * IO异常
     */
    private fun clickIOException(){
        val file = File("test.txt")
        file.readText()
    }

    /**
     * 测页面加载
     */
    private fun clickPageLoad(){
        startActivity(Intent(this, PageLoadTestActivity::class.java))
    }

    /**
     * 测页面滑动
     */
    private fun clickPageScroll(){
        startActivity(Intent(this, PageSkateTestActivity::class.java))
    }

    /**
     * 网络请求
     */
    private fun clickNetRequest() = runBlocking{
        async {
            OkHttpClient.Builder().build().newCall(
                Request.Builder()
                    .url("https://help.aliyun.com/document_detail/2669178.html")
                    .build()
            ).enqueue(object: Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("MainActivity", "failure: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.e("MainActivity", "response: ${response.body?.string()}")
                }
            })
        }
    }

    /**
     * 打日志
     */
    private fun clickPrintLog(){
        TLogService.logv("main","MainActivity","test log upload")
    }

    /**
     * 更新昵称
     */
    private fun clickUpdateNickName(){
        val inputDialogBinding = UpdateNickNameBinding.inflate(LayoutInflater.from(this))

        val dialog = BottomSheetDialog(this, R.style.RoundedBottomSheetDialog).apply {

            setContentView(inputDialogBinding.root)
            inputDialogBinding.lifecycleOwner = this
            show()
        }
        inputDialogBinding.tvCancel.setOnClickListener { dialog.dismiss() }
        inputDialogBinding.tvConfirm.setOnClickListener {
            val nickName = inputDialogBinding.etNickName.text.toString().trim()
            if (TextUtils.isEmpty(nickName)) {
                Toast.makeText(this, getString(R.string.monitor_toast_input_nick_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AliHaAdapter.getInstance().updateUserNick(nickName)
            dialog.dismiss()
        }
    }

    private fun showTestStepDialog() {
        val testStepBinding = TestStepBinding.inflate(LayoutInflater.from(this))
        val dialog = BottomSheetDialog(this, R.style.RoundedBottomSheetDialog).apply {
            setContentView(testStepBinding.root)
            show()
        }
        testStepBinding.ivClose.setOnClickListener { dialog.dismiss() }
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