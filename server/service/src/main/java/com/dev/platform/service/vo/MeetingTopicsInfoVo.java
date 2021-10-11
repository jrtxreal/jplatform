package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议议题信息
 * @date : 2021/6/8 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingTopicsInfoVo implements Serializable {

    /**
     * 议题名称
     */
    private String topicName;

    /**
     * 汇报部门
     */
    private String dept;


    /**
     * 汇报人
     */
    private String reporter;


    /**
     * 汇报开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Timestamp startTime;


    /**
     * 汇报结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Timestamp endTime;


    private String time;

    /**
     * 议题文件夹的fileId
     */
    private Long topicDocId;

    /**
     * id：前端操作使用无意义
     */
    private Long topicId;


    /**
     * 会议议题文件
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<MeetingFileInfoVo> fileList;


    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getTopicDocId() {
        return topicDocId;
    }

    public void setTopicDocId(Long topicDocId) {
        this.topicDocId = topicDocId;
    }

    public List<MeetingFileInfoVo> getFileList() {
        return fileList;
    }

    public void setFileList(List<MeetingFileInfoVo> fileList) {
        this.fileList = fileList;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
