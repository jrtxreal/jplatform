package com.dev.platform.data.do_;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "msg_from_to", schema = "base_paperless", catalog = "")
public class MsgFromToDo {
    private Long id;
    private Long fromUser;
    private Long toUser;
    private Long msgId;
    private Boolean readTag;

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
    @Column(name = "from_user", nullable = false)
    public Long getFromUser() {
        return fromUser;
    }

    public void setFromUser(Long fromUser) {
        this.fromUser = fromUser;
    }

    @Basic
    @Column(name = "to_user", nullable = false)
    public Long getToUser() {
        return toUser;
    }

    public void setToUser(Long toUser) {
        this.toUser = toUser;
    }

    @Basic
    @Column(name = "msg_id", nullable = false)
    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    @Basic
    @Column(name = "read_tag", nullable = false)
    public Boolean getReadTag() {
        return readTag;
    }

    public void setReadTag(Boolean readTag) {
        this.readTag = readTag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsgFromToDo that = (MsgFromToDo) o;
        return id == that.id &&
                fromUser == that.fromUser &&
                toUser == that.toUser &&
                msgId == that.msgId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUser, toUser, msgId);
    }
}
