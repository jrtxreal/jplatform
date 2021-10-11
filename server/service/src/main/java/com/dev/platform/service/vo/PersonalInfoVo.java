package com.dev.platform.service.vo;

/**
 * @Classname PersonalInfoVo
 * @Description TODO
 * @Date 2021/6/15 9:07
 * @Author Mr.zsf
 */
public class PersonalInfoVo {
    private long id;
    private String username;
    private long deptId;
    private String deptName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
