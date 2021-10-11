package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "sys_src_link_src_grp", schema = "base_paperless", catalog = "")
public class SysSrcLinkSrcGrpDo {
    private Long id;
    private Integer ord;
    private Long srcId;
    private Long srcGrpId;

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
    @Column(name = "ord", nullable = false)
    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

    @Basic
    @Column(name = "src_id", nullable = false)
    public Long getSrcId() {
        return srcId;
    }

    public void setSrcId(Long srcId) {
        this.srcId = srcId;
    }

    @Basic
    @Column(name = "src_grp_id", nullable = false)
    public Long getSrcGrpId() {
        return srcGrpId;
    }

    public void setSrcGrpId(Long srcGrpId) {
        this.srcGrpId = srcGrpId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysSrcLinkSrcGrpDo that = (SysSrcLinkSrcGrpDo) o;
        return id == that.id &&
                ord == that.ord &&
                srcId == that.srcId &&
                srcGrpId == that.srcGrpId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ord, srcId, srcGrpId);
    }
}
