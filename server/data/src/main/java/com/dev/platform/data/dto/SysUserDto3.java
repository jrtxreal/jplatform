package com.dev.platform.data.dto;

import java.sql.Timestamp;

import com.dev.platform.data.def.JpaDto;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : UserDo的完全复制版
 * @date : 2021/6/16 22:35
 */
@JpaDto
public class SysUserDto3 {
    private Long id;
    private String username;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Boolean disabled;
    private String detInfo;
    private Long deptId;
    private Long docRoleId;
    private String docRoleName;
    private Long gluId;

    public Long getGluId() {
        return gluId;
    }

    public void setGluId(Long gluId) {
        this.gluId = gluId;
    }

    public Long getDocRoleId() {
        return docRoleId;
    }

    public void setDocRoleId(Long docRoleId) {
        this.docRoleId = docRoleId;
    }

    public String getDocRoleName() {
        return docRoleName;
    }

    public void setDocRoleName(String docRoleName) {
        this.docRoleName = docRoleName;
    }

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
}
