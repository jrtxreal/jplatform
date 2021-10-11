package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议基础信息
 * @date : 2021/6/8 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingInfoVo implements Serializable {

    /**
     * 会议室
     */
    private String meetingRoom;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactNumber;

    /**
     * 主持人
     */
    private String host;

    /**
     * 主办部门
     */
    private String dept;

    /**
     * 会议室编号：小鱼
     */
    private String meetingNumber;

    /**
     * 会议文件夹id
     */
    private Long meetingDocId;


    public String getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(String meetingRoom) {
        this.meetingRoom = meetingRoom;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(String meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public Long getMeetingDocId() {
        return meetingDocId;
    }

    public void setMeetingDocId(Long meetingDocId) {
        this.meetingDocId = meetingDocId;
    }
}
