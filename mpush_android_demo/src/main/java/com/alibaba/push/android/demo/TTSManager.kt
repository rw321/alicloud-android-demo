package com.alibaba.push.android.demo

import android.content.Context
import android.os.Build
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

object TTSManager {

    private var mTextToSpeech: TextToSpeech? = null

    fun init(context: Context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech = TextToSpeech(context) {
                if (it == TextToSpeech.SUCCESS) {
                    val languageCode = mTextToSpeech?.setLanguage(Locale.CHINESE)
                    if (languageCode == TextToSpeech.LANG_NOT_SUPPORTED || languageCode == TextToSpeech.LANG_MISSING_DATA) {
                        //语音包未安装或者不支持
                        mTextToSpeech?.language = Locale.US
                    }
                    mTextToSpeech?.setPitch(1.0f)
                    mTextToSpeech?.setSpeechRate(1.0f)
                }
            }
        }
    }

    fun speak(text: String) {
        if (mTextToSpeech?.isSpeaking == true) {
            mTextToSpeech?.stop()
        }
        mTextToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

}