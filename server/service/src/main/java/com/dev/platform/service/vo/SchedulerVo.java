package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author : liujj
 * @version : 1.0
 * @description : quartz定时任务vo
 * @date : 2021/6/16 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SchedulerVo {

    /**
     * 会议号
     */
    private String jobName;

    /**
     * 会控密码
     */
    private String jobGroup;

    /**
     * 密码
     */
    private String jobCron;

    /**
     *
     */
    private Map<String,String> jobData;


    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public Map<String, String> getJobData() {
        return jobData;
    }

    public void setJobData(Map<String, String> jobData) {
        this.jobData = jobData;
    }
}
