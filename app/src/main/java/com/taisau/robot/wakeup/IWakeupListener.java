package com.taisau.robot.wakeup;

/**
 * Created by Devin on 2018/7/12.
 */
public interface IWakeupListener {

    void onSuccess(String word, WakeUpResult result);

    void onStop();

    void onError(int errorCode, String errorMessge, WakeUpResult result);

    void onASrAudio(byte[] data, int offset, int length);
}
