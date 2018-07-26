package com.taisau.robot.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.GFace;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.AVChatProfile;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.constant.AVChatExtras;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.mixpush.MixPushService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.taisau.robot.R;
import com.taisau.robot.config.preference.Preferences;
import com.taisau.robot.config.preference.UserPreferences;
import com.taisau.robot.interfacej.OnSpeakListener;
import com.taisau.robot.interfacej.OnStartListeningListener;
import com.taisau.robot.interfacej.OnStopListeningListener;
import com.taisau.robot.login.LoginActivity;
import com.taisau.robot.login.LogoutHelper;
import com.taisau.robot.main.VoiceMsgService;
import com.taisau.robot.main.fragment.HomeFragment;
import com.taisau.robot.session.SessionHelper;
import com.taisau.robot.team.TeamCreateHelper;
import com.taisau.robot.utils.Constant;
import com.taisau.robot.utils.FeaAction;
import com.taisau.robot.utils.FileUtils;
import com.taisau.robot.utils.ImgUtils;
import com.unity3d.player.UnityPlayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * 主界面
 * <p/>
 * Created by huangjun on 2015/3/25.
 */
public class MainActivity extends UI {

    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private HomeFragment mainFragment;

    protected UnityPlayer mUnityPlayer;
    private Bitmap faceBitmap;

    private CountDownTimer cdt;
    private Intent intentOne;

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_tab);

        requestBasicPermission();//运行时权限管理

        onParseIntent();

        // 等待同步数据完成
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
            @Override
            public void onEvent(Void v) {

                syncPushNoDisturb(UserPreferences.getStatusConfig());

                DialogMaker.dismissProgressDialog();
            }
        });

        LogUtil.i(TAG, "sync completed = " + syncCompleted);
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
        } else {
            syncPushNoDisturb(UserPreferences.getStatusConfig());
        }


        mUnityPlayer = new UnityPlayer(this);
        onInit();//加載主界面
        setContentView(mUnityPlayer);

        //设置Unity多久拍摄一张图片
        UnityPlayer.UnitySendMessage("Main Camera", "Pe_Excent", "1.0");

        startRecvMsgService();

        registerObservers(true);//網易云交互

        //开启一个5秒的倒计时器
        cdt = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                isUploadBitmap = true;
            }
        };

        initPermission();

        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
