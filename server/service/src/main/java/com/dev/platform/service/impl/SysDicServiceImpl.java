package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dev.platform.data.dao.SysDicDao;
import com.dev.platform.data.do_.SysDicDo;
import com.dev.platform.service.SysDicService;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysDicVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/5/31 16:31
 */
@Service
public class SysDicServiceImpl extends BasicServiceImpl implements SysDicService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    SysDicDao sysDicDao;

    @Override
    public void addSysDic(SysDicVo sysDicVo) throws Exception {
        ValidateUtil.validate(sysDicVo);
        SysDicDo sysDicDo = new SysDicDo();
        sysDicDo.setKey(sysDicVo.getKey());
        sysDicDo.setVal(sysDicVo.getVal());
        sysDicDo.setOrd(sysDicVo.getOrd());
        sysDicDo.setDeprecated(sysDicVo.isDeprecated());
        Date current = new Date();
        sysDicDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDicDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysDicDo.setDicGrpId(sysDicVo.getDicGrpId());
        sysDicDao.saveAndFlush(sysDicDo);
    }

    @Override
    public void deleteSysDic(long dicId) {
        sysDicDao.deleteById(dicId);
    }

    @Override
    public void editSysDic(SysDicVo sysDicVo) throws Exception {
        ValidateUtil.validate(sysDicVo);
        SysDicDo sysDicDo = new SysDicDo();
        sysDicDo.setId(sysDicVo.getId());
        sysDicDo.setKey(sysDicVo.getKey());
        sysDicDo.setVal(sysDicVo.getVal());
        sysDicDo.setDeprecated(sysDicVo.isDeprecated());
        Date current = new Date();
        sysDicDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDicDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysDicDo.setDicGrpId(sysDicVo.getDicGrpId());
        sysDicDo.setOrd(sysDicVo.getOrd());
        sysDicDao.saveAndFlush(sysDicDo);
    }

    @Override
    public List<SysDicDo> querySysDicByDicGrpId(long dicGrpId,String name) {
        List<SysDicDo> sysDicDos = sysDicDao.querySysDicByDicGrpId(dicGrpId,name);
        return sysDicDos;
    }
}
