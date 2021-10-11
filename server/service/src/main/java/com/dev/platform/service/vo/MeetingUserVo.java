package com.dev.platform.service.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议人员议题信息对象
 * @date : 2021/6/19 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingUserVo implements Serializable {

    private Long userId;

    /**
     * 中文名称
     */
    private String nickName;


    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 与会人员
     */
    private Long meetingDocId;

    /**
     * 会议议题文件，对应个人议题文件映射
     */
    private Map<Long, Long> fileMap;


    /**
     * 会议议题文件夹，对应个人议题文件夹映射
     */
    private Map<Long,Long> topicMap;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMeetingDocId() {
        return meetingDocId;
    }

    public void setMeetingDocId(Long meetingDocId) {
        this.meetingDocId = meetingDocId;
    }

    public Map<Long, Long> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<Long, Long> fileMap) {
        this.fileMap = fileMap;
    }

    public Map<Long, Long> getTopicMap() {
        return topicMap;
    }

    public void setTopicMap(Map<Long, Long> topicMap) {
        this.topicMap = topicMap;
    }
}