//                if (onSpeakListener != null) {
//                    onSpeakListener.startSpeaking("泰首智能机器人上线了，很高兴为你服务。");
//                }
            }
        }.start();
    }

    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(incomingMessageObserver, register);
    }

    /**
     * 网易云消息接收观察者
     */
    Observer<List<IMMessage>> incomingMessageObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> messages) {
            if (messages == null || messages.isEmpty()) {
                return;
            }

            String strMsg = "";
            int counts = messages.size();
        }
    };

    private void startRecvMsgService() {
        intentOne = new Intent(this, VoiceMsgService.class);
        startService(intentOne);
    }

    /**
     * 若增加第三方推送免打扰（V3.2.0新增功能），则：
     * 1.添加下面逻辑使得 push 免打扰与先前的设置同步。
     * 2.设置界面{@link com.taisau.robot.main.activity.SettingsActivity} 以及
     * 免打扰设置界面{@link com.taisau.robot.main.activity.NoDisturbActivity} 也应添加 push 免打扰的逻辑
     * <p>
     * 注意：isPushDndValid 返回 false， 表示未设置过push 免打扰。
     */
    private void syncPushNoDisturb(StatusBarNotificationConfig staConfig) {

        boolean isNoDisbConfigExist = NIMClient.getService(MixPushService.class).isPushNoDisturbConfigExist();

        if (!isNoDisbConfigExist && staConfig.downTimeToggle) {
            NIMClient.getService(MixPushService.class).setPushNoDisturbConfig(staConfig.downTimeToggle,
                    staConfig.downTimeBegin, staConfig.downTimeEnd);
        }
    }

    private void onInit() {
        // 加载主页面
        //showMainFragment();

        LogUtil.ui("NIM SDK cache path=" + NIMClient.getSdkStorageDirPath());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    @Override
    public void onBackPressed() {
        if (mainFragment != null) {
            if (mainFragment.onBackPressed()) {
                return;
            } else {
                moveTaskToBack(true);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    private void onParseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        } else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting()) {
                Intent localIntent = new Intent();
                localIntent.setClass(this, AVChatActivity.class);
                startActivity(localIntent);
            }
        } else if (intent.hasExtra(AVChatExtras.EXTRA_FROM_NOTIFICATION)) {
            String account = intent.getStringExtra(AVChatExtras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    private void showMainFragment() {
        if (mainFragment == null && !isDestroyedCompatible()) {
            mainFragment = new HomeFragment();
            switchFragmentContent(mainFragment);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MainActivity.this, selected, false, null);
                } else {
                    Toast.makeText(MainActivity.this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_ADVANCED) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                TeamCreateHelper.createAdvancedTeam(MainActivity.this, selected);
            }
        }
    }

    // 注销
    private void onLogout() {
        Preferences.saveUserToken("");
        // 清理缓存&注销监听
        LogoutHelper.logout();

        // 启动登录
        LoginActivity.start(this);
        finish();
    }

    //百度云语音识别的停止
    private static OnStopListeningListener onStopListeningListener;

    public static void setOnStopListeningListener(OnStopListeningListener onStopListeningListener) {
        MainActivity.onStopListeningListener = onStopListeningListener;
    }

    //百度云语音识别的开始
    private static OnStartListeningListener onStartListeningListener;

    public static void setOnStartListeningListener(OnStartListeningListener onStartListeningListener) {
        MainActivity.onStartListeningListener = onStartListeningListener;
    }

    public void Kdt_tFace() {//开
        if (onStartListeningListener != null) {
            onStartListeningListener.startListening();
//            LogUtils.e("梅调语音识别     开   >>>");
        }
//        SPUtils.getInstance().put("recognition", true);
//        LogUtils.e("调用了    true   >>>");
    }

    public void Kdf_tFace() {//关
        if (onStopListeningListener != null) {
            onStopListeningListener.stopListening();
//            LogUtils.e("梅调语音识别     关   >>>");
        }

//        SPUtils.getInstance().put("recognition", false);
//        LogUtils.e("调用了    false   >>>");
    }

    //百度云语音合成监听
    private static OnSpeakListener onSpeakListener;

    public static void setOnSpeakListener(OnSpeakListener onSpeakListener) {
        MainActivity.onSpeakListener = onSpeakListener;
    }

    public void Log_Test(String logTest) {
        if (logTest != null) {
            LogUtils.w("Unity>>>   " + logTest);
        }
    }

    //提供Unity语音合成的接口
    public void Audio_Play(String speakStr) {
        if (speakStr != null && !TextUtils.isEmpty(speakStr) && onSpeakListener != null) {
            SPUtils.getInstance().put("meijunrui", false);
            onSpeakListener.startSpeaking(speakStr);
        }
    }

    private byte[] last_bytes;

    //Unity传byte[]数组过来
    public void detect_Face(byte[] bitmapInt, int length) {
//        last_bytes = new byte[length];
//        if (length > 0) {
//            for (int i = 0; i < length; i++) {
//                last_bytes[i] = (byte) (bitmapInt[i] - 128);
//            }
//            GFace.FaceInfo aa = GFace.getFaceInfo(last_bytes);
//            Bitmap adjustBitmap = ImgUtils.getUtils().adjustBitmap(faceBitmap, aa, 1);
//        }
    }


    private boolean isUploadBitmap = true;//默认上传图片到后台
    private String sexString = "";
    private boolean isComingMember_V = false;//访客
    private boolean isComingMember_S = false;//员工
    private boolean isComingMember_B = false;//黑名单
    private int noFaceCount = 0;//无人脸出现的次数

    private FeaAction feaAction = new FeaAction();

    private int detect_face_count = 0;

    private byte[] bitmap2Gray(Bitmap faceBitmap) {
        int[] pixels = new int[faceBitmap.getHeight() * faceBitmap.getWidth()];
        faceBitmap.getPixels(pixels, 0, faceBitmap.getWidth(), 0, 0, faceBitmap.getWidth(), faceBitmap.getHeight());
        byte[] rgb = new byte[faceBitmap.getHeight() * faceBitmap.getWidth()];
        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = (byte) RgbToGray(pixels[i]);
        }
        return rgb;
    }

    //Unity调用
    public void detectFace(String picName) {
        long currentTimeMillis = System.currentTimeMillis();
        LogUtils.i("Unity多久调一次detectFace()   >>>" + (currentTimeMillis - SPUtils.getInstance().getLong("detect_face")));
        SPUtils.getInstance().put("detect_face", currentTimeMillis);
        // Log.e("___WW___", "detectFace picName= "+picName);

        String filePath = "/mnt/sdcard/Android/data/com.taisau.robot/files";
        String fileName = filePath + "/" + picName;
//        Log.e("___WW___", "filePath= " + fileName);
        faceBitmap = BitmapFactory.decodeFile(fileName);

        if (faceBitmap == null) {
            return;
        }
        byte[] rgb = bitmap2Gray(faceBitmap);
        byte[] ret = GFace.detectFace(rgb, faceBitmap.getWidth(), faceBitmap.getHeight());


        if (ret != null && ret[0] > 0) {
            Log.e("___WW___", "has Face");
            server4MCS(ret);
        } else {
            Log.e("___WW___", "no Face");
            server4MCSElse();
        }

        if (faceBitmap != null && !faceBitmap.isRecycled()) {
            faceBitmap.recycle();
            faceBitmap = null;
        }

        FileUtils.deleteFile(fileName);  //照片使用完后删除
    }

    private float[] bitmap2Fea(Bitmap bitmap) {
        byte[] gray = bitmap2Gray(bitmap);
        byte[] detectFace = GFace.detectFace(gray, bitmap.getWidth(), bitmap.getHeight());
        GFace.FaceInfo faceInfo = GFace.getFaceInfo(detectFace);
        return this.feaAction.doFeaAction(FeaAction.FEA_CASE.DO_FACE_COMPARE, detectFace, faceInfo);
    }

    private void server4MCS(byte[] ret) {
        GFace.FaceInfo aa = GFace.getFaceInfo(ret);
        //抠图
        final Bitmap adjustBitmap = ImgUtils.getUtils().adjustBitmap(faceBitmap, aa, 1);

//        Bitmap bitmap = null;
//        try {
//            bitmap = ImageUtils.getBitmap("/mnt/sdcard/Android/data/com.taisau.robot/files/adjustBitmap.jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (bitmap != null) {
//            float feaCompare = GFace.feaCompare(bitmap2Fea(adjustBitmap), bitmap2Fea(bitmap));
//            LogUtils.e("比对的分值为   >>>" + feaCompare);
//        }


        //得到adjustBitmap的人脸特征值
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] detectFace = GFace.detectFace(bitmap2Gray(adjustBitmap), adjustBitmap.getWidth(), adjustBitmap.getHeight());//人脸结构体
//                GFace.FacePointInfo point = GFace.getFaceInfo(detectFace).info[0];
//                float[] floats = GFace.getFea(detectFace, adjustBitmap.getWidth(), adjustBitmap.getHeight(), (int) point.ptEyeLeft.x, (int) point.ptEyeLeft.y, (int) point.ptEyeRight.x, (int) point.ptEyeRight.y,
//                        (int) point.ptNose.x, (int) point.ptNose.y, (int) point.ptMouthLeft.x, (int) point.ptMouthLeft.y,
//                        (int) point.ptMouthRight.x, (int) point.ptMouthRight.y);
//
//                for (int i = 0; i < floats.length; i++) {
//                    LogUtils.e("人脸特征值   >>>" + floats[i]);
//                }
//            }
//        }).start();


        boolean save = ImageUtils.save(adjustBitmap, "/mnt/sdcard/Android/data/com.taisau.robot/files/adjustBitmap.jpg", Bitmap.CompressFormat.JPEG);


        //有人脸，与人脸库比对  3种情况：访客；黑名单；职员
        final String picString = ImgUtils.bitmapToBase64(adjustBitmap);
        if (picString == null || picString.equals("")) {
            LogUtils.e("faceBitmap的Base64位为空   >>>");
            return;
        }
        LogUtils.e("是否上传Bitmap位图   >>>" + isUploadBitmap);
        //有人脸时，上传人脸照给后台比对，后台返回比对结果
        if (isUploadBitmap) {
            isUploadBitmap = false;
            LogUtils.e("开始上传人脸数据  >>>");
            OkHttpUtils.post()
                    .url(Constant.UPLOAD_FACE_URL)
                    .addParams("picStr", picString)
                    .addParams("orgCode", Constant.ORGCODE)
                    .addParams("eqCode", Constant.EQCODE)
                    .addParams("time", TimeUtils.getNowString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            LogUtils.e("上传人脸请求，数据异常：" + e);
                            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Test", "上传人脸请求，数据异常：" + e);
                            if (onSpeakListener != null) {
                                onSpeakListener.startSpeaking("上传人脸请求，数据异常");
                            }
                            //异常情况下，5秒发一次上传
                            cdt.start();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            LogUtils.e("上传人脸请求      返回数据：" + s);
                            cdt.start();
                             JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(s);
                                int code = jsonObject.optInt("code");
                                if (code == 0) {
                                    JSONObject jsonObjectRts = jsonObject.optJSONObject("objRts");
                                    if (jsonObjectRts != null) {
                                        String score = jsonObjectRts.optString("score");
                                        if (score == null || score.equals("")) {
                                            cdt.start();
                                            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Test", "上传人脸接口，服务器返回数据错误  === 没有分数值！");
//                                                LogUtils.e("score分数为空   >>>");//{"code":0,"datas":[],"msg":null,"objRts":{"memberType":2,"id":"1"}}
                                            return;
                                        }

                                        if (Float.valueOf(score) >= 80.0) {
                                            int memberType = jsonObjectRts.optInt("memberType");//1：员工；2：黑名单；3：访客
//                                            LogUtils.e("memberType的值为    >>>" + memberType);
                                            int sex = jsonObjectRts.optInt("sex");//0：男；1：女；2：未知
                                            String id = jsonObjectRts.optString("id");
                                            String userName = jsonObjectRts.optString("userName");
                                            int attendance = jsonObjectRts.optInt("attendance");//考勤字段 attendance  ； 值 0 考勤成功，1 考勤迟到 ， 2 正常下班， 3 早退，  4(员工未同步) 欢迎观临
//                                                if (id.equals(member_id)) {
//                                                    //不往下面走
//                                                    return;
//                                                }
                                            switch (memberType) {
                                                case 1://员工
                                                    //开启倒计时
                                                    cdt.start();
                                                    if (isComingMember_S) {
                                                        return;
                                                    }
                                                    isComingMember_S = true;//员工来了
                                                    isComingMember_B = false;
                                                    isComingMember_V = false;
                                                    noFaceCount = 0;
                                                    //加语音合成
                                                    if (onSpeakListener != null) {
                                                        switch (sex) {
                                                            case 0:
                                                                sexString = userName + "，先生。";
                                                                break;
                                                            case 1:
                                                                sexString = userName + "，女士。";
                                                                break;
                                                            default:
                                                                sexString = userName;
                                                                break;
                                                        }
//                                                            onStopListeningListener.stopListening();
                                                        onSpeakListener.startSpeaking(sexString);
                                                    }
                                                    //在框中显示考勤时间
                                                    UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Content", TimeUtils.getNowString());
                                                    try {
                                                        Thread.sleep(1500);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
//                                                        member_id = id;
                                                    if (attendance == 0) {
                                                        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_s", "1");
                                                        LogUtils.e("attendance  0");
                                                    } else if (attendance == 1) {
                                                        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_s", "3");
                                                        LogUtils.e("attendance  1");
                                                    } else if (attendance == 2) {
                                                        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_s", "4");
                                                        LogUtils.e("attendance  2");
                                                    } else if (attendance == 3) {
                                                        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_s", "4");
                                                        LogUtils.e("attendance  3");
                                                    } else if (attendance == 4) {
                                                        UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_OnlyContent", "欢迎光临！");
                                                        if (onSpeakListener != null) {
                                                            onSpeakListener.startSpeaking("欢迎光临");
                                                        }
                                                    }
                                                    LogUtils.e("员工       1");
                                                    break;
                                                case 2://黑名单
                                                    cdt.start();
                                                    if (isComingMember_B) {
                                                        return;
                                                    }
                                                    isComingMember_B = true;//黑名单来了
                                                    isComingMember_S = false;
                                                    isComingMember_V = false;
                                                    noFaceCount = 0;
                                                    UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_b", "2");
                                                    UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Content", "严重警告！");
                                                    LogUtils.e("黑名单          2");
//                                                        isUploadBitmap=true;
                                                    break;
                                                default:
                                                    cdt.start();
                                                    break;
                                            }
                                        } else {
                                            //全部归为访客
                                            cdt.start();
                                            if (isComingMember_V) {
                                                return;
                                            }
                                            isComingMember_V = true;//访客来了
                                            isComingMember_S = false;
                                            isComingMember_B = false;
                                            noFaceCount = 0;
                                            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage", "1");//外宾
                                            LogUtils.e("访客       1");
//                                                isUploadBitmap=true;
                                        }
                                    }
                                } else {
                                    String msg = jsonObject.optString("msg");
                                    ToastUtils.showShort("" + msg);
                                    cdt.start();
                                    UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Test", msg);
                                    if (onSpeakListener != null) {
                                        onSpeakListener.startSpeaking(msg);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                cdt.start();
                            }
                        }
                    });
        }
    }

    private void server4MCSElse() {
        //长久的没有人脸就清除缓存的id值
        //访客来了
        if (isComingMember_V) {
            //当前有人
            noFaceCount++;
            if (noFaceCount >= 15) {//至多等待10秒
                //说明当前无人，已经离开了
                noFaceCount = 0;
                isComingMember_V = false;
                isComingMember_S = false;
                isComingMember_B = false;
            }
        }
        //员工来了
        if (isComingMember_S) {
            //当前有人
            noFaceCount++;
            if (noFaceCount >= 15) {//至多等待10秒
                //说明当前无人，已经离开了
                noFaceCount = 0;
                isComingMember_V = false;
                isComingMember_S = false;
                isComingMember_B = false;
            }
        }
        //黑名单来了
        if (isComingMember_B) {
            //当前有人
            noFaceCount++;
            if (noFaceCount >= 15) {//至多等待10秒
                //说明当前无人，已经离开了
                noFaceCount = 0;
                isComingMember_V = false;
                isComingMember_S = false;
                isComingMember_B = false;
            }
        }
    }

    private void server4GFS(byte[] ret) {
        GFace.FaceInfo aa = GFace.getFaceInfo(ret);
        //抠图
        Bitmap adjustBitmap = ImgUtils.getUtils().adjustBitmap(faceBitmap, aa, 1);

        //有人脸，与人脸库比对  3种情况：访客；黑名单；职员
        final String picString = ImgUtils.bitmapToBase64(adjustBitmap);

        if (picString == null || picString.equals("")) {
            LogUtils.e("faceBitmap的Base64位为空   >>>");
            return;
        }
        LogUtils.e("是否上传Bitmap位图   >>>" + isUploadBitmap);
        //有人脸时，上传人脸照给后台比对，后台返回比对结果
        if (isUploadBitmap) {
            isUploadBitmap = false;
            LogUtils.e("开始上传人脸数据  >>>");
            OkHttpUtils.post()
                    .url("http://192.168.2.7:8080/gface/face/search_face")
                    .addParams("group_id_list", "jqr_007")
                    .addParams("img", picString)
//                    .addParams("count", "1")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            LogUtils.e("上传人脸请求，数据异常：" + e);
                            UnityPlayer.UnitySendMessage("Main Camera", "Getmessage_Test", "上传人脸请求，数据异常：" + e);
                            if (onSpeakListener != null) {
                                onSpeakListener.startSpeaking("上传人脸请求，数据异常");
                            }
                            //异常情况下，5秒发一次上传
                            cdt.start();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            LogUtils.e("上传人脸请求      返回数据：" + s);
                            cdt.start();
                        }
                    });
        }
    }

    private GFace.FaceInfo adjustFaceInfo(GFace.FaceInfo aa, boolean isAdd) {
        if (isAdd) {
            aa.rc[0].left = aa.rc[0].left + 170;//越大，框越往左
            aa.rc[0].right = aa.rc[0].right + 170;
            aa.rc[0].top = aa.rc[0].top + 80;//越大，框越往下
            aa.rc[0].bottom = aa.rc[0].bottom + 80;
        } else {
            aa.rc[0].left = aa.rc[0].left - 170;
            aa.rc[0].right = aa.rc[0].right - 170;
            aa.rc[0].top = aa.rc[0].top - 80;
            aa.rc[0].bottom = aa.rc[0].bottom - 80;
        }
        return aa;
    }

    //发音视频  TODO
    public void startAudioVideoCall(AVChatType avChatType) {
        AVChatKit.outgoingCall(getApplicationContext(), "yx_m333111", UserInfoHelper.getUserDisplayName("yx_m333111"), avChatType.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }


////////////////////////////////////////////////////////////////////////////////////////////////////

    // Quit Unity
    @Override
    protected void onDestroy() {
        mUnityPlayer.quit();
        super.onDestroy();
        stopService(intentOne);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        mUnityPlayer.quit();
//        stopService(intentOne);
    }

    // Pause Unity
    @Override
    protected void onPause() {
        super.onPause();
        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override
    protected void onResume() {
        super.onResume();
        mUnityPlayer.resume();
    }


    // Low Memory Unity
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
        Log.e("___WW___", "UnityPlayer.lowMemory()");
    }

    // Trim Memory Unity
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            mUnityPlayer.lowMemory();
        }

        Log.e("___WW___", "onTrimMemory level = " + level);
    }

    // This ensures the layout will be correct.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    public int RgbToGray(int r, int g, int b) {
        return (r * 30 + g * 59 + b * 11) / 100;
    }

    public int RgbToGray(int xrgb) {
        return RgbToGray((xrgb >> 16) & 0xff,
                (xrgb >> 8) & 0xff,
                (xrgb) & 0xff);
    }


    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private void requestBasicPermission() {
        MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
        MPermission.with(MainActivity.this)
                .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(BASIC_PERMISSIONS)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        try {
            //Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        try {
            Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }
}