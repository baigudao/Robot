package com.taisau.robot.main.interfacej;

import com.taisau.robot.main.wakeup.WakeUpResult;

/**
 * Created by Devin on 2018/7/10.
 */
public interface IWakeupListener {

    void onSuccess(String word, WakeUpResult result);

    void onStop();

    void onError(int errorCode, String errorMessge, WakeUpResult result);

    void onASrAudio(byte[] data, int offset, int length);
}
