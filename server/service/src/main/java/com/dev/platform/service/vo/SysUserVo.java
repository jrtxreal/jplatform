package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @Description:用户VO
 * @PackgeName: com.dev.platform.paperless.service.vo
 * @ClassName: SysUserVo
 * @Author: fanjunhui
 * @Date: 2021/05/31 14:18
 * @Version: V 1.0
 */
public class SysUserVo {

    @Positive
    @ValidateGroup({ValidateGroupType.edit})
    private long id;
//    @NotBlank(message = "用户名不能为空")
//    private String username;

    @NotBlank(message = "密码不能为空")
    @ValidateGroup({ValidateGroupType.add})
    private String password;

    private boolean disabled;
    private String detInfo;
    private long deptId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }

    public long getDeptId() {
        return deptId;
    }

    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }
}
