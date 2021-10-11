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
 * @date : 2021/5/31 17:45
 */
public class SysSrcVo {
    @Positive(message = "id不能为空")
    @ValidateGroup({ValidateGroupType.edit})
    @ErrorType(ExceptionCmpt.DEV)
    private Long id;
    @NotBlank(message = "资源名称不能为空")
    private String name;
    @NotBlank(message = "资源编码不能为空")
    private String code;
    private Integer ord;
    private String des;
    private Boolean deprecated;

    public Integer getOrd() {
        return ord;
    }

    public void setOrd(Integer ord) {
        this.ord = ord;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }
}
