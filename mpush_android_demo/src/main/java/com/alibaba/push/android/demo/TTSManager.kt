package com.alibaba.push.android.demo

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TTSManager {

    private var textToSpeech: TextToSpeech? = null

    fun init(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    Log.d("TTSManager", "init success")
                    val languageCode = textToSpeech?.setLanguage(Locale.CHINESE)
                    if (languageCode == TextToSpeech.LANG_NOT_SUPPORTED || languageCode == TextToSpeech.LANG_MISSING_DATA) {
                        //语音包未安装或者不支持
                        Log.d("TTSManager", "Language not supported")
                        textToSpeech?.language = Locale.US
                    }else {
                        Log.d("TTSManager", "support chinese")
                    }
                    textToSpeech?.setPitch(1.0f)
                    textToSpeech?.setSpeechRate(1.0f)
                }
            }
        }
    }

    fun isSpeaking() = textToSpeech?.isSpeaking ?: false

    fun stop() = textToSpeech?.stop()

    fun speak(text: String) {
        if (textToSpeech?.isSpeaking == true) {
            textToSpeech?.stop()
        }
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}