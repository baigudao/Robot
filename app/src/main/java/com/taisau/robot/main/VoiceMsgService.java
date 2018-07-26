package com.taisau.robot.main;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.AVChatProfile;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.taisau.robot.interfacej.OnSpeakListener;
import com.taisau.robot.interfacej.OnStartListeningListener;
import com.taisau.robot.interfacej.OnStopListeningListener;
import com.taisau.robot.interfacej.OnWakeupStartListener;
import com.taisau.robot.interfacej.OnWakeupStopListener;
import com.taisau.robot.interfacej.ThirdPartyDataImpl;
import com.taisau.robot.main.activity.MainActivity;
import com.taisau.robot.protocol.RobotProtocol;
import com.taisau.robot.utils.AutoCheck;
import com.taisau.robot.utils.AutoCheck1;
import com.taisau.robot.utils.Constant;
import com.taisau.robot.utils.InitConfig;
import com.taisau.robot.utils.MessageListener;
import com.taisau.robot.utils.NetWorkSpeedUtils;
import com.taisau.robot.utils.OfflineResource;
import com.taisau.robot.utils.UiMessageListener;
import com.taisau.robot.wakeup.IWakeupListener;
import com.taisau.robot.wakeup.MyWakeup;
import com.taisau.robot.wakeup.RecogWakeupListener;
import com.unity3d.player.UnityPlayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.taisau.robot.wakeup.IStatus.STATUS_NONE;

/**
 * 此service包含
 * 1，网易云信
 * 2，百度云语音识别
 * 3，百度云语音合成
 */
public class VoiceMsgService extends Service implements EventListener {

