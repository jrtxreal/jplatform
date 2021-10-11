package com.dev.platform.controller.vo;

import javax.validation.constraints.NotNull;

import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.def.ErrorType;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/2 10:36
 */
public class PageRequestVo {
    @NotNull(message = "page不能为空")
    @ErrorType(ExceptionCmpt.DEV)
    private Integer page;
    @NotNull(message = "size不能为空")
    @ErrorType(ExceptionCmpt.DEV)
    private Integer size;
    private String sort;
    // 1 = asc 2 = desc
    private Integer  direction;
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}
