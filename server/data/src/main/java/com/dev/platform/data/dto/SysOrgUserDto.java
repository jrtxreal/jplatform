package com.dev.platform.data.dto;

/**
 * @Classname SysOrgUserDto
 * @Description   选取组织机构用户
 * @Date 2021/6/17 16:08
 * @Author Mr.zsf
 */
public class SysOrgUserDto {

    private String name;
    private String userName;
    private long id;
    private boolean isDept=true; //是否是部门


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isDept() {
        return isDept;
    }

    public void setDept(boolean dept) {
        isDept = dept;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
