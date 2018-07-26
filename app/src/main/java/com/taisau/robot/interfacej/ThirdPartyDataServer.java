package com.taisau.robot.interfacej;

/**
 * Created by Devin on 2018/7/6.
 */
public interface ThirdPartyDataServer {

    //笑话
    void requestJoke();

    //天气情况
    void requestWeather();

    //今天空气
    void requestAirQuality();

    //新闻头条
    void requestTopNews();

    //今日影讯
    void requestMovies();

    //猜灯谜
    void requestLanternRiddles();

    //查快递
    void requestExpress(String string);

    //京东云的图灵机器人的使用
    void requestJingDongRobot(String string,long time);
}
