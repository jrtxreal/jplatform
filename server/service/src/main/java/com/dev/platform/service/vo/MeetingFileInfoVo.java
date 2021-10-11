package com.dev.platform.service.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : liujj
 * @version : 1.0
 * @description : 会议议题文件信息
 * @date : 2021/6/21 10:10
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingFileInfoVo implements Serializable {


    /**
     * wps path
     */
    private Long fileId;

    /**
     * wps path
     */
    private String wpsPath;

    /**
     * ftp path
     */
    private String ftpPath;


    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小:单位b
     */
    private Long size;

    /**
     * 文件上传时间
     */
    private Date uploadTime;

    /**
     * 文件类型
     */
    private String docType;


    /**
     * id：前端操作使用无意义
     */
    private Long id;


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getWpsPath() {
        return wpsPath;
    }

    public void setWpsPath(String wpsPath) {
        this.wpsPath = wpsPath;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
