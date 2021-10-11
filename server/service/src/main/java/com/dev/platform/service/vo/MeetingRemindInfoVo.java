package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议提醒信息
 * @date : 2021/6/8 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingRemindInfoVo implements Serializable {

    private String meetingId;

    /**
     * 提醒内容
     */
    private String remindContent;

    /**
     * 会议前分钟
     */
    private Integer preTime;


    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public Integer getPreTime() {
        return preTime;
    }

    public void setPreTime(Integer preTime) {
        this.preTime = preTime;
    }
}
