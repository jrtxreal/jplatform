package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysPermitToRoleDao;
import com.dev.platform.data.dao.SysSrcDao;
import com.dev.platform.data.dao.SysSrcGrpDao;
import com.dev.platform.data.dao.SysSrcLinkSrcGrpDao;
import com.dev.platform.data.do_.SysSrcDo;
import com.dev.platform.data.do_.SysSrcGrpDo;
import com.dev.platform.service.SysSrcGrpService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.ValidateGroupType;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysSrcGrpVo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Service
public class SysSrcGrpServiceImpl extends BasicServiceImpl implements SysSrcGrpService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Resource
    SysSrcGrpDao sysSrcGrpDao;
    @Resource
    SysSrcLinkSrcGrpDao sysSrcLinkSrcGrpDao;
    @Resource
    SysPermitToRoleDao sysPermitToRoleDao;
    @Resource
    SysSrcDao sysSrcDao;

    @Override
    public void addSrcGrp(SysSrcGrpVo sysSrcGrpVo) throws Exception {
        ValidateUtil.validate(sysSrcGrpVo, ValidateGroupType.add);
        SysSrcGrpDo sysSrcGrpDo = new SysSrcGrpDo();
        Date current = new Date();
        sysSrcGrpDo.setCreateTime(new Timestamp(current.getTime()));
        sysSrcGrpDo.setLastUpdate(new Timestamp(current.getTime()));
        sysSrcGrpDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysSrcGrpDo.setName(sysSrcGrpVo.getName());
        sysSrcGrpDo.setOrd(sysSrcGrpVo.getOrd());
        sysSrcGrpDao.saveAndFlush(sysSrcGrpDo);
    }

    @Override
    public List<SysSrcGrpDo> queryAllSrcGrp() {
        Sort sort = Sort.by("ord");
        return sysSrcGrpDao.findAll(sort);
    }

    @Override
    public void editSrcGrp(SysSrcGrpVo sysSrcGrpVo) throws Exception {
        ValidateUtil.validate(sysSrcGrpVo,ValidateGroupType.edit);
        Optional<SysSrcGrpDo> optional = sysSrcGrpDao.findById(sysSrcGrpVo.getId());
        if(optional.isEmpty()){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"未找到该资源分组，或该资源分组已被删除");
        }
        SysSrcGrpDo sysSrcGrpDo = optional.get();
        Date current = new Date();
        sysSrcGrpDo.setLastUpdate(new Timestamp(current.getTime()));
        sysSrcGrpDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysSrcGrpDo.setName(sysSrcGrpVo.getName());
        sysSrcGrpDo.setOrd(sysSrcGrpVo.getOrd());
        sysSrcGrpDao.saveAndFlush(sysSrcGrpDo);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSrcGrp(long srcGrpId) throws Exception {
        sysSrcGrpDao.deleteById(srcGrpId);  // 删除资源分组A
        List<Long> srcIds = sysSrcDao.querySrcIdsOfSrcGrp(srcGrpId);
        List<SysSrcDo> sysSrcDoList = new ArrayList<>();
        srcIds.forEach(item -> {
            SysSrcDo sysSrcDo = new SysSrcDo();
            sysSrcDo.setId(item);
            sysSrcDoList.add(sysSrcDo);
        });
        sysSrcDao.deleteAll(sysSrcDoList);  // 删除资源分组A下的所有资源
        sysPermitToRoleDao.deletePermitToRoleBySrcIds(srcIds);  // 根据资源id集合删除角色权限
        sysSrcLinkSrcGrpDao.deleteSrcLinkSrcGrpBySrcGrpId(srcGrpId);  // 删除资源分组和资源的关系
    }
}
