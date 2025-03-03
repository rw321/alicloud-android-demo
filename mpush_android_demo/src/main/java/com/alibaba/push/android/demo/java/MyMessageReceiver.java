package com.alibaba.push.android.demo.java;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;

import java.util.Map;

public class MyMessageReceiver extends MessageReceiver {
    @Override
    protected void onNotification(Context context, String title, String content, Map<String, String> map) {
        if (map != null && map.containsKey("ttsContent")){
            String ttsContent = map.get("ttsContent");
            if (!TextUtils.isEmpty(ttsContent)) {
                TTSManager.getInstance().speak(ttsContent);
            }
        }
    }

    @Override
    protected void onMessage(Context context, CPushMessage message) {
        if (message != null) {
            TTSManager.getInstance().speak(message.getContent());
        }
    }

    @Override
    protected void onNotificationOpened(Context context, String s, String s1, String s2) {

    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String s, String s1, String s2) {

    }

    @Override
    protected void onNotificationRemoved(Context context, String s) {

    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String s, String s1, Map<String, String> map, int i, String s2, String s3) {

    }
}
