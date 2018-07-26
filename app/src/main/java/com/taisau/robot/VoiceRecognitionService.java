package com.taisau.robot;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.speech.RecognitionService;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Devin on 2018/6/20.
 */
public final class VoiceRecognitionService extends RecognitionService {

    public static final String TAG = "VoiceRecognitionService";
    public static final String VERSION_NAME = "3.4.1.100";
    public static final int EVENT_ENGINE_SWITCH = 12;
    private static final Logger logger = Logger.getLogger("VoiceRecognitionService");
    private static final int EVENT_ERROR = 11;
    private static final int EVENT_THIRD_DATA = 12;
    private boolean internal;
    private EventManager mEventManagerAsr;
    private VoiceRecognitionService.MyListener mUsingListener;
    private Bundle mFinalBundle;
    private boolean mLongSpeech;

    public VoiceRecognitionService() {
        Log.e(TAG,"VoiceRecognitionService      onCreate>>>");
    }

    public static String getSdkVersion() {
        return "3.4.1.100";
    }

    public static Bundle fromJson(JSONObject var0) {
        Bundle var1 = new Bundle();
        Iterator var2 = var0.keys();

        while (true) {
            while (var2.hasNext()) {
                String var3 = (String) var2.next();
                JSONArray var4 = var0.optJSONArray(var3);
                String var5 = var0.optString(var3);
                if (var4 != null && var4.length() <= 0) {
                    var1.putStringArray(var3, new String[0]);
                } else {
                    int var7;
                    if (var4 != null && var4.optString(0) != null) {
                        ArrayList var8 = new ArrayList();

                        for (var7 = 0; var7 < var4.length(); ++var7) {
                            var8.add(var4.optString(var7));
                        }

                        var1.putStringArrayList(var3, var8);
                    } else if (var4 != null && !Double.isNaN(var4.optDouble(0))) {
                        double[] var6 = new double[var4.length()];

                        for (var7 = 0; var7 < var4.length(); ++var7) {
                            var6[var7] = var4.optDouble(var7);
                        }

                        var1.putDoubleArray(var3, var6);
                    } else if (var5 != null) {
                        var1.putString(var3, var5);
                    }
                }
            }

            return var1;
        }
    }

