package com.dev.platform.service.vo;

import javax.validation.constraints.Positive;

import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/12 14:46
 */
public class GmGroupLinkUserVo {
    @Positive(message = "id不能为空")
    @ValidateGroup({ValidateGroupType.edit})
    private Long id;
    @Positive(message = "团队id不能为空")
    @ValidateGroup({ValidateGroupType.add})
    private Long grpId;
    @Positive(message = "用户id不能为空")
    @ValidateGroup({ValidateGroupType.add})
    private Long userId;
    @Positive(message = "角色id不能为空")
    @ValidateGroup({ValidateGroupType.edit})
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGrpId() {
        return grpId;
    }

    public void setGrpId(Long grpId) {
        this.grpId = grpId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
