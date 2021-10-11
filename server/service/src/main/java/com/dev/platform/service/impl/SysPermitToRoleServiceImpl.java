package com.dev.platform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysPermitToRoleDao;
import com.dev.platform.data.do_.SysPermitToRoleDo;
import com.dev.platform.data.dto.SysSrcDto;
import com.dev.platform.service.SysPermitToRoleService;

import java.util.ArrayList;
import java.util.List;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Service
public class SysPermitToRoleServiceImpl extends BasicServiceImpl implements SysPermitToRoleService {
    @Resource
    SysPermitToRoleDao sysPermitToRoleDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantPrivilegesToRole(List<Long> srcIds, List<Long> roleIds) {
        List<SysPermitToRoleDo> sysPermitToRoleDoList = new ArrayList<>();
        roleIds.forEach(item->{
            sysPermitToRoleDao.deletePermitToRoleByRoleId(item);
            srcIds.forEach(sItem->{
                SysPermitToRoleDo sysPermitToRoleDo = new SysPermitToRoleDo();
                sysPermitToRoleDo.setSrcId(sItem);
                sysPermitToRoleDo.setRoleId(item);
                sysPermitToRoleDoList.add(sysPermitToRoleDo);
            });
        });
        sysPermitToRoleDao.saveAll(sysPermitToRoleDoList);
        sysPermitToRoleDao.flush();

    }

    @Override
    public List<SysSrcDto> querySrcOfRole(Long roleId) {
        List<SysSrcDto> list = sysPermitToRoleDao.querySrcOfRole(roleId);
        list.forEach(e->{
            e.setCheckTag(1);
        });
        return list;
    }
}
