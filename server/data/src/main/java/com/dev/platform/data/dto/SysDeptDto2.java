package com.dev.platform.data.dto;

import com.dev.platform.data.def.JpaDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;


@JpaDto
public class SysDeptDto2 {
    private Long id;
    private String name;
    private Long pid;
    private String path;
    private Integer ord;
    private String deptType;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private String fullPath;
    private Boolean canDelete;
    // 用户管理部门的标志：1:用户的管理部门，2：用户管理的部门的子部门，3：非用户管理部门
    private Integer adminTag;

    public Integer getAdminTag() {
        return adminTag;
    }

    public void setAdminTag(Integer adminTag) {
        this.adminTag = adminTag;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    @JsonIgnore
    private Boolean remove=false;

    public Boolean getRemove() {
        return remove;
    }

    public void setRemove(Boolean remove) {
        this.remove = remove;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
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

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
