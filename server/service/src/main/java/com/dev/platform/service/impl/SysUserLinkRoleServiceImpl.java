package com.dev.platform.service.impl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysDeptDao;
import com.dev.platform.data.dao.SysRoleDao;
import com.dev.platform.data.dao.SysUserLinkDeptDao;
import com.dev.platform.data.dao.SysUserLinkRoleDao;
import com.dev.platform.data.do_.SysDeptDo;
import com.dev.platform.data.do_.SysUserLinkRoleDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.service.SysUserLinkRoleService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;

import java.util.*;

/**
 * @Description:一句话描述这个类
 * @PackgeName: com.dev.platform.paperless.service.impl
 * @ClassName: SysUserLinkRoleServiceImpl
 * @Author: fanjunhui
 * @Date: 2021/06/02 21:49
 * @Version: V 1.0
 */

@Service
public class SysUserLinkRoleServiceImpl extends BasicServiceImpl implements SysUserLinkRoleService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Resource
    SysUserLinkRoleDao sysUserLinkRoleDao;
    @Resource
    SysDeptDao sysDeptDao;
    @Resource
    SysUserLinkDeptDao sysUserLinkDeptDao;
    @Resource
    SysRoleDao sysRoleDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoleToUser(long userId, String roleIds) {
        SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
        cdt.setUserId(userId);
        List<SysUserLinkRoleDo> deleteList = sysUserLinkRoleDao.findAll(Example.of(cdt));
        sysUserLinkRoleDao.deleteInBatch(deleteList);

        List<SysUserLinkRoleDo> addList = new ArrayList<>();

        if(StringUtils.isNotBlank(roleIds)){

            Arrays.asList(roleIds.split("[,]")).forEach(item->{
                SysUserLinkRoleDo sysUserLinkRoleDo = new SysUserLinkRoleDo();
                sysUserLinkRoleDo.setUserId(userId);
                long roleId =0L;

                try {
                    roleId = Long.valueOf(item);
                } catch (NumberFormatException e) {
                    throw new ExceptionCmpt(ExceptionCmpt.BIZ, "角色参数列表中出现非法字符");
                }

                sysUserLinkRoleDo.setRoleId(roleId);
                addList.add(sysUserLinkRoleDo);
                });
        }
        sysUserLinkRoleDao.saveAll(addList);
        sysUserLinkRoleDao.flush();

//        if(roleIds == null || "".equals(roleIds) || roleIds.length() == 0){
//            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "角色参数列表为空");
//        }
//        String[] split = roleIds.split(",");
//        List<SysUserLinkRoleDo> list = new ArrayList<>();
//        for (String s : split) {
//            SysUserLinkRoleDo sysUserLinkRoleDo = new SysUserLinkRoleDo();
//            sysUserLinkRoleDo.setUserId(userId);
//            Long roleId = 0L;
//            try {
//                roleId = Long.valueOf(s);
//            } catch (NumberFormatException e) {
//                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "角色参数列表中出现非法字符");
//            }
//            sysUserLinkRoleDo.setRoleId(roleId);
//            list.add(sysUserLinkRoleDo);
//        }
//        List<SysRoleDto> sysRoleDtos = sysUserLinkRoleDao.queryRoleOfUser(userId);
//        Iterator<SysUserLinkRoleDo> iterator = list.iterator();
//        List<SysUserLinkRoleDo> tmpList = new ArrayList<>();
//        while (iterator.hasNext()){
//            SysUserLinkRoleDo item = iterator.next();
//            boolean flg = false;
//            for(SysRoleDto sysRoleDto:sysRoleDtos){
//                if(sysRoleDto.getUserId().equals(item.getUserId()) && sysRoleDto.getRoleId().equals(item.getRoleId())){
//                    flg=true;
//                    break;
//                }
//            }
//            if(!flg){
//                tmpList.add(item);
//            }
//        }
//        if(!tmpList.isEmpty()){
//            sysUserLinkRoleDao.saveAll(tmpList);
//            sysUserLinkRoleDao.flush();
//        }else {
//            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"该用户已拥有此角色");
//        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserToRole(long roleId, String userIds) {
        String[] split = userIds.split(",");
        SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
        cdt.setRoleId(roleId);
        List<SysUserLinkRoleDo> delList = sysUserLinkRoleDao.findAll(Example.of(cdt));
        sysUserLinkRoleDao.deleteInBatch(delList);
        if(StringUtils.isBlank(userIds)){
            return;
        }
        List<SysUserLinkRoleDo> list = new ArrayList<>();
        for (String s : split) {
            SysUserLinkRoleDo sysUserLinkRoleDo = new SysUserLinkRoleDo();
            sysUserLinkRoleDo.setRoleId(roleId);

            Long userId = 0L;
            try {
                userId = Long.valueOf(s);
            } catch (NumberFormatException e) {
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "角色参数列表中出现非法字符");
            }
            sysUserLinkRoleDo.setUserId(userId);
            list.add(sysUserLinkRoleDo);
        }
        sysUserLinkRoleDao.saveAll(list);
        sysUserLinkRoleDao.flush();
    }

    @Override
    public void deleteUserLinkRoleByRoleId(long roleId) {
        sysUserLinkRoleDao.deleteSysUserLinkRoleByRoleId(roleId);
    }

    @Override
    public void deleteUserLinkRoleByUserId(long userId) {
        SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
        cdt.setUserId(userId);
        List<SysUserLinkRoleDo> list = sysUserLinkRoleDao.findAll(Example.of(cdt));
        if (list.size() > 0) {
            sysUserLinkRoleDao.deleteInBatch(list);
        }
    }

    @Override
    public void deleteUserLinkRoleByUserIdAndRoleId(long userId, long roleId) {
        SysUserLinkRoleDo cdt = new SysUserLinkRoleDo();
        cdt.setRoleId(roleId);
        cdt.setUserId(userId);
        List<SysUserLinkRoleDo> list = sysUserLinkRoleDao.findAll(Example.of(cdt));
        if (list.size() > 0) {
            sysUserLinkRoleDao.deleteInBatch(list);
        }

    }

    //<查询用户已有角色，带当前用户可操作标记>
    @Override
    public List<SysRoleDto> queryRoleOfUser(long userId) throws Exception {
        long adminId = SubjectUtil.getCurrentUser(sessionCmpt).getId();
        //查询路径映射Map
        List<SysDeptDo> all = sysDeptDao.findAll();
        Map<Long,String> map = new HashMap<>();
        for(SysDeptDo sysDeptDo :all){
            map.put(sysDeptDo.getId(),sysDeptDo.getName());
        }
        //查询该管理员能够管理的部门
        List<SysDeptDto> sysDeptDtos =null;
        if(checkSa(String.valueOf(adminId))){
            sysDeptDtos = sysDeptDao.queryAllDept1();
        }else {
            sysDeptDtos = sysUserLinkDeptDao.queryUserLinkDept(adminId);
        }


        //查询用户已有角色
        List<SysRoleDto> sysRoleDtos = sysUserLinkRoleDao.queryRoleOfUser(userId);
        for(SysRoleDto sysRoleDto : sysRoleDtos){
            String path = sysRoleDto.getPath();
            sysRoleDto.setCanDelete(false);
            for(SysDeptDto sysDeptDto : sysDeptDtos){
                if(path.contains(sysDeptDto.getPath())){
                    sysRoleDto.setCanDelete(true);
                    break;
                }
            }
            sysRoleDto.setFullPath(getFullPath(path,map));
        }

        return sysRoleDtos;
    }

    //<查询角色下的用户，带当前用户可操作标记>
    @Override
    public List<SysUserDto> queryUserOfRole(long roleId) throws Exception {
        List<SysUserDto> list = sysUserLinkRoleDao.queryUserOfRole(roleId);
        List<SysDeptDo> all = sysDeptDao.findAll();
        Map<Long,String> map = new HashMap<>();
        for(SysDeptDo sysDeptDo :all){
            map.put(sysDeptDo.getId(),sysDeptDo.getName());
        }

        //查询管理员能管理的部门，从而查询出能够管理的用户
        long adminId = SubjectUtil.getCurrentUser(sessionCmpt).getId();
        boolean canDelete=false;
        if(checkSa(String.valueOf(adminId))){
            canDelete=true;
        }

        //查询该管理员能够管理的部门
        List<SysDeptDto> sysDeptDtos = sysUserLinkDeptDao.queryUserLinkDept(adminId);

        for (SysUserDto sysUserDto : list){
            String path = sysUserDto.getPath();
            sysUserDto.setCanDelete(canDelete);
            for(SysDeptDto sysDeptDto : sysDeptDtos){
                if(path.contains(sysDeptDto.getPath())){
                    sysUserDto.setCanDelete(true);
                    break;
                }
            }

            sysUserDto.setFullPath(getFullPath(path,map));
        }
        return list;
    }

    @Override
    public List<SysRoleDto> queryRoleForCurrentUser() throws Exception {
        long adminId = SubjectUtil.getCurrentUser(sessionCmpt).getId();

        List<SysDeptDo> all = sysDeptDao.findAll();
        Map<Long,String> map = new HashMap<>();
        for(SysDeptDo sysDeptDo :all){
            map.put(sysDeptDo.getId(),sysDeptDo.getName());
        }

        if (checkSa(String.valueOf(adminId))) {
            List<SysRoleDto> list = sysRoleDao.queryAllRole();
            list.forEach(item->{
                item.setFullPath(getFullPath(item.getPath(),map));
            });
            return list;
        }
        List<SysRoleDto> sysRoleDtos = sysUserLinkRoleDao.queryRoleForCurrentUser(adminId);

        for (SysRoleDto sysRoleDto : sysRoleDtos){
            sysRoleDto.setFullPath(getFullPath(sysRoleDto.getPath(),map));
        }

        return sysRoleDtos;
    }

    public String getFullPath(String path,Map map){
        String[] split = path.split("/");
        StringBuilder sp = new StringBuilder();
        for (String a : split){
            if(!"".equals(a)) {
                sp.append("/");
                sp.append(map.get(Long.valueOf(a)));
            }
        }
        return sp.toString();
    }
}
