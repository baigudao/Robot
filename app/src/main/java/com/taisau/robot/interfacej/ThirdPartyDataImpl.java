package com.taisau.robot.interfacej;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.taisau.robot.bean.AirQualityBean;
import com.taisau.robot.bean.ExpressCompanyBean;
import com.taisau.robot.bean.ExpressInfoBean;
import com.taisau.robot.bean.JingDongBean;
import com.taisau.robot.bean.JokeBean;
import com.taisau.robot.bean.LanternRiddlesBean;
import com.taisau.robot.bean.MoviesBean;
import com.taisau.robot.bean.TopNewsBean;
import com.taisau.robot.bean.WeatherBean;
import com.taisau.robot.utils.Constant;
import com.unity3d.player.UnityPlayer;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Devin on 2018/7/6.
 */
public class ThirdPartyDataImpl implements ThirdPartyDataServer {

    //百度云语音合成监听
    private static OnSpeakListener onSpeakListener;

    public static void setOnSpeakListener(OnSpeakListener onSpeakListener) {
        ThirdPartyDataImpl.onSpeakListener = onSpeakListener;
    }

    @Override
    public void requestJoke() {
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_JOKE_URL)
                .addParams("sort", "desc")
                .addParams("page", "1")
                .addParams("pagesize", "1")
                .addParams("time", String.valueOf(getSecondTimestamp(new Date())))
                .addParams("key", "1b783c6fbcf6780208c0609d689bdea3")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        JokeBean jokeBean = gson.fromJson(s, JokeBean.class);
                        if (jokeBean != null) {
                            JokeBean.ResultBean resultBean = jokeBean.getResult();
                            if (resultBean != null) {
                                List<JokeBean.ResultBean.DataBean> resultBeanData = resultBean.getData();
                                if (resultBeanData != null) {
                                    JokeBean.ResultBean.DataBean dataBean = resultBeanData.get(0);
                                    if (dataBean != null && onSpeakListener != null) {
                                        onSpeakListener.startSpeaking(dataBean.getContent());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    private long getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    @Override
    public void requestWeather() {
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_WEATHER_URL)
                .addParams("cityname", "深圳")
                .addParams("dtype", "json")
                .addParams("format", "1")
                .addParams("key", "4280f2dd45dd3d98fb4e2f834ac20b0d")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("网络异常:" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        WeatherBean weatherBean = gson.fromJson(s, WeatherBean.class);
                        if (weatherBean != null) {
                            WeatherBean.ResultBean resultBean = weatherBean.getResult();
                            if (resultBean != null) {
                                WeatherBean.ResultBean.TodayBean todayBean = resultBean.getToday();
                                if (todayBean != null && onSpeakListener != null) {
                                    onSpeakListener.startSpeaking("今天天气" + todayBean.getWeather());
                                    UnityPlayer.UnitySendMessage("Main Camera", "Baidu_speechsynthesis_weather", todayBean.getWeather());
                                }
                            }
                        }

                    }
                });
    }

    @Override
    public void requestAirQuality() {
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_AIR_QUALITY_URL)
                .addParams("city", "深圳")
                .addParams("key", "bcff52a6d8e3754bd0bcd3954dfae592")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        LogUtils.e("空气质量返回的数据   >>>" + s);
                        Gson gson = new Gson();
                        AirQualityBean airQualityBean = gson.fromJson(s, AirQualityBean.class);
                        if (airQualityBean != null) {
                            List<AirQualityBean.ResultBean> result = airQualityBean.getResult();
                            if (result != null) {
                                AirQualityBean.ResultBean resultBean = result.get(0);
                                if (resultBean != null && onSpeakListener != null) {
                                    onSpeakListener.startSpeaking("今天空气质量" + resultBean.getQuality()
                                            + "。其中，PM2点5的值为：" + resultBean.get_$PM25115()
                                            + "，一氧化碳：" + resultBean.getCO()
                                            + "，二氧化硫：" + resultBean.getSO2());
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void requestTopNews() {
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_TOP_NEWS_URL)
                .addParams("key", "6dee06428f3a8f00a62403c90af06e21")
                .addParams("type", "top")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        TopNewsBean topNewsBean = gson.fromJson(s, TopNewsBean.class);
                        if (topNewsBean != null) {
                            TopNewsBean.ResultBean result = topNewsBean.getResult();
                            if (result != null) {
                                List<TopNewsBean.ResultBean.DataBean> data = result.getData();
                                if (data != null) {
                                    TopNewsBean.ResultBean.DataBean dataBean = data.get(0);
                                    if (dataBean != null && onSpeakListener != null) {
                                        onSpeakListener.startSpeaking("来自" + dataBean.getAuthor_name() + "的最新消息：" + dataBean.getTitle());
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void requestMovies() {
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_MOVIES_URL)
                .addParams("cityid", "3")
                .addParams("key", "49f1c320044842c019d2968608fb73c6")
                .addParams("dtype", "json")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        MoviesBean moviesBean = gson.fromJson(s, MoviesBean.class);
                        if (moviesBean != null) {
                            List<MoviesBean.ResultBean> result = moviesBean.getResult();
                            if (result != null && onSpeakListener != null) {
                                int size = result.size();
                                onSpeakListener.startSpeaking("今天共上映" + size + "部影片，我随机给您推荐五部，它们分别是：" + result.get(4).getMovieName()
                                        + "、" + result.get(8).getMovieName()
                                        + "、" + result.get(2).getMovieName()
                                        + "、" + result.get(5).getMovieName()
                                        + "、" + result.get(10).getMovieName()
                                        + "、");
//                                if (size > 5) {
//                                    //随机选5部
//
//                                } else {
//                                    //全部说出来
//
//                                }
                            }
                        }
                    }
                });
    }

    private void getRandomNum() {
        int[] num = new int[10];//存储10个生成的数据
        int i = 0;//计数器 指示当前要填加到的数组下标,并指示当前已经添加了几个数
        boolean b;//判断是否重复的辅助变量
        while (i < 10) {
            //生成一个随机数
            int j = (int) (Math.random() * 100 + 1);
            //将辅助变量设置为true 表示可以添加到数组
            b = true;
            //循环判断是否重复
            for (int n = 0; n < i; n++) {
                //如果重复,设置辅助变量为false且跳出循环
                //如果不重复则会一直将已添加的数组历遍一次
                if (num[n] == j) {
                    b = false;
                    break;
                }
            }
            //如果可以添加 添加到存储数组 并将计数器i自加1
            if (b) {
                num[i] = j;
                i++;
            }
        }
        //将结果循环输出到控制台
        for (int j = 0; j < 10; j++) {
            System.out.println(num[j]);
        }
    }

    @Override
    public void requestLanternRiddles() {
        OkHttpUtils.post()
                .url(Constant.DENG_MI_URL)
                .addParams("appKey", "71db10ac155a444d8f806391e9b924c9")
                .addParams("typeId", "gxmy")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        LanternRiddlesBean lanternRiddlesBean = gson.fromJson(s, LanternRiddlesBean.class);
                        if (lanternRiddlesBean != null) {
                            LanternRiddlesBean.RESULTBean result = lanternRiddlesBean.getRESULT();
                            if (result != null) {
                                List<LanternRiddlesBean.RESULTBean.ContentlistBean> contentlist = result.getContentlist();
                                if (contentlist != null) {
                                    LanternRiddlesBean.RESULTBean.ContentlistBean contentlistBean = contentlist.get(0);
                                    if (contentlistBean != null && onSpeakListener != null) {
                                        onSpeakListener.startSpeaking("好的，" + contentlistBean.getTitle() + "。您猜猜是什么？");
                                    }
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void requestExpress(final String best_result_str) {
        //先得到快递公司编号列表
        OkHttpUtils.get()
                .url(Constant.JUHE_DATA_EXPRESS_COMPANY_NO_URL)
                .addParams("key", "7855badd19afa79631c402e17cd5ac8b")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Gson gson = new Gson();
                        ExpressCompanyBean expressCompanyBean = gson.fromJson(s, ExpressCompanyBean.class);
                        if (expressCompanyBean != null) {
                            List<ExpressCompanyBean.ResultBean> result = expressCompanyBean.getResult();
                            if (result != null) {
                                int size = result.size();
                                if (size != 0) {
                                    List<String> listNO = new ArrayList<>();
                                    for (int j = 0; j < size - 1; j++) {
                                        ExpressCompanyBean.ResultBean resultBean = result.get(j);
                                        listNO.add(resultBean.getNo());
                                    }
                                    requestRealExpress(listNO, best_result_str);//请求快递信息
                                }
                            }
                        }
                    }
                });
    }

    private boolean isWarning = true;

    private void requestRealExpress(List<String> listNO, String best_result_str) {
        if (listNO != null && listNO.size() != 0) {
            isWarning = true;
            int size = listNO.size();
            for (int i = 0; i < size; i++) {
                OkHttpUtils.get()
                        .url(Constant.JUHE_DATA_EXPRESS_INFO_URL)
                        .addParams("key", "7855badd19afa79631c402e17cd5ac8b")
                        .addParams("dtype", "json")
                        .addParams("com", listNO.get(i))//需要查询的快递公司编号
                        .addParams("no", best_result_str)//需要查询的订单号
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                LogUtils.e("请求网络数据异常" + e.toString());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                LogUtils.e("快递信息   >>>" + s);
                                boolean isEnterNext = false;
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(s);
                                    String resultcode = jsonObject.optString("resultcode");
                                    if (resultcode.equals("200")) {
                                        isEnterNext = true;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (isEnterNext) {
                                    Gson gson = new Gson();
                                    ExpressInfoBean expressInfoBean = gson.fromJson(s, ExpressInfoBean.class);
                                    if (expressInfoBean != null) {
                                        ExpressInfoBean.ResultBean result = expressInfoBean.getResult();
                                        if (result != null) {
                                            String company = result.getCompany();
                                            List<ExpressInfoBean.ResultBean.ListBean> list = result.getList();
                                            if (list != null) {
                                                int size = list.size();
                                                if (size != 0) {
                                                    ExpressInfoBean.ResultBean.ListBean listBean = list.get(size - 1);
                                                    if (listBean != null && onSpeakListener != null) {
                                                        String remark = listBean.getRemark();
                                                        onSpeakListener.startSpeaking("根据" + company + "最新物流信息：" + remark);

                                                        isWarning = false;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }
            if (isWarning) {
//                speak("不好意思，没有找到您要的快递！");
            }
        }
    }

    @Override
    public void requestJingDongRobot(String string, final long startTime) {
        OkHttpUtils.get()
                .url("https://way.jd.com/turing/turing")
                .addParams("info", string)
                .addParams("loc", "深圳市南山区深南大道28号")
                .addParams("userid", "222")
                .addParams("appkey", "e0ac22f54fa596bc2c600b501ebd594c")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        LogUtils.e("请求网络数据异常" + e.toString());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        LogUtils.e("京东云返回的数据   >>>" + s);
                        Gson gson = new Gson();
                        JingDongBean jingDongBean = gson.fromJson(s, JingDongBean.class);
                        if (jingDongBean != null) {
                            JingDongBean.ResultBean result = jingDongBean.getResult();
                            if (result != null) {
                                String text = result.getText();
                                if (text != null && onSpeakListener != null) {
                                    onSpeakListener.startSpeaking(text);
                                    LogUtils.e("闲聊耗时   >>>"+(TimeUtils.getNowMills()-startTime));
                                }
                            }
                        }
                    }
                });
    }
}