    public void onCreate() {
        super.onCreate();
        Class var1 = VoiceRecognitionService.class;
        synchronized (VoiceRecognitionService.class) {
            if (null == this.mEventManagerAsr) {
                this.mEventManagerAsr = EventManagerFactory.create(this.getApplicationContext(), "asr");
                this.mUsingListener = new VoiceRecognitionService.MyListener();
                this.mEventManagerAsr.registerListener(this.mUsingListener);
                SpeechConstant.PUBLIC_DECODER = false;
            }
        }

        logger.info(String.format("onCreate(), hashcode=%s", this.hashCode()));

        try {
            Class.forName("com.baidu.android.voicedemo.SettingMore");
            this.internal = true;
        } catch (Exception var4) {

        }

        logger.info("internal=" + this.internal);

        try {
            ServiceInfo var6 = this.getPackageManager().getServiceInfo(new ComponentName(this.getPackageName(), this.getClass().getName()), 128);
            boolean var2 = var6.exported;
            if (var2) {
                throw new AndroidRuntimeException(this.getClass().getName() + ", 'android:exported' should be false, please modify AndroidManifest.xml");
            }
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

    }

    private JSONObject convertIntentToJson(Intent var1) {
        HashMap var2 = new HashMap();
        var1.getStringExtra("a");
        Bundle var3 = var1.getExtras();
        Iterator var4 = var3.keySet().iterator();

        while (true) {
            while (var4.hasNext()) {
                String var5 = (String) var4.next();
                Object var6 = var3.get(var5);
                if (var5.equals("args") && var6 instanceof String) {
                    String var7 = (String) var6;
                    String[] var8 = var7.split("--");
                    String[] var9 = var8;
                    int var10 = var8.length;

                    for (int var11 = 0; var11 < var10; ++var11) {
                        String var12 = var9[var11];
                        int var13 = var12.trim().indexOf(" ");
                        if (var13 < 0) {
                            var13 = var12.indexOf("\t");
                        }

                        if (var13 < 0) {
                            var13 = var12.indexOf("=");
                        }

                        if (var13 > 0) {
                            String var14 = var12.substring(0, var13).trim();
                            String var15 = var12.substring(var13 + 1).trim();
                            var2.put(var14, var15);
                        }
                    }
                } else {
                    var2.put(var5, var6);
                }
            }

            return new JSONObject(var2);
        }
    }

    protected void onStartListening(Intent var1, Callback var2) {
        if (!var1.hasExtra("audio.mills")) {
            var1.putExtra("audio.mills", System.currentTimeMillis());
        }

        this.mLongSpeech = var1.getIntExtra("vad.endpoint-timeout", -1) == 0;
        JSONObject var3 = this.convertIntentToJson(var1);

        try {
            this.mUsingListener.setCallbackListener(var2);
            int var4 = var1.getIntExtra("decoder", 0);
            if (var4 != 0) {
                this.mEventManagerAsr.send("asr.kws.load", var3.toString(4), (byte[]) null, 0, 0);
            }

            this.mEventManagerAsr.send("asr.start", var3.toString(4), (byte[]) null, 0, 0);
        } catch (JSONException var5) {
            var5.printStackTrace();
        }

    }

    protected void onStopListening(Callback var1) {
        this.mEventManagerAsr.send("asr.stop", "{}", (byte[]) null, 0, 0);
    }

    protected void onCancel(Callback var1) {
        this.mEventManagerAsr.send("asr.cancel", "{}", (byte[]) null, 0, 0);
    }

    public void onDestroy() {
        this.mEventManagerAsr.send("asr.kws.unload", "{}", (byte[]) null, 0, 0);
        super.onDestroy();
    }

    class MyListener implements EventListener {
        Callback mListener;

        MyListener() {
        }

        public void setCallbackListener(Callback var1) {
            this.mListener = var1;
        }

        public void onEvent(String var1, String var2, byte[] var3, int var4, int var5) {
            Callback var6 = this.mListener;
            if (var6 != null) {
                try {
                    if ("asr.ready".equals(var1)) {
                        var6.readyForSpeech(new Bundle());
                    } else if ("asr.begin".equals(var1)) {
                        var6.beginningOfSpeech();
                    } else if ("asr.audio".equals(var1)) {
                        var6.bufferReceived(var3);
                    } else {
                        JSONObject var7;
                        if ("asr.volume".equals(var1)) {
                            var7 = new JSONObject(var2);
                            double var8 = var7.optDouble("volume");
                            var6.rmsChanged((float) var8);
                        } else if ("asr.end".equals(var1)) {
                            var6.endOfSpeech();
                        } else {
                            Bundle var9;
                            if ("asr.partial".equals(var1)) {
                                var7 = new JSONObject(var2);
                                String var13 = var7.optString("result_type");
                                var9 = VoiceRecognitionService.fromJson(var7);
                                if (var13 != null && var13 != "") {
                                    if (var13.equals("partial_result")) {
                                        var6.partialResults(var9);
                                    } else if (var13.equals("final_result")) {
                                        VoiceRecognitionService.this.mFinalBundle = var9;
                                    } else if (var13.equals("third_result")) {
                                        Bundle var10 = new Bundle();
                                        var10.putByteArray("third_data", var3);
                                        this.callbackOnEvent(var6, 12, var10);
                                    }
                                }
                            } else if ("asr.finish".equals(var1)) {
                                var7 = new JSONObject(var2);
                                int var14 = var7.getInt("error");
                                if (var14 != 0) {
                                    var6.error(var14);
                                    var9 = new Bundle();
                                    var9.putInt("error", var7.getInt("sub_error"));
                                    var9.putString("reason", var7.getString("desc"));
                                    this.callbackOnEvent(var6, 11, var9);
                                } else if (!VoiceRecognitionService.this.mLongSpeech) {
                                    var6.results(VoiceRecognitionService.this.mFinalBundle);
                                    VoiceRecognitionService.this.mFinalBundle = null;
                                }
                            } else if ("asr.long-speech.finish".equals(var1)) {
                                var6.results(VoiceRecognitionService.this.mFinalBundle);
                                VoiceRecognitionService.this.mFinalBundle = null;
                            }
                        }
                    }
                } catch (RemoteException var11) {
                    var11.printStackTrace();
                } catch (JSONException var12) {
                    var12.printStackTrace();
                }

            }
        }

        private final void callbackOnEvent(Callback var1, int var2, Bundle var3) {
            try {
                Field var4 = var1.getClass().getDeclaredField("mListener");
                var4.setAccessible(true);
                Class var5 = Class.forName("android.speech.IRecognitionListener");
                Method var6 = var5.getMethod("onEvent", Integer.TYPE, Bundle.class);
                var6.invoke(var4.get(var1), var2, var3);
            } catch (Exception var7) {
                var7.printStackTrace();
                VoiceRecognitionService.logger.log(Level.WARNING, "", var7);
            }

        }
    }
}
