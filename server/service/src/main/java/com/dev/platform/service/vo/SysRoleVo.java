package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

import java.sql.Timestamp;

public class SysRoleVo  {
    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
    @NotBlank(message = "角色名称不能为空")
    private String name;
    private Integer ord;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Boolean def;
    private Boolean disabled;
    private Long deptId;

    //控制部门
    private String deptIds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
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

    public Boolean getDef() {
        return def;
    }

    public void setDef(Boolean def) {
        this.def = def;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }
}
