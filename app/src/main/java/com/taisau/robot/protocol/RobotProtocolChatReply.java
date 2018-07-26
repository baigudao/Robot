package com.taisau.robot.protocol;

import java.util.Date;

/**
 * Created by Devin on 2018/5/31.
 */
public class RobotProtocolChatReply {
    /**
     * 会话类型：
     *
     * 1，机器人端发出的。
     * 2，服务端AIS 智能自动回复的。
     */
    private char type;
    private String msg;
    private Date time;

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
