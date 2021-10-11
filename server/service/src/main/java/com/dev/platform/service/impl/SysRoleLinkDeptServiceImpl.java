package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysRoleDao;
import com.dev.platform.data.dao.SysRoleLinkDeptDao;
import com.dev.platform.data.dao.SysUserLinkRoleDao;
import com.dev.platform.data.do_.SysRoleDo;
import com.dev.platform.data.do_.SysUserLinkRoleDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.service.SysRoleLinkDeptService;
import com.dev.platform.service.config.shiro.SessionCmpt;

import java.util.List;
import java.util.Optional;

@Service
public class SysRoleLinkDeptServiceImpl extends BasicServiceImpl implements SysRoleLinkDeptService {

    @Resource
    private SysRoleLinkDeptDao sysRoleLinkDeptDao;
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysUserLinkRoleDao sysUserLinkRoleDao;

    @Override
    public List<SysDeptDto> queryDeptForRole(long roleId) {
        return sysRoleLinkDeptDao.queryDeptForRole(roleId);
    }

    @Override
    public Page<SysRoleDto> queryRoleOfDept(PageRequest pageRequest, long deptId) {
        return sysRoleLinkDeptDao.queryRoleOfDept(pageRequest,deptId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleDept(long deptId, long roleId) {
        List<SysDeptDto> sysDeptDtos = sysRoleLinkDeptDao.queryDeptForRole(roleId);
        if(sysDeptDtos!=null&&sysDeptDtos.size()==1){
            Optional<SysRoleDo> optional = sysRoleDao.findById(roleId);
            if(optional.isPresent()){
                SysRoleDo sysRoleDo = optional.get();
                if(sysRoleDo.getDef()){
                    return;
                }
                SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
                cdt.setRoleId(roleId);
                List<SysUserLinkRoleDo> list = sysUserLinkRoleDao.findAll(Example.of(cdt));
                sysUserLinkRoleDao.deleteInBatch(list);
            }
        }
        sysRoleLinkDeptDao.deleteRoleDept(deptId,roleId);
    }
}
