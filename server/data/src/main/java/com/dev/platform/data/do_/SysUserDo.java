package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_user", schema = "base_paperless", catalog = "")
public class SysUserDo {
    private Long id;
    private String username;
    private String password;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Boolean disabled;
    private String detInfo;
    private Long deptId;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "last_update", nullable = true)
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Basic
    @Column(name = "update_by", nullable = false)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Basic
    @Column(name = "disabled", nullable = false)
    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    @Basic
    @Column(name = "det_info", nullable = false)
    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }

    @Basic
    @Column(name = "dept_id", nullable = false)
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysUserDo sysUserDo = (SysUserDo) o;
        return id == sysUserDo.id &&
                updateBy == sysUserDo.updateBy &&
                disabled == sysUserDo.disabled &&
                Objects.equals(username, sysUserDo.username) &&
                Objects.equals(password, sysUserDo.password) &&
                Objects.equals(createTime, sysUserDo.createTime) &&
                Objects.equals(lastUpdate, sysUserDo.lastUpdate) &&
                Objects.equals(detInfo, sysUserDo.detInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, createTime, lastUpdate, updateBy, disabled, detInfo);
    }
}
