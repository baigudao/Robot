package com.taisau.robot.bean;

import java.util.List;

/**
 * Created by Devin on 2018/6/29.
 */
public class MoviesBean {

    /**
     * error_code : 0
     * reason : success
     * result : [{"movieId":"258153","movieName":"姽婳","pic_url":"http://img5.mtime.cn/mt/2018/06/08/145945.30124629_182X243X4.jpg"},{"movieId":"257858","movieName":"最后一球","pic_url":"http://img5.mtime.cn/mt/2018/06/26/111634.77947793_182X243X4.jpg"},{"movieId":"256650","movieName":"恐怖浴室","pic_url":"http://img5.mtime.cn/mt/2018/05/04/151534.74614121_182X243X4.jpg"},{"movieId":"256276","movieName":"因果启示录","pic_url":"http://img5.mtime.cn/mt/2018/04/28/141649.67406890_182X243X4.jpg"},{"movieId":"255800","movieName":"青春不留白","pic_url":"http://img5.mtime.cn/mt/2018/06/20/142722.82384491_182X243X4.jpg"},{"movieId":"254656","movieName":"潜艇总动员：海底两万里","pic_url":"http://img5.mtime.cn/mt/2018/05/24/112442.80786906_182X243X4.jpg"},{"movieId":"254184","movieName":"暹罗决：九神战甲","pic_url":"http://img5.mtime.cn/mt/2018/06/05/110114.29268063_182X243X4.jpg"},{"movieId":"253011","movieName":"厕所英雄","pic_url":"http://img5.mtime.cn/mt/2018/05/31/190850.56463522_182X243X4.jpg"},{"movieId":"250858","movieName":"寂静之地","pic_url":"http://img5.mtime.cn/mt/2018/05/07/162525.98877773_182X243X4.jpg"},{"movieId":"250729","movieName":"超时空同居","pic_url":"http://img5.mtime.cn/mt/2018/05/05/175421.64702950_182X243X4.jpg"},{"movieId":"250595","movieName":"泄密者","pic_url":"http://img5.mtime.cn/mt/2018/06/12/144615.15753425_182X243X4.jpg"},{"movieId":"247378","movieName":"哆啦A梦：大雄的金银岛","pic_url":"http://img5.mtime.cn/mt/2018/05/28/103843.25274692_182X243X4.jpg"},{"movieId":"244257","movieName":"龙虾刑警","pic_url":"http://img5.mtime.cn/mt/2018/06/11/114337.56675383_182X243X4.jpg"},{"movieId":"240989","movieName":"动物世界","pic_url":"http://img5.mtime.cn/mt/2018/06/24/173836.91706768_182X243X4.jpg"},{"movieId":"237536","movieName":"生存家族","pic_url":"http://img5.mtime.cn/mt/2018/06/12/102343.67282689_182X243X4.jpg"},{"movieId":"237446","movieName":"金蝉脱壳2","pic_url":"http://img5.mtime.cn/mt/2018/06/06/122847.71875760_182X243X4.jpg"},{"movieId":"234987","movieName":"猛虫过江","pic_url":"http://img5.mtime.cn/mt/2018/06/05/091818.46413077_182X243X4.jpg"},{"movieId":"232316","movieName":"完美陌生人","pic_url":"http://img5.mtime.cn/mt/2018/05/17/102651.23426030_182X243X4.jpg"},{"movieId":"229345","movieName":"青年马克思","pic_url":"http://img5.mtime.cn/mt/2018/05/02/144324.94176866_182X243X4.jpg"},{"movieId":"225759","movieName":"侏罗纪世界2","pic_url":"http://img5.mtime.cn/mt/2018/05/25/100000.65855294_182X243X4.jpg"},{"movieId":"225720","movieName":"冰河追凶","pic_url":"http://img31.mtime.cn/mt/2016/03/18/091618.24526976_182X243X4.jpg"},{"movieId":"223888","movieName":"夺命旅行","pic_url":"http://img31.mtime.cn/mt/2015/05/15/184935.48442893.jpg"},{"movieId":"223686","movieName":"超人总动员2","pic_url":"http://img5.mtime.cn/mt/2018/05/23/125020.85709614_182X243X4.jpg"},{"movieId":"219707","movieName":"第七个小矮人","pic_url":"http://img5.mtime.cn/mt/2018/05/30/103453.69566094_182X243X4.jpg"},{"movieId":"217723","movieName":"重生爱人","pic_url":"http://img31.mtime.cn/mt/2015/05/15/155053.90411520.jpg"},{"movieId":"217497","movieName":"复仇者联盟3：无限战争","pic_url":"http://img5.mtime.cn/mt/2018/03/30/101316.99752366_182X243X4.jpg"},{"movieId":"10053","movieName":"阿飞正传","pic_url":"http://img5.mtime.cn/mt/2018/06/21/113853.36979689_182X243X4.jpg"}]
     */

    private int error_code;
    private String reason;
    private List<ResultBean> result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * movieId : 258153
         * movieName : 姽婳
         * pic_url : http://img5.mtime.cn/mt/2018/06/08/145945.30124629_182X243X4.jpg
         */

        private String movieId;
        private String movieName;
        private String pic_url;

        public String getMovieId() {
            return movieId;
        }

        public void setMovieId(String movieId) {
            this.movieId = movieId;
        }

        public String getMovieName() {
            return movieName;
        }

        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        public String getPic_url() {
            return pic_url;
        }

        public void setPic_url(String pic_url) {
            this.pic_url = pic_url;
        }
    }
}
