package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/7/17 22:13
 */
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_dic", schema = "base_paperless", catalog = "")
public class SysDicDo {
    private Long id;
    private String key;
    private String val;
    private Boolean deprecated;
    private Timestamp lastUpdate;
    private String updateBy;
    private Long dicGrpId;
    private Integer ord;

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
    @Column(name = "`key`", nullable = false, length = 50)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Basic
    @Column(name = "val", nullable = false, length = 50)
    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
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
    @Column(name = "last_update", nullable = true)
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Basic
    @Column(name = "update_by", nullable = false, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Basic
    @Column(name = "dic_grp_id", nullable = false)
    public Long getDicGrpId() {
        return dicGrpId;
    }

    public void setDicGrpId(Long dicGrpId) {
        this.dicGrpId = dicGrpId;
    }

    @Basic
    @Column(name = "ord", nullable = true)
    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysDicDo sysDicDo = (SysDicDo) o;
        return Objects.equals(id, sysDicDo.id) &&
                Objects.equals(key, sysDicDo.key) &&
                Objects.equals(val, sysDicDo.val) &&
                Objects.equals(deprecated, sysDicDo.deprecated) &&
                Objects.equals(lastUpdate, sysDicDo.lastUpdate) &&
                Objects.equals(updateBy, sysDicDo.updateBy) &&
                Objects.equals(dicGrpId, sysDicDo.dicGrpId) &&
                Objects.equals(ord, sysDicDo.ord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, val, deprecated, lastUpdate, updateBy, dicGrpId, ord);
    }
}