    //百度云相关
    private EventManager asr;
    private boolean enableOffline = false; // 测试离线命令词，需要改成true
    private String netSpeedStr;
    private boolean isCheckExpress = false;
    private ThirdPartyDataImpl thirdPartyData;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    LogUtils.e("百度云语音合成初始化成功   >>>");
                    break;
                case 2:
//                    LogUtils.e("百度云语音合成进度   >>>" + msg.arg1);
                    break;
                case 3:
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        LogUtils.e("百度云语音识别错误信息返回         >>>" + message + "\n");
                    }
                    break;
                case 4:
                    voiceInitOnBaidu();
                    LogUtils.e("handle   4");
                    break;
                case 5:
                    AutoCheck1 autoCheck1 = (AutoCheck1) msg.obj;
                    synchronized (autoCheck1) {
                        String message1 = autoCheck1.obtainDebugMessage();
                        LogUtils.e("百度云语音合成错误信息返回         >>>" + message1 + "\n");
                    }
                    break;
                case 6:
                    netSpeedStr = msg.obj.toString();
                    break;
                default:
                    break;
            }
        }
    };

    public VoiceMsgService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerObservers(true);//注册网易云信消息接收观察者

        delayVoiceInit();//延迟初始化百度云语音识别

        delayStartAgain();//延迟数小时后再开启百度云语音

        initialTts();//初始化百度云语音合成

        initWakeup();//初始化唤醒功能
    }

    /**
     * 注册网易云信消息接收观察者
     *
     * @param register 注册与否
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
    }

    //百度云语音识别
    private void delayVoiceInit() {
        CountDownTimer cdt = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                voiceInitOnBaidu();

//                start_wakeup();
//                LogUtils.e("开始说唤醒词");
            }
        };
        cdt.start();
    }

    //百度云语音识别
    private void delayStartAgain() {
        CountDownTimer cdt = new CountDownTimer(3600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                LogUtils.e("1小时后，再次开启百度云语音识别    >>>");
                voiceInitOnBaidu();
                delayStartAgain();
            }
        };
        cdt.start();
    }

    //百度云语音识别
    private void voiceInitOnBaidu() {
        asr = EventManagerFactory.create(this, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法

        //百度语音识别监听
        MainActivity.setOnStopListeningListener(new OnStopListeningListener() {
            @Override
            public void stopListening() {
                stop();
            }
        });
        MainActivity.setOnStartListeningListener(new OnStartListeningListener() {
            @Override
            public void startListening() {
                voiceInitOnBaidu();
            }
        });

        if (enableOffline) {
            loadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }
        LogUtils.e("百度云语音识别初始化成功   >>>");
        start();//开启语音识别
    }


    /**
     * 百度云语音识别参数填在这里
     */
    private void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        String event = null;
        event = SpeechConstant.ASR_START; // 替换成测试的event

        if (enableOffline) {
            params.put(SpeechConstant.DECODER, 2);//表示启用离线功能，但是SDK强制在线优先。
        } else {
//            params.put(SpeechConstant.DECODER,0);//表示禁用离线功能，只使用在线功能；
        }
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        // params.put(SpeechConstant.NLU, "enable");
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音   在VAD开启的情况下，可以自定义静音时长。即xx毫秒静音后，SDK认为用户一句话讲完。
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");//从默认麦克风的音频输入，可以改为用户自定义的音频文件或者自定义的音频流。适用于对于音频输入有定制化的情况。
        params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);//根据静音时长切分句子的算法  默认自动判断用户一句话是否讲完。也可以禁用。
        // params.put(SpeechConstant.PROP ,20000);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);

        // 复制此段可以自动检测错误
        (new AutoCheck(getApplicationContext(), handler, enableOffline)).checkAsr(params);

        String json = null; // 可以替换成自己的json
        json = new JSONObject(params).toString(); // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0);//设置参数并开启识别功能
    }

    //百度云语音识别
    private void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
    }

    //百度云语音识别的回调
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                }
            }
            //解析返回的语音字符
            JSONObject jsonObjectParams = null;
            try {
                jsonObjectParams = new JSONObject(params);
                String result_type_str = jsonObjectParams.optString("result_type");
                if (result_type_str.equals("final_result")) {
                    String best_result_str = jsonObjectParams.optString("best_result");
                    //测网速
                    new NetWorkSpeedUtils(this, handler).startShowNetSpeed();
                    LogUtils.e("百度云语音识别返回结果    >>>" + best_result_str + "；当前网速： " + netSpeedStr);

//                    if (!SPUtils.getInstance().getBoolean("recognition",true)) {
//                        LogUtils.e("到这里了，看清楚啊   >>>");
//                        if (best_result_str.contains("同学")){
//                            stop_speak();
//                            LogUtils.e("语音打断成功    >>>");
//                        }
//                    }else {
//                        LogUtils.e("上传语音喽   >>>");
//
//                    }
                    uploadVoice2Server(best_result_str);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }

        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)) {
            // 引擎就绪，可以说话，一般在收到此事件后通过UI通知用户可以说话了
            LogUtils.e("百度云语音识别   onEvent>>>" + "可以开始说话啦~~");
            startTime = TimeUtils.getNowMills();
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)) {
            // 识别结束
            LogUtils.e("百度云语音识别   onEvent>>>" + "一句话已说完。");
        }
    }

    //百度云语音识别离线加载引擎
    private void loadOfflineEngine() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(com.baidu.speech.asr.SpeechConstant.DECODER, 2);
        params.put(com.baidu.speech.asr.SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        params.putAll(fetchSlotDataParam());
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    //获取百度云语音识别离线参数
    private Map<String, Object> fetchSlotDataParam() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject json = new JSONObject();
            json.put("name", new JSONArray().put("妈妈").put("百度").put("冰箱"))
                    .put("greetings", new JSONArray().put("你好"));
            map.put(SpeechConstant.SLOT_DATA, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void unloadOfflineEngine() {
        asr.send(com.baidu.speech.asr.SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private boolean isStartAudioVideoCall = false;

    private long startTime;

    private void uploadVoice2Server(final String voiceResult) {
        //测试用的，用于在界面显示自己说的话。
        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Test", voiceResult);

        if (voiceResult == null || voiceResult.equals("") || voiceResult.length() == 1) {
            return;
        }

        //////////////////////////////////////////////////
        if (voiceResult.contains("你叫什么名字")) {
            speak("您好，我叫小梅，很高兴认识你。");
            return;
        }
        if (voiceResult.contains("说个笑话")) {
            thirdPartyData.requestJoke();
            return;
        }
        if (voiceResult.contains("今天天气")) {
            thirdPartyData.requestWeather();
            return;
        }
        if (voiceResult.contains("今天空气")) {
            thirdPartyData.requestAirQuality();
            return;
        }
        if (voiceResult.contains("新闻头条") || voiceResult.contains("新闻")) {
            thirdPartyData.requestTopNews();
            return;
        }
        if (voiceResult.contains("今天") && voiceResult.contains("上映") && voiceResult.contains("电影")) {
            thirdPartyData.requestMovies();
            return;
        }
        if (voiceResult.contains("灯谜")) {
            thirdPartyData.requestLanternRiddles();
            return;
        }
        if (voiceResult.contains("现在为你呼叫请稍后")) {
            return;
        }
//        if (voiceResult.contains("查快递")) {
//            isCheckExpress = true;
//            speak("请说出运单号");
//            return;
//        }
//        if (isCheckExpress) {
//            LogUtils.e("测试的数据返回   >>>" + voiceResult);
//            //检验运单号的格式
//            Pattern pattern = Pattern.compile("^-?[0-9]+");
//            if (pattern.matcher(voiceResult).matches()) {
//                //数字
//                thirdPartyData.requestExpress(voiceResult);
//            } else {
//                speak("运单号有误，请重新查询！");
//            }
//            isCheckExpress = false;
//            return;
//        }

        if (voiceResult.contains("背诵一首古诗")) {
            speak("白日依山尽，黄河入海流。欲穷千里目，更上一层楼。");
            return;
        }
        ////////////////////////////////////////////
        if (voiceResult.contains("拜拜") || voiceResult.contains("bye bye") || voiceResult.contains("再见") || voiceResult.contains("goodbye")) {
            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "6");//外宾
            return;
        }
        if (voiceResult.contains("你好") || voiceResult.contains("您好") || voiceResult.contains("good morning")) {
            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "1");//外宾
            return;
        }
        if (voiceResult.contains("测试")) {
//            startAudioVideoCall(AVChatType.AUDIO);
            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "7");//外宾
            return;
        }
        //上传语音字符到AIS
        RobotProtocol robotProtocol = new RobotProtocol();
        robotProtocol.setOrgCode(Constant.ORGCODE);//企业号
        robotProtocol.setEqCode(Constant.EQCODE);//设备号
        robotProtocol.setVoiceText(voiceResult);
        startTime = TimeUtils.getNowMills();
        OkHttpUtils.post()
                .url(Constant.UPLOAD_VOICE_URL)
                .addParams("chatMsg", new Gson().toJson(robotProtocol))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求数据异常：" + e);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        LogUtils.e("上传语音字符   返回的数据：" + s);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                            int code = jsonObject.optInt("code");
                            if (code == 0) {
                                JSONObject jsonObjectRobot = jsonObject.optJSONObject("objRts").optJSONObject("robot");
                                if (jsonObjectRobot != null) {
                                    int connectType = jsonObjectRobot.optInt("connectType");//1：找人；2：呼叫；3；闲聊；
                                    int staffSize = jsonObjectRobot.optInt("staffSize");//找到同名人的个数
                                    int replyCode = jsonObjectRobot.optInt("replyCode");//是 0 说明没有匹配到 reply时空；是 1 说明匹配到了 reply就是回答
                                    String reply = jsonObjectRobot.optString("reply");
                                    switch (connectType) {
                                        case 0:
//                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "3");//外宾
                                            thirdPartyData.requestJingDongRobot(voiceResult, startTime);
                                            LogUtils.e("闲聊        1");
                                            break;
                                        case 1://找人
                                            if (staffSize == 0) {
                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "4");//外宾
                                            } else if (staffSize == 1) {
                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "2");//外宾
                                                //显示在界面上
                                                JSONObject jsonObjectExtMap = jsonObjectRobot.optJSONObject("extMap");
                                                JSONObject jsonObjectPerson = jsonObjectExtMap.optJSONObject("person");
                                                if (jsonObjectPerson != null) {
                                                    String userName = jsonObjectPerson.optString("userName");
                                                    UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Content", userName);
                                                }
                                            } else {
//                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "3");//外宾  没听懂你在说什么
                                                thirdPartyData.requestJingDongRobot(voiceResult, startTime);
                                                LogUtils.e("闲聊        2");
                                            }
                                            LogUtils.e("找人耗时   >>>" + (TimeUtils.getNowMills() - startTime));
                                            break;
                                        case 2://呼叫
                                            if (staffSize == 1) {
                                                JSONObject jsonObjectExtMap = jsonObjectRobot.optJSONObject("extMap");
                                                JSONObject jsonObjectPerson = jsonObjectExtMap.optJSONObject("person");
                                                if (jsonObjectPerson != null) {
                                                    String userName = jsonObjectPerson.optString("userName");

                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            while (true) {
                                                                LogUtils.e("是否开启音视频对话      >>>" + AVChatProfile.getInstance().isAVChatting());
                                                                if (AVChatProfile.getInstance().isAVChatting()) {
                                                                    stop();
                                                                    isStartAudioVideoCall = true;
                                                                }
                                                                if (isStartAudioVideoCall) {
                                                                    if (!AVChatProfile.getInstance().isAVChatting()) {
                                                                        //取消线程
                                                                        isStartAudioVideoCall = false;
                                                                        handler.sendEmptyMessage(4);
                                                                        Thread.currentThread().interrupt();
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }).start();

                                                    startAudioVideoCall(AVChatType.AUDIO);//开启音频对话
//                                                    startAudioVideoCall(AVChatType.VIDEO);
                                                }
                                            } else if (staffSize == 0) {
                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "4");//外宾
                                            }
                                            break;
                                        case 3://闲聊
//                                            if (replyCode == 0) {
//                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "3");//外宾
//                                            } else if (replyCode == 1) {
//                                                LogUtils.e("闲聊的返回       >>>" + reply);
//                                                UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Content", reply);
//                                            }
                                            thirdPartyData.requestJingDongRobot(voiceResult, startTime);

                                            LogUtils.e("闲聊        3");
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } else {
                                String msg = jsonObject.optString("msg");
                                ToastUtils.showShort("" + msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //消息接收观察者
        registerObservers(false);

        //百度云语音识别
        asr.send(com.baidu.speech.asr.SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        //百度云语音合成
        if (synthesizer != null) {
            synthesizer.stop();
            synthesizer.release();
            synthesizer = null;
        }

        myWakeup.release();
    }

    //消息接收观察者
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            int counts = messages.size();
            for (int i = 0; i < counts; i++) {
                switch (messages.get(i).getMsgType()) {
                    case text:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "文本" + "；内容：" + messages.get(i).getContent());
                        break;
                    case tip:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "提醒消息" + "；内容：" + messages.get(i).getContent());
                        break;
                    case file:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "文件" + "；内容：" + messages.get(i).getContent());
                        break;
                    case audio:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "语音" + "；内容：" + messages.get(i).getContent());
                        break;
                    case image:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "图片" + "；内容：" + messages.get(i).getContent());
                        break;
                    case robot:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "机器人消息" + "；内容：" + messages.get(i).getContent());
                        break;
                    case undef:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "Unknown" + "；内容：" + messages.get(i).getContent());
                        break;
                    case video:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "视频" + "；内容：" + messages.get(i).getContent());
                        break;
                    case avchat:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "音视频通话" + "；内容：" + messages.get(i).getContent());
                        break;
                    case custom:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "自定义消息" + "；内容：" + messages.get(i).getContent());
                        break;
                    case location:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "位置" + "；内容：" + messages.get(i).getContent());
                        break;
                    case notification:
                        LogUtils.e("网易云接收的消息" + "，类型：" + "通知消息" + "；内容：" + messages.get(i).getContent());
                        break;
                    default:
                        break;
                }
