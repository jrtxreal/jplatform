package com.dev.platform.service.vo;

import javax.validation.constraints.NotNull;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

import java.sql.Timestamp;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/12 14:45
 */
public class GmGroupVo {
    @ValidateGroup({ValidateGroupType.edit})
    private Long id;
    @NotNull(message = "团队名称不能为空")
    private String grpName;// 1
    private Boolean isDeptGrp;// 1
    private Long deptId;// 1
    private String auditStatus;// 1
    private Timestamp createTime;// 1
    private String createBy;// 1
    private Timestamp lastUpdate;// 1
    private Long fileId;// 1
    private String announcement; // 1
    private String logo; // 1
    private String color; // 1

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public Boolean getDeptGrp() {
        return isDeptGrp;
    }

    public void setDeptGrp(Boolean deptGrp) {
        isDeptGrp = deptGrp;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
