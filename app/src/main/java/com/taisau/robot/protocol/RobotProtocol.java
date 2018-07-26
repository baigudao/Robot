package com.taisau.robot.protocol;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Devin on 2018/5/31.
 */
public class RobotProtocol {
    /**
     * 协议版本，默认必传字段。
     */
    private String version = "v1.0";
    /**
     * 企业编码，必传字段。
     */
    private String orgCode;

    /**
     * 设备编码，同一公司下，设备编码是唯一的。   必传字段。
     */
    private String eqCode;

    /**
     * 当前待解析的语音文字。
     */
    private String voiceText;
    /**
     * 由服务器返回给机器人的  回复信息。 这个是相对于  当前 voiceText 的。
     */
    private String reply;

    /**
     * 连接交互类型， 这是一个枚举：
     * <pre>
     * 1，找人
     * 2，闲聊
     * </pre>
     */
    private int connectType;

    /**
     * 如果是找人，这里会有一个数量。
     */
    private int staffSize;

    /**
     * 聊天轨迹。
     *
     * 这里记录轨迹还是有用的，考虑到后续的大数据分析。
     * 但同时也会有一个性能问题，  当聊天的内容多了之后，  这个协议内容也会越来越多， 前后端传递也是网络消耗。
     */
    private LinkedList<RobotProtocolChatReply> chatList;

    /**
     * 表情  和  人员基本信息  等等 信息 可以放在这里。
     * 具体 的 内容是 包罗万象的，  key 可根据实际需求进行定义。
     */
    private Map<String, Object> extMap;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getEqCode() {
        return eqCode;
    }

    public void setEqCode(String eqCode) {
        this.eqCode = eqCode;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getConnectType() {
        return connectType;
    }

    public void setConnectType(int connectType) {
        this.connectType = connectType;
    }

    public int getStaffSize() {
        return staffSize;
    }

    public void setStaffSize(int staffSize) {
        this.staffSize = staffSize;
    }

    public LinkedList<RobotProtocolChatReply> getChatList() {
        return chatList;
    }

    public void setChatList(LinkedList<RobotProtocolChatReply> chatList) {
        this.chatList = chatList;
    }

    public Map<String, Object> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, Object> extMap) {
        this.extMap = extMap;
    }
}
