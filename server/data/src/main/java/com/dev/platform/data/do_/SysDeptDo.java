package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_dept", schema = "base_paperless", catalog = "")
public class SysDeptDo {
    private Long id;
    private String name;
    private Long pid;
    private String path;
    private Integer ord;
    private String deptType;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;

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
    @Column(name = "pid", nullable = false)
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "path", nullable = false, length = 100)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
    @Column(name = "dept_type", nullable = false, length = 50)
    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysDeptDo sysDeptDo = (SysDeptDo) o;
        return id == sysDeptDo.id &&
                pid == sysDeptDo.pid &&
                ord == sysDeptDo.ord &&
                updateBy == sysDeptDo.updateBy &&
                Objects.equals(name, sysDeptDo.name) &&
                Objects.equals(path, sysDeptDo.path) &&
                Objects.equals(deptType, sysDeptDo.deptType) &&
                Objects.equals(createTime, sysDeptDo.createTime) &&
                Objects.equals(lastUpdate, sysDeptDo.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pid, path, ord, deptType, createTime, lastUpdate, updateBy);
    }
}
