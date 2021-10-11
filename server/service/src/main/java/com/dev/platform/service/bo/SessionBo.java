package com.dev.platform.service.bo;

import com.dev.platform.service.pojo.BasicUserInfo;

import java.util.Set;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : session数据结构
 * @date : 2021/5/31 17:45
 */
public class SessionBo {
    private BasicUserInfo basicUserInfo;
    private Set<String> roles;   //roleid@deptPath
    private Set<String> permits; //permitname@deptPath
    private String json;
    private String token;
    private Boolean sa;
    private Long pfId; // 个人文件夹根目录 personal file Id
    private String loginTime;
    private String loginDevice;
    private String loginIp;

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Boolean getSa() {
        return sa;
    }

    public void setSa(Boolean sa) {
        this.sa = sa;
    }

    public Long getPfId() {
        return pfId;
    }

    public void setPfId(Long pfId) {
        this.pfId = pfId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BasicUserInfo getBasicUserInfo() {
        return basicUserInfo;
    }

    public void setBasicUserInfo(BasicUserInfo basicUserInfo) {
        this.basicUserInfo = basicUserInfo;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getPermits() {
        return permits;
    }

    public void setPermits(Set<String> permits) {
        this.permits = permits;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
