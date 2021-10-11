package com.dev.platform.service.impl;

import com.dev.platform.service.pojo.BasicUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.common.util.UpdateUtil;
import com.dev.platform.data.dao.SysRoleDao;
import com.dev.platform.data.dao.SysRoleLinkDeptDao;
import com.dev.platform.data.dao.SysUserLinkRoleDao;
import com.dev.platform.data.do_.SysRoleDo;
import com.dev.platform.data.do_.SysRoleLinkDeptDo;
import com.dev.platform.data.do_.SysUserLinkRoleDo;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.service.SysRoleService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.ValidateGroupType;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysRoleVo;

import java.sql.Timestamp;
import java.util.*;

@Service
public class SysRoleServiceImpl extends BasicServiceImpl implements SysRoleService {

    @Resource
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRoleLinkDeptDao sysRoleLinkDeptDao;
    @Autowired
    private SysUserLinkRoleDao sysUserLinkRoleDao;
    @Autowired
    SessionCmpt sessionCmpt;



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(SysRoleVo sysRoleVo, String deptIds) throws Exception {
        if(StringUtil.isBlank(deptIds)){
           throw new ExceptionCmpt(ExceptionCmpt.BIZ,"请选择部门");
        }
        ValidateUtil.validate(sysRoleVo);
        String[] deptIdsSeg = deptIds.split("[,]");
        BasicUserInfo currentUser = SubjectUtil.getCurrentUser(sessionCmpt);
        Arrays.asList(deptIdsSeg).forEach(item->{
            SysRoleDo sysRoleDo = voToDo(sysRoleVo);
            sysRoleDo.setId(null);
            Date current = new Date();
            sysRoleDo.setLastUpdate(new Timestamp(current.getTime()));
            sysRoleDo.setCreateTime(new Timestamp(current.getTime()));
            sysRoleDo.setUpdateBy(currentUser.getUsername());
            sysRoleDo.setDeptId(currentUser.getDeptId());
            sysRoleDao.saveAndFlush(sysRoleDo);
            this.assignOrgToRole(sysRoleDo.getId(),item);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(long roleId) {
        sysRoleDao.deleteById(roleId);
        SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
        cdt.setRoleId(roleId);
        List<SysUserLinkRoleDo> list = sysUserLinkRoleDao.findAll(Example.of(cdt));
        sysUserLinkRoleDao.deleteInBatch(list);
        deleteRoleLinkDept(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRole(SysRoleVo sysRoleVo, String deptIds) throws Exception {
        ValidateUtil.validate(sysRoleVo, ValidateGroupType.edit);
        SysRoleDo sysRoleDo = voToDo(sysRoleVo);
        Date current = new Date();
        sysRoleDo.setLastUpdate(new Timestamp(current.getTime()));
        sysRoleDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        SysRoleDo oldSysRoleDo = sysRoleDao.findById(sysRoleDo.getId()).get();
        UpdateUtil.copyNullProperties(oldSysRoleDo,sysRoleDo);
        sysRoleDao.saveAndFlush(sysRoleDo);
        if(StringUtils.isNotBlank(deptIds)){
            this.deleteRoleLinkDept(sysRoleVo.getId());
            this.assignOrgToRole(sysRoleDo.getId(),deptIds);
        }
    }

    @Override
    public Page<SysRoleDto> queryRoleByDeptId(PageRequest pageRequest, long deptId,String condition) {
        return sysRoleDao.querySysRoleByDeptId(pageRequest,deptId,condition);
    }

    @Override
    public SysRoleDto queryRoleById(long id) {
        return sysRoleDao.queryRoleById(id);
    }

    @Override
    public void assignOrgToRole(long roleId, String deptIds) {
        List<SysRoleLinkDeptDo> list=new ArrayList<>();
        Arrays.stream(deptIds.split(",")).forEach(deptId->{
            SysRoleLinkDeptDo sysRoleLinkDeptDo=new SysRoleLinkDeptDo();
            sysRoleLinkDeptDo.setRoleId(roleId);
            sysRoleLinkDeptDo.setDeptId(Long.valueOf(deptId));
            list.add(sysRoleLinkDeptDo);
        });
        sysRoleLinkDeptDao.saveAll(list);
    }

    @Override
    public void deleteRoleLinkDept(long roleId) {
        sysRoleLinkDeptDao.deleteSysRoleLinkDept(roleId);
    }

    @Override
    public void disableRole(long roleId) {
        Optional<SysRoleDo> optional = sysRoleDao.findById(roleId);
        if(!optional.isPresent()){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"角色不存在");
        }
        SysRoleDo roleDo=  optional.get();
        if(roleDo.getDef()){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"默认角色,无法禁用");
        }
        roleDo.setDisabled(true);
        sysRoleDao.saveAndFlush(roleDo);
    }

    private SysRoleDo voToDo(SysRoleVo sysRoleVo){
        SysRoleDo sysRoleDo=new SysRoleDo();
        sysRoleDo.setId(sysRoleVo.getId());
        sysRoleDo.setName(sysRoleVo.getName());
        sysRoleDo.setDeptId(sysRoleVo.getDeptId());
        sysRoleDo.setDef(sysRoleVo.getDef());
        sysRoleDo.setDisabled(sysRoleVo.getDisabled());
        sysRoleDo.setOrd(sysRoleVo.getOrd());
        sysRoleDo.setCreateTime(sysRoleVo.getCreateTime());
        return sysRoleDo;
    }
}
