package com.dev.platform.service.vo;

import javax.validation.constraints.Positive;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.def.ErrorType;
import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/1 10:53
 */
public class MsgVo {
    @Positive(message = "消息模板id不能为空")
    @ValidateGroup({ValidateGroupType.edit})
    @ErrorType(ExceptionCmpt.DEV)
    private long id;
    private String detInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetInfo() {
        return detInfo;
    }

    public void setDetInfo(String detInfo) {
        this.detInfo = detInfo;
    }
}
