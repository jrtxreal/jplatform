package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "msg", schema = "base_paperless", catalog = "")
public class MsgDo {
    private Long id;
    private Timestamp sendTime;
    private Timestamp cancelTime;
    private String detInfo;

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
    @Column(name = "send_time", nullable = true)
    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    @Basic
    @Column(name = "cancel_time", nullable = true)
    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Basic
    @Column(name = "det_info", nullable = false, length = 500)
    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgDo msgDo = (MsgDo) o;
        return id == msgDo.id &&
                Objects.equals(sendTime, msgDo.sendTime) &&
                Objects.equals(cancelTime, msgDo.cancelTime) &&
                Objects.equals(detInfo, msgDo.detInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sendTime, cancelTime, detInfo);
    }
}
