package com.dev.platform.data.dto;

/**
 * @Classname SysOrgUserDto
 * @Description   部门  组织 用户 dto
 * @Date 2021/6/17 16:08
 * @Author Mr.zsf
 */
public class DeptGroupUserDto {

    private String type;
    private long id;
    private String name;
    private String fullPath;
    private String userName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
