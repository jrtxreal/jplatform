package com.dev.platform.service.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.def.ErrorType;
import com.dev.platform.service.def.ValidateGroup;
import com.dev.platform.service.def.ValidateGroupType;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/1 8:54
 */
public class MsgTplVo {
    @Positive(message = "消息模板id不能为空")
    @ValidateGroup({ValidateGroupType.edit})
    @ErrorType(ExceptionCmpt.DEV)
    private Long id;
    @NotBlank(message = "模板名称不能为空")
    private String name;
    @NotBlank(message = "必须选择模板类型")
    private String msgType;
    // 不设置的默认为10分钟;
    private Long cancellableTime;
    // 不设置默认为5分钟;
    private Long resendIntvl;
    // 不设置默认为0;
    private Integer maxret;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Long getCancellableTime() {
        return cancellableTime;
    }

    public void setCancellableTime(Long cancellableTime) {
        this.cancellableTime = cancellableTime;
    }

    public Long getResendIntvl() {
        return resendIntvl;
    }

    public void setResendIntvl(Long resendIntvl) {
        this.resendIntvl = resendIntvl;
    }

    public Integer getMaxret() {
        return maxret;
    }

    public void setMaxret(Integer maxret) {
        this.maxret = maxret;
    }
}
