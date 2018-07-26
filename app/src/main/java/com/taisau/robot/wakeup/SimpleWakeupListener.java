package com.taisau.robot.wakeup;

import com.blankj.utilcode.util.LogUtils;

/**
 * Created by Devin on 2018/7/12.
 */
public class SimpleWakeupListener implements IWakeupListener {

    private static final String TAG = "SimpleWakeupListener";

    @Override
    public void onSuccess(String word, WakeUpResult result) {
        LogUtils.e("_lhl_", "唤醒成功，唤醒词：" + word);
    }

    @Override
    public void onStop() {
        LogUtils.i("_lhl_", "唤醒词识别结束：");
    }

    @Override
    public void onError(int errorCode, String errorMessge, WakeUpResult result) {
        LogUtils.i("_lhl_", "唤醒错误：" + errorCode + ";错误消息：" + errorMessge + "; 原始返回" + result.getOrigalJson());
    }

    @Override
    public void onASrAudio(byte[] data, int offset, int length) {
        LogUtils.e("_lhl_", "audio data： " + data.length);
    }
}
