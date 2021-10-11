package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_role", schema = "base_paperless", catalog = "")
public class SysRoleDo {
    private Long id;
    private String name;
    private Integer ord;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private Boolean def;
    private Boolean disabled;
    private Long deptId;
    private String type;

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
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "ord", nullable = false)
    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
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
    @Column(name = "def", nullable = false)
    public Boolean getDef() {
        return def;
    }

    public void setDef(Boolean def) {
        this.def = def;
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
    @Column(name = "type", nullable = false, length = 50)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        SysRoleDo sysRoleDo = (SysRoleDo) o;
        return id ==(sysRoleDo.id) &&
                ord.equals(sysRoleDo.ord) &&
                updateBy.equals(sysRoleDo.updateBy) &&
                def.equals(sysRoleDo.def) &&
                disabled.equals(sysRoleDo.disabled) &&
                Objects.equals(name, sysRoleDo.name) &&
                Objects.equals(createTime, sysRoleDo.createTime) &&
                Objects.equals(lastUpdate, sysRoleDo.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, ord, createTime, lastUpdate, updateBy, def, disabled);
    }


}