//                if (messages.get(i).getMsgType() == MsgTypeEnum.tip && messages.get(i).getContent().equals("开门指令")) {
//                    Intent intent = new Intent("com.taisau.broadcast.OPENDOOR");
//                    getApplicationContext().sendBroadcast(intent);
//                    Log.e("incomingMessageObserver", "opendoor");
//                }
            }
        }
    };

    private void sendMsg2Robot() {
        String account = "ts";//000002

        SessionTypeEnum sessionType = SessionTypeEnum.P2P;
        String text = "";

        IMMessage textMessage = MessageBuilder.createTextMessage(account, sessionType, text);

        NIMClient.getService(MsgService.class).sendMessage(textMessage, false);
        LogUtils.e("网易云发送消息   >>>" + text);
    }

    //发音视频
    public void startAudioVideoCall(AVChatType avChatType) {
        LogUtils.e("正在呼叫...");
        AVChatKit.outgoingCall(getApplicationContext(), "111111", UserInfoHelper.getUserDisplayName("111111"), avChatType.getValue(), AVChatActivity.FROM_INTERNAL);
    }


    //////////////////////////////////百度云离在线语音合成相关////////////////////////////////////////

    // ================== 初始化参数设置开始 ==========================
    /**
     * 发布时请替换成自己申请的appId appKey 和 secretKey。注意如果需要离线合成功能,请在您申请的应用中填写包名。
     * 本demo的包名是com.baidu.tts.sample，定义在build.gradle中。
     */
    protected String appId = "11421619";

    protected String appKey = "EBu2azrNCdtZpiaENgCst1Xs";

    protected String secretKey = "xUE0w6WCR6cg0XoMV9yGlEQljO3PRSNm";

    // TtsMode.MIX; 离在线融合，在线优先； TtsMode.ONLINE 纯在线； 没有纯离线
    private TtsMode ttsMode = TtsMode.ONLINE;

    // 离线发音选择，VOICE_FEMALE即为离线女声发音。
    // assets目录下bd_etts_common_speech_m15_mand_eng_high_am-mix_v3.0.0_20170505.dat为离线男声模型；
    // assets目录下bd_etts_common_speech_f7_mand_eng_high_am-mix_v3.0.0_20170512.dat为离线女声模型
    protected String offlineVoice = OfflineResource.VOICE_FEMALE;

    // 主控制类，所有合成控制方法从这个类开始
    protected MySynthesizer synthesizer;

    // ===============初始化参数设置完毕，更多合成参数请至getParams()方法中设置 =================

    /**
     * 初始化引擎，需要的参数均在InitConfig类里
     * <p>
     * DEMO中提供了3个SpeechSynthesizerListener的实现
     * MessageListener 仅仅用log.i记录日志，在logcat中可以看见
     * UiMessageListener 在MessageListener的基础上，对handle发送消息，实现UI的文字更新
     * FileSaveListener 在UiMessageListener的基础上，使用 onSynthesizeDataArrived回调，获取音频流
     */
    protected void initialTts() {
        LoggerProxy.printable(true); // 日志打印在logcat中
        // 设置初始化参数
        // 此处可以改为 含有您业务逻辑的SpeechSynthesizerListener的实现类
        SpeechSynthesizerListener listener = new UiMessageListener(handler);

        //注册百度云语音合成监听
        MainActivity.setOnSpeakListener(new OnSpeakListener() {
            @Override
            public void startSpeaking(String speakStr) {
                speak(speakStr);
            }
        });
        ThirdPartyDataImpl.setOnSpeakListener(new OnSpeakListener() {
            @Override
            public void startSpeaking(String speakStr) {
                speak(speakStr);
            }
        });

        Map<String, String> params = getParams();


        // appId appKey secretKey 网站上您申请的应用获取。注意使用离线合成功能的话，需要应用中填写您app的包名。包名在build.gradle中获取。
        InitConfig initConfig = new InitConfig(appId, appKey, secretKey, ttsMode, params, listener);

        // 如果您集成中出错，请将下面一段代码放在和demo中相同的位置，并复制InitConfig 和 AutoCheck到您的项目中
        // 上线时请删除AutoCheck的调用
        AutoCheck1.getInstance(getApplicationContext()).check(initConfig, handler);
        synthesizer = new NonBlockSynthesizer(this, initConfig, handler); // 此处可以改为MySynthesizer 了解调用过程

        thirdPartyData = new ThirdPartyDataImpl();
    }

    /**
     * 百度云语音合成的参数，可以初始化时填写，也可以在合成前设置。
     *
     * @return
     */
    protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        // 以下参数均为选填
        // 设置在线发声音人： 0 普通女声（默认） 1 普通男声 2 特别男声 3 情感男声<度逍遥> 4 情感儿童声<度丫丫>
        params.put(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // 设置合成的音量，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_VOLUME, "9");
        // 设置合成的语速，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_SPEED, "5");
        // 设置合成的语调，0-9 ，默认 5
        params.put(SpeechSynthesizer.PARAM_PITCH, "5");

        params.put(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
        // 该参数设置为TtsMode.MIX生效。即纯在线模式不生效。
        // MIX_MODE_DEFAULT 默认 ，wifi状态下使用在线，非wifi离线。在线状态下，请求超时6s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE_WIFI wifi状态下使用在线，非wifi离线。在线状态下， 请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_NETWORK ， 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线
        // MIX_MODE_HIGH_SPEED_SYNTHESIZE, 2G 3G 4G wifi状态下使用在线，其它状态离线。在线状态下，请求超时1.2s自动转离线

        // 离线资源文件， 从assets目录中复制到临时目录，需要在initTTs方法前完成
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        // 声学模型文件路径 (离线引擎使用), 请确认下面两个文件存在
        if (offlineResource != null) {
            params.put(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, offlineResource.getTextFilename());
            params.put(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, offlineResource.getModelFilename());
        }
        return params;
    }

    //百度云语音合成
    protected OfflineResource createOfflineResource(String voiceType) {
        OfflineResource offlineResource = null;
        try {
            offlineResource = new OfflineResource(this, voiceType);
        } catch (IOException e) {
            // IO 错误自行处理
            e.printStackTrace();
            print("【error】:copy files from assets failed>>>" + e.getMessage());
        }
        return offlineResource;
    }


    /**
     * speak 实际上是调用 synthesize后，获取音频流，然后播放。
     * 获取音频流的方式见SaveFileActivity及FileSaveListener
     * 需要合成的文本text的长度不能超过1024个GBK字节。
     */
    private void speak(String speakStr) {
        // 需要合成的文本speakStr的长度不能超过1024个GBK字节。
        if (TextUtils.isEmpty(speakStr)) {
            speakStr = "不好意思，我没听清楚你说什么，请再说一遍好吗？";
        }
        // 合成前可以修改参数：
        // Map<String, String> params = getParams();
        // synthesizer.setParams(params);
        int result = synthesizer.speak(speakStr);
        checkResult(result, "speak");
    }

    /*
     * 百度云语音合成，停止合成引擎。即停止播放，合成，清空内部合成队列。
     */
    private void stop_speak() {
        int result = synthesizer.stop();
        checkResult(result, "stop");
    }

    /**
     * 百度云语音合成切换离线发音。注意需要添加额外的判断：引擎在合成时该方法不能调用
     */
    private void loadModel(String mode) {
        offlineVoice = mode;
        OfflineResource offlineResource = createOfflineResource(offlineVoice);
        print("切换离线语音：" + offlineResource.getModelFilename());
        int result = synthesizer.loadModel(offlineResource.getModelFilename(), offlineResource.getTextFilename());
        checkResult(result, "loadModel");
    }

    //百度云语音合成
    private void print(String message) {
        Log.e("TAG", message);
    }

    /**
     * 百度云语音合成检查错误
     *
     * @param result
     * @param method
     */
    private void checkResult(int result, String method) {
        if (result != 0) {
            print("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }


    private static final String TAG = "ActivityWakeUp";
    protected MyWakeup myWakeup;

    private int status = STATUS_NONE;

    private void initWakeup() {
        IWakeupListener listener = new RecogWakeupListener(handler);
        // 改为 SimpleWakeupListener 后，不依赖handler，但将不会在UI界面上显示
        myWakeup = new MyWakeup(this, listener);

        MessageListener.setOnWakeupStartListener(new OnWakeupStartListener() {
            @Override
            public void wakeupStart() {
                start_wakeup();
            }
        });
        MessageListener.setOnWakeupStopListener(new OnWakeupStopListener() {
            @Override
            public void wakeupStop() {
                stop_wakeup();
            }
        });
    }

    protected void start_wakeup() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(SpeechConstant.WP_WORDS_FILE, "assets:///WakeUp.bin");
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下

        // params.put(SpeechConstant.ACCEPT_AUDIO_DATA,true);
        // params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME,true);
        // params.put(SpeechConstant.IN_FILE,"res:///com/baidu/android/voicedemo/wakeup.pcm");
        // params里 "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        myWakeup.start(params);
    }

    protected void stop_wakeup() {
        myWakeup.stop();
    }
}
