package com.dev.platform.data.dto;

import java.sql.Timestamp;

import com.dev.platform.data.def.JpaDto;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/8 14:19
 */
@JpaDto
public class SysUserDto2 {
    private Long id;
    private String username;
    private String password;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Boolean disabled;
    private String detInfo;
    private Long deptId;
    private Long fmRoleId;
    private String fmRoleName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getFmRoleId() {
        return fmRoleId;
    }

    public void setFmRoleId(Long fmRoleId) {
        this.fmRoleId = fmRoleId;
    }

    public String getFmRoleName() {
        return fmRoleName;
    }

    public void setFmRoleName(String fmRoleName) {
        this.fmRoleName = fmRoleName;
    }
}
