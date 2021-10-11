package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dev.platform.data.dao.SysUserSettingDao;
import com.dev.platform.data.do_.SysUserSettingDo;
import com.dev.platform.service.SysUserSettingService;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.UserSettingDef;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysUserSettingVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/6/1 9:39
 */
@Service
public class SysUserSettingServiceImpl extends BasicServiceImpl implements SysUserSettingService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    SysUserSettingDao sysUserSettingDao;

    @Override
    public void editSysUserSetting(SysUserSettingVo sysUserSettingVo) throws Exception {
        ValidateUtil.validate(sysUserSettingVo);
        SysUserSettingDo sysUserSettingDo = new SysUserSettingDo();
        sysUserSettingDo.setKey(sysUserSettingVo.getKey());
        sysUserSettingDo.setVal(sysUserSettingVo.getVal());
        Date current = new Date();
        sysUserSettingDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserSettingDo.setUserId(SubjectUtil.getCurrentUserId(sessionCmpt));
        sysUserSettingDao.saveAndFlush(sysUserSettingDo);
    }

    @Override
    public SysUserSettingVo findUserSetting() throws Exception {
        long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        SysUserSettingDo sysUserSettingDo = sysUserSettingDao.findUserSettingByUserId(userId);
        SysUserSettingVo sysUserSettingVo = new SysUserSettingVo();
        sysUserSettingVo.setKey(sysUserSettingDo.getKey());
        sysUserSettingVo.setVal(sysUserSettingDo.getVal());
        return sysUserSettingVo;
    }

    @Override
    public Map getOrderSortTypeSetting() throws Exception {
        long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        SysUserSettingDo sysUserSettingDo = sysUserSettingDao.findOrderTypeByIdAndOrder(userId, UserSettingDef.ORDER_TYPE);
        SysUserSettingDo sysUserSettingDo1 = sysUserSettingDao.findSortTypeByIdAndOrder(userId, UserSettingDef.SORT_TYPE);
        Map map = new HashMap();
        map.put(UserSettingDef.ORDER_TYPE, sysUserSettingDo.getVal());
        map.put(UserSettingDef.SORT_TYPE, sysUserSettingDo1.getVal());
        return map;
    }

    @Override
    @Transactional
    public void updateOrderSortTypeSetting(String order_type, String sort_type) throws Exception {
        long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        sysUserSettingDao.updateOrderType(userId, UserSettingDef.ORDER_TYPE, order_type);
        sysUserSettingDao.updateSortType(userId, UserSettingDef.SORT_TYPE, sort_type);
    }
}
