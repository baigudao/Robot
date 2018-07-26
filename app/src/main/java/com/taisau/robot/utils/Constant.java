package com.taisau.robot.utils;

/**
 * Created by Devin on 2018/6/7.
 */
public class Constant {

//    private static final String BASE_URL = "http://192.168.2.7:8080/mcsWeb/";

//    private static final String BASE_URL = "http://192.168.2.152:8080/";//金刚

//    private static final String BASE_URL = "http://jvm.natapp1.cc/mcsWeb/";//李强

    private static final String BASE_URL = "http://192.168.2.158:81/mcsWeb/";//李强1.0

    public static final String LOGIN_URL = BASE_URL + "service/v1/login";

    public static final String UPLOAD_FACE_URL = BASE_URL + "service/v1/signFace";

    public static final String UPLOAD_VOICE_URL = BASE_URL + "service/v1/analyzeChat";

    //设备号
    public static final String EQCODE = "TS_1003";//TS_0719_1
    //机构号
    public static final String ORGCODE = "0001";//"0001";

    //聚合数据相关
    private static final String JUHE_DATA_BASE_URL = "http://v.juhe.cn";
    //天气
    public static final String JUHE_DATA_WEATHER_URL = JUHE_DATA_BASE_URL + "/weather/index";
    //笑话
    public static final String JUHE_DATA_JOKE_URL = JUHE_DATA_BASE_URL + "/joke/content/list.php";
    //空气质量
    public static final String JUHE_DATA_AIR_QUALITY_URL = "http://web.juhe.cn:8080/environment/air/pm";
    //新闻头条
    public static final String JUHE_DATA_TOP_NEWS_URL = JUHE_DATA_BASE_URL + "/toutiao/index";
    //今日影讯
    public static final String JUHE_DATA_MOVIES_URL = JUHE_DATA_BASE_URL + "/movie/movies.today";
    //猜灯谜
    public static final String DENG_MI_URL = "http://api.shujuzhihui.cn/api/riddle/rand";
    //快递公司编号列表
    public static final String JUHE_DATA_EXPRESS_COMPANY_NO_URL = "http://v.juhe.cn/exp/com";
    //快递信息
    public static final String JUHE_DATA_EXPRESS_INFO_URL = "http://v.juhe.cn/exp/index";
}
