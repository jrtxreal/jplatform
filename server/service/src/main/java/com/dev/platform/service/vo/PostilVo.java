package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

import java.sql.Timestamp;

/**
 * @Classname PostilVo
 * @Description TODO
 * @Date 2021/6/15 11:10
 * @Author Mr.zsf
 */
public class PostilVo {
    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
    private String content;
    private Timestamp createTime;
    private Timestamp lastUpdate;
    @NotBlank(message = "文件不能为空")
    private Long fileId;
    private String fileName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }


}
