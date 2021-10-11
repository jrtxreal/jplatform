package com.dev.platform.data.dto;

import java.sql.Timestamp;

import com.dev.platform.data.def.JpaDto;

@JpaDto
public class SysSrcDto {
    private Long id;
    private String name;
    private String code;
    private String des;
    private Boolean deprecated;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Long srcGrpId;
    private String srcGrpName;
    private Integer checkTag;
    private Integer ord;
    private String deptPath;

    public String getDeptPath() {
        return deptPath;
    }

    public void setDeptPath(String deptPath) {
        this.deptPath = deptPath;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
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

    public Long getSrcGrpId() {
        return srcGrpId;
    }

    public void setSrcGrpId(Long srcGrpId) {
        this.srcGrpId = srcGrpId;
    }

    public String getSrcGrpName() {
        return srcGrpName;
    }

    public void setSrcGrpName(String srcGrpName) {
        this.srcGrpName = srcGrpName;
    }

    public Integer getCheckTag() {
        return checkTag;
    }

    public void setCheckTag(Integer checkTag) {
        this.checkTag = checkTag;
    }

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }
}
