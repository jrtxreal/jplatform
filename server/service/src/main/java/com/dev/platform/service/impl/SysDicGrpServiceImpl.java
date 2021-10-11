package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.dev.platform.data.dao.SysDicDao;
import com.dev.platform.data.dao.SysDicGrpDao;
import com.dev.platform.data.do_.SysDicDo;
import com.dev.platform.data.do_.SysDicGrpDo;
import com.dev.platform.service.SysDicGrpService;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysDicGrpVo;

/**
 * @version 1.0
 * @description: TODO
 * @author: yn_zhang
 * @date 2021/5/31 16:33
 */
@Service
public class SysDicGrpServiceImpl extends BasicServiceImpl implements SysDicGrpService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    SysDicGrpDao sysDicGrpDao;
    @Autowired
    SysDicDao sysDicDao;

    @Override
    public void addSysDicGrp(SysDicGrpVo sysDicGrpVo) throws Exception {
        ValidateUtil.validate(sysDicGrpVo);
        SysDicGrpDo sysDicGrpDo = new SysDicGrpDo();
        sysDicGrpDo.setName(sysDicGrpVo.getName());
        sysDicGrpDo.setExclusive(sysDicGrpVo.isExclusive());
        sysDicGrpDo.setOrd(sysDicGrpVo.getOrd());
        Date current = new Date();
        sysDicGrpDo.setCreateTime(new Timestamp(current.getTime()));
        sysDicGrpDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDicGrpDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysDicGrpDao.saveAndFlush(sysDicGrpDo);
    }

    @Override
    public void deleteSysDicGrp(long dicGrpId) {
        //A.删除该字典分组下的字典
        List<SysDicDo> sysDicDos = sysDicDao.querySysDicByDicGrpId(dicGrpId,"");
        sysDicDao.deleteInBatch(sysDicDos);
        //B.删除该字典分组
        sysDicGrpDao.deleteById(dicGrpId);
    }

    @Override
    public void editSysDicGrp(SysDicGrpVo sysDicGrpVo) throws Exception {
        ValidateUtil.validate(sysDicGrpVo);
        SysDicGrpDo sysDicGrpDo = new SysDicGrpDo();
        sysDicGrpDo.setId(sysDicGrpVo.getId());
        sysDicGrpDo.setName(sysDicGrpVo.getName());
        sysDicGrpDo.setExclusive(sysDicGrpVo.isExclusive());
        sysDicGrpDo.setOrd(sysDicGrpVo.getOrd());
        Date current = new Date();
        sysDicGrpDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDicGrpDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysDicGrpDao.saveAndFlush(sysDicGrpDo);
    }

    @Override
    public List<SysDicGrpDo> querySysDicGrp() {
        List<SysDicGrpDo> sysDicGrpDos = sysDicGrpDao.findAll();
        return sysDicGrpDos;
    }
}
