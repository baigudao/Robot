package com.taisau.robot.bean;

/**
 * Created by Devin on 2018/7/3.
 */
public class JingDongBean {

    /**
     * code : 10000
     * charge : false
     * msg : 查询成功
     * result : {"code":200000,"text":"亲，北京天气情况如下","url":"http://m.sohu.com/weather/?city=%E5%8C%97%E4%BA%AC"}
     */

    private String code;
    private boolean charge;
    private String msg;
    private ResultBean result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isCharge() {
        return charge;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * code : 200000
         * text : 亲，北京天气情况如下
         * url : http://m.sohu.com/weather/?city=%E5%8C%97%E4%BA%AC
         */

        private int code;
        private String text;
        private String url;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
