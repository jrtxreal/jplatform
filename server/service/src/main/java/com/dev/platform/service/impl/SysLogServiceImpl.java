package com.dev.platform.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysLogDao;
import com.dev.platform.data.do_.SysLogDo;
import com.dev.platform.service.SysLogService;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysLogVo;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/29 16:25
 */
@Service
public class SysLogServiceImpl extends BasicServiceImpl implements SysLogService {
    @Resource
    SysLogDao sysLogDao;

    @Override
    public void addSysLog(SysLogVo sysLogVo) throws Exception {
        ValidateUtil.validate(sysLogVo);
        SysLogDo sysLogDo = new SysLogDo();
        sysLogDo.setLoginIp(sysLogVo.getClientIp());
        sysLogDo.setLoginTime(sysLogVo.getLoginTime());
        sysLogDo.setLoginDevice(sysLogVo.getClientDevice());
        sysLogDo.setUsername(sysLogVo.getUsername());
        sysLogDo.setRequestTime(sysLogVo.getRequestTime());
        sysLogDo.setModule(sysLogVo.getModule());
        sysLogDo.setMethod(sysLogVo.getMethod());
        sysLogDao.saveAndFlush(sysLogDo);
    }

    @Override
    public Page<SysLogDo> querySysLogByPage(PageRequest pageRequest, String condition) {
        if (StringUtils.isBlank(condition)) {
            condition = null;
        }
        return sysLogDao.querySysLogByPage(pageRequest, condition);
    }
}
