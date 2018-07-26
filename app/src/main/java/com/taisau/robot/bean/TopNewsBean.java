package com.taisau.robot.bean;

import java.util.List;

/**
 * Created by Devin on 2018/6/29.
 */
public class TopNewsBean {

    /**
     * error_code : 0
     * reason : 成功的返回
     * result : {"data":[{"author_name":"红蓝对抗","category":"头条","date":"2018-06-29 13:24","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_2_mwpm_03200403.jpg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_5_mwpm_03200403.jpg","title":"马拉多纳回应世界杯观赛失态：都是白酒惹的祸 康复后舞姿亮了","uniquekey":"e083f4de0ad661484f39d3310a3a743f","url":"http://mini.eastday.com/mobile/180629132415141.html"},{"author_name":"毛开云","category":"头条","date":"2018-06-29 13:23","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_1_mwpm_03200403.jpg","thumbnail_pic_s03":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_2_mwpm_03200403.jpg","title":"美国要求各国停止进口伊朗石油，三国奋起反击必定引发连锁反应","uniquekey":"29905d77e26c96a528430125b7f04c83","url":"http://mini.eastday.com/mobile/180629132324584.html"}],"stat":"1"}
     */

    private int error_code;
    private String reason;
    private ResultBean result;

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

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * data : [{"author_name":"红蓝对抗","category":"头条","date":"2018-06-29 13:24","thumbnail_pic_s":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_2_mwpm_03200403.jpg","thumbnail_pic_s02":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_4_mwpm_03200403.jpg","thumbnail_pic_s03":"http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_5_mwpm_03200403.jpg","title":"马拉多纳回应世界杯观赛失态：都是白酒惹的祸 康复后舞姿亮了","uniquekey":"e083f4de0ad661484f39d3310a3a743f","url":"http://mini.eastday.com/mobile/180629132415141.html"},{"author_name":"毛开云","category":"头条","date":"2018-06-29 13:23","thumbnail_pic_s":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_3_mwpm_03200403.jpg","thumbnail_pic_s02":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_1_mwpm_03200403.jpg","thumbnail_pic_s03":"http://03.imgmini.eastday.com/mobile/20180629/20180629132324_265341b04c867cccd90abfc99d6d7037_2_mwpm_03200403.jpg","title":"美国要求各国停止进口伊朗石油，三国奋起反击必定引发连锁反应","uniquekey":"29905d77e26c96a528430125b7f04c83","url":"http://mini.eastday.com/mobile/180629132324584.html"}]
         * stat : 1
         */

        private String stat;
        private List<DataBean> data;

        public String getStat() {
            return stat;
        }

        public void setStat(String stat) {
            this.stat = stat;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * author_name : 红蓝对抗
             * category : 头条
             * date : 2018-06-29 13:24
             * thumbnail_pic_s : http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_2_mwpm_03200403.jpg
             * thumbnail_pic_s02 : http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_4_mwpm_03200403.jpg
             * thumbnail_pic_s03 : http://08.imgmini.eastday.com/mobile/20180629/20180629132415_fc262990c9cdca3e44b92371254ff637_5_mwpm_03200403.jpg
             * title : 马拉多纳回应世界杯观赛失态：都是白酒惹的祸 康复后舞姿亮了
             * uniquekey : e083f4de0ad661484f39d3310a3a743f
             * url : http://mini.eastday.com/mobile/180629132415141.html
             */

            private String author_name;
            private String category;
            private String date;
            private String thumbnail_pic_s;
            private String thumbnail_pic_s02;
            private String thumbnail_pic_s03;
            private String title;
            private String uniquekey;
            private String url;

            public String getAuthor_name() {
                return author_name;
            }

            public void setAuthor_name(String author_name) {
                this.author_name = author_name;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getThumbnail_pic_s() {
                return thumbnail_pic_s;
            }

            public void setThumbnail_pic_s(String thumbnail_pic_s) {
                this.thumbnail_pic_s = thumbnail_pic_s;
            }

            public String getThumbnail_pic_s02() {
                return thumbnail_pic_s02;
            }

            public void setThumbnail_pic_s02(String thumbnail_pic_s02) {
                this.thumbnail_pic_s02 = thumbnail_pic_s02;
            }

            public String getThumbnail_pic_s03() {
                return thumbnail_pic_s03;
            }

            public void setThumbnail_pic_s03(String thumbnail_pic_s03) {
                this.thumbnail_pic_s03 = thumbnail_pic_s03;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUniquekey() {
                return uniquekey;
            }

            public void setUniquekey(String uniquekey) {
                this.uniquekey = uniquekey;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
