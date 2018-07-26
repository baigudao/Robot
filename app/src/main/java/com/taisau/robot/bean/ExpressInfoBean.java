package com.taisau.robot.bean;

import java.util.List;

/**
 * Created by Devin on 2018/7/2.
 */
public class ExpressInfoBean {

    /**
     * resultcode : 200
     * reason : 查询成功!
     * result : {"company":"圆通","com":"yt","no":"800410019700732561","status":"0","list":[{"datetime":"2018-06-30 20:40:35","remark":"【江苏省无锡市公司】 取件人: 曹国强 已收件","zone":""},{"datetime":"2018-06-30 20:46:39","remark":"【江苏省无锡市公司】 已收件","zone":""},{"datetime":"2018-06-30 20:46:39","remark":"【江苏省无锡市公司】 已打包","zone":""},{"datetime":"2018-06-30 20:55:16","remark":"【江苏省无锡市公司】 已发出 下一站 【无锡转运中心】","zone":""},{"datetime":"2018-07-01 00:57:55","remark":"【无锡转运中心】 已收入","zone":""},{"datetime":"2018-07-01 00:59:00","remark":"【无锡转运中心】 已发出 下一站 【深圳转运中心】","zone":""},{"datetime":"2018-07-02 07:57:10","remark":"【深圳转运中心】 已收入","zone":""},{"datetime":"2018-07-02 09:39:02","remark":"【深圳转运中心】 已发出 下一站 【广东省深圳市宝安区固戍公司】","zone":""}]}
     * error_code : 0
     */

    private String resultcode;
    private String reason;
    private ResultBean result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
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

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * company : 圆通
         * com : yt
         * no : 800410019700732561
         * status : 0
         * list : [{"datetime":"2018-06-30 20:40:35","remark":"【江苏省无锡市公司】 取件人: 曹国强 已收件","zone":""},{"datetime":"2018-06-30 20:46:39","remark":"【江苏省无锡市公司】 已收件","zone":""},{"datetime":"2018-06-30 20:46:39","remark":"【江苏省无锡市公司】 已打包","zone":""},{"datetime":"2018-06-30 20:55:16","remark":"【江苏省无锡市公司】 已发出 下一站 【无锡转运中心】","zone":""},{"datetime":"2018-07-01 00:57:55","remark":"【无锡转运中心】 已收入","zone":""},{"datetime":"2018-07-01 00:59:00","remark":"【无锡转运中心】 已发出 下一站 【深圳转运中心】","zone":""},{"datetime":"2018-07-02 07:57:10","remark":"【深圳转运中心】 已收入","zone":""},{"datetime":"2018-07-02 09:39:02","remark":"【深圳转运中心】 已发出 下一站 【广东省深圳市宝安区固戍公司】","zone":""}]
         */

        private String company;
        private String com;
        private String no;
        private String status;
        private List<ListBean> list;

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCom() {
            return com;
        }

        public void setCom(String com) {
            this.com = com;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * datetime : 2018-06-30 20:40:35
             * remark : 【江苏省无锡市公司】 取件人: 曹国强 已收件
             * zone :
             */

            private String datetime;
            private String remark;
            private String zone;

            public String getDatetime() {
                return datetime;
            }

            public void setDatetime(String datetime) {
                this.datetime = datetime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getZone() {
                return zone;
            }

            public void setZone(String zone) {
                this.zone = zone;
            }
        }
    }
}
