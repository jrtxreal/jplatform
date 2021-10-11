package com.dev.platform.data.dto;

import java.sql.Timestamp;

import com.dev.platform.data.def.JpaDto;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/1 11:09
 */
@JpaDto
public class MsgDto {
    private Long id;
    private Timestamp sendTime;
    private Timestamp cancelTime;
    private String detInfo;
    private Long fromUser;
    private String fromUserName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }

    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }
}
