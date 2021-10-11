package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dev.platform.data.dao.SysGlobalSettingDao;
import com.dev.platform.data.do_.SysGlobalSettingDo;
import com.dev.platform.service.SysSettingService;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysGlobalSettingVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 9:03
 */
@Service
public class SysSettingServiceImpl extends BasicServiceImpl implements SysSettingService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    SysGlobalSettingDao sysGlobalSettingDao;
    @Override
    public void editSysGlobalSetting(SysGlobalSettingVo sysGlobalSettingVo) throws Exception{
        ValidateUtil.validate(sysGlobalSettingVo);
        SysGlobalSettingDo sysGlobalSettingDo = new SysGlobalSettingDo();
        sysGlobalSettingDo.setId(sysGlobalSettingVo.getId());
        sysGlobalSettingDo.setKey(sysGlobalSettingVo.getKey());
        sysGlobalSettingDo.setVal(sysGlobalSettingVo.getVal());
        Date current= new Date();
        sysGlobalSettingDo.setLastUpdate(new Timestamp(current.getTime()));
        sysGlobalSettingDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysGlobalSettingDao.saveAndFlush(sysGlobalSettingDo);
    }

    @Override
    public List<SysGlobalSettingDo> getSysGlobalSetting() {
        List<SysGlobalSettingDo> sysGlobalSettingDos = sysGlobalSettingDao.findAll();
        if(sysGlobalSettingDos.isEmpty()){
            return null;
        }
        return sysGlobalSettingDos;
    }
}
