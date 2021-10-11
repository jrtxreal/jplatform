package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_src", schema = "base_paperless", catalog = "")
public class SysSrcDo {
    private Long id;
    private String name;
    private String code;
    private String des;
    private Boolean deprecated;
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
    @Column(name = "code", nullable = false, length = 50)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "des", nullable = false, length = 100)
    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Basic
    @Column(name = "deprecated", nullable = false)
    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
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
        SysSrcDo sysSrcDo = (SysSrcDo) o;
        return id == sysSrcDo.id &&
                deprecated == sysSrcDo.deprecated &&
                updateBy == sysSrcDo.updateBy &&
                Objects.equals(name, sysSrcDo.name) &&
                Objects.equals(code, sysSrcDo.code) &&
                Objects.equals(des, sysSrcDo.des) &&
                Objects.equals(createTime, sysSrcDo.createTime) &&
                Objects.equals(lastUpdate, sysSrcDo.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, des, deprecated, createTime, lastUpdate, updateBy);
    }
}
