package com.taisau.robot.wakeup;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;

import com.baidu.speech.asr.SpeechConstant;
import com.blankj.utilcode.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Devin on 2018/7/12.
 */
public class ActivityWakeUp extends BaseActivity implements IStatus {

    private static final String TAG = "ActivityWakeUp";
    protected MyWakeup myWakeup;

    private int status = STATUS_NONE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initRecog() {
        IWakeupListener listener = new RecogWakeupListener(handler);
        // 改为 SimpleWakeupListener 后，不依赖handler，但将不会在UI界面上显示
        myWakeup = new MyWakeup(this, listener);
    }

    protected void start() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }

    protected void stop() {
        myWakeup.stop();
    }

    @Override
    protected void onDestroy() {
        myWakeup.release();
        super.onDestroy();
    }

    @Override
    protected void handleMsg(Message msg) {
        super.handleMsg(msg);
        if (msg.what == STATUS_WAKEUP_SUCCESS) {
            LogUtils.e("唤醒成功   >>>");
        }
    }
}
