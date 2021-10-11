package com.dev.platform.data.dto;

import java.sql.Timestamp;
import java.util.List;

import com.dev.platform.data.def.JpaDto;

@JpaDto
public class SysSrcGroupDto {
    private Long id;
    private String name;
    private Integer ord;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    private String updateBy;
    private List<SysSrcDto> list;

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

    public List<SysSrcDto> getList() {
        return list;
    }

    public void setList(List<SysSrcDto> list) {
        this.list = list;
    }
}
