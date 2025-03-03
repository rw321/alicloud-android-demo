package com.alibaba.push.android.demo.java;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTSManager {

    private TextToSpeech mTextToSpeech;
    private TTSManager(){}

    private static class SingletonHolder{
        private static final TTSManager INSTANCE = new TTSManager();
    }

    public static TTSManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private static final String TAG = "TTSManager";

    public void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTextToSpeech.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        mTextToSpeech.setLanguage(Locale.US);
                    }
                    mTextToSpeech.setPitch(1.0f);
                    mTextToSpeech.setSpeechRate(1.0f);
                }
            });
        }
    }

    public void speak(String text) {
        if (mTextToSpeech != null) {
            if (mTextToSpeech.isSpeaking()) {
                mTextToSpeech.stop();
            }
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "");
        }
    }

}
