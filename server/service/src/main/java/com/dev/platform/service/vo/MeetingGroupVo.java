package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议参与人员信息
 * @date : 2021/6/8 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingGroupVo implements Serializable {

    /**
     * 组织id
     */
    private Long groupId;

    /**
     * 组织名称
     */
    private String groupName;

    /**
     * 用户信息
     */
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<MeetingUserVo> userList;


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MeetingUserVo> getUserList() {
        return userList;
    }

    public void setUserList(List<MeetingUserVo> userList) {
        this.userList = userList;
    }
}
