package com.dev.platform.service.impl;

import cn.hutool.json.JSONUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysDeptDao;
import com.dev.platform.data.dao.SysRoleDao;
import com.dev.platform.data.dao.SysRoleLinkDeptDao;
import com.dev.platform.data.dao.SysUserDao;
import com.dev.platform.data.do_.SysDeptDo;
import com.dev.platform.data.do_.SysRoleDo;
import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysDeptDto2;
import com.dev.platform.data.dto.SysOrgUserDto;
import com.dev.platform.service.SysDeptService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.def.ValidateGroupType;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysDeptVo;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysDeptServiceImpl extends BasicServiceImpl implements SysDeptService {

    @Resource
    private SysDeptDao sysDeptDao;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private SysRoleDao sysRoleDao;
    @Resource
    private SysRoleLinkDeptDao sysRoleLinkDeptDao;
    @Autowired
    SessionCmpt sessionCmpt;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSysDept(SysDeptVo sysDeptVo) throws Exception {
        ValidateUtil.validate(sysDeptVo, ValidateGroupType.add);
        SysDeptDo sysDeptDo = voToDo(sysDeptVo);
        sysDeptDo.setId(null);
        Date current = new Date();
        sysDeptDo.setCreateTime(new Timestamp(current.getTime()));
        sysDeptDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDeptDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysDeptDao.saveAndFlush(sysDeptDo);
        if (sysDeptDo.getPid() == 0) {
            sysDeptDo.setPath("/" + sysDeptDo.getId() + "/");
        } else {
            Optional<SysDeptDo> sysDeptDoOpt = sysDeptDao.findById(sysDeptDo.getPid());
            if (sysDeptDoOpt.isEmpty()) {
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "无法为此节点添加子节点，原因是此节点不存在或已被删除");
            }
            SysDeptDo parentDept = sysDeptDoOpt.get();
            sysDeptDo.setPath(parentDept.getPath() + sysDeptDo.getId() + "/");
        }
        sysDeptDao.saveAndFlush(sysDeptDo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editSysDept(SysDeptVo sysDeptVo) throws Exception {
        ValidateUtil.validate(sysDeptVo, ValidateGroupType.edit);
        Optional<SysDeptDo> sysDeptDoOpt = sysDeptDao.findById(sysDeptVo.getId());
        if (sysDeptDoOpt.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该部门不存在或已被删除");
        }
        SysDeptDo sysDeptDo = sysDeptDoOpt.get();
        Date current = new Date();
//        private Long pid;
//        private String path;
        sysDeptDo.setName(sysDeptVo.getName());
        sysDeptDo.setOrd(sysDeptVo.getOrd());
        sysDeptDo.setDeptType(sysDeptVo.getDeptType());
        sysDeptDo.setLastUpdate(new Timestamp(current.getTime()));
        sysDeptDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        if (sysDeptDo.getPid().equals(sysDeptVo.getPid())) {
            // 父节点没有发生变化
            sysDeptDao.saveAndFlush(sysDeptDo);
        } else {
            // 父节点有变化
            sysDeptDo.setPid(sysDeptVo.getPid());
            SysDeptDo parent = null;
            String nPath = "";
            if (sysDeptVo.getPid() != 0) {
                Optional<SysDeptDo> parentOpt = sysDeptDao.findById(sysDeptVo.getPid());
                if (parentOpt.isEmpty()) {
                    throw new ExceptionCmpt(ExceptionCmpt.BIZ, "不能选择此部门作为上级部门，此部门可能已被删除");
                }
                parent = parentOpt.get();
                nPath = parent.getPath();
            } else {
                nPath = "/";
            }
            final String fnPath = nPath;
            // 更改当前节点的父节点
            // 更改节点path
            List<SysDeptDo> children = sysDeptDao.queySubDept(sysDeptDo.getId());
            children.add(sysDeptDo);
            children.forEach(item -> {
                String cPath = item.getPath();
                String r = cPath.replace("/" + sysDeptDo.getId() + "/", "@");
                String[] rSeg = r.split("@");
                if (rSeg.length == 0 || rSeg.length == 1) {
                    String path = fnPath + sysDeptDo.getId() + "/";
                    item.setPath(path);
                } else {
                    String path = fnPath + rSeg[1];
                    item.setPath(path);
                }
            });
            sysDeptDao.saveAll(children);
            sysDeptDao.flush();
        }
    }

    @Override
    public List<SysDeptDto> querySysDept(long deptId, Boolean spreadTag) {
        List<SysDeptDto> deptDtos = new ArrayList<>();
        deptDtos.add(sysDeptDao.querySysDeptById(deptId));
        if (spreadTag) {
            deptDtos.addAll(querySysDeptSpread(deptId));
        }
        return deptDtos;
    }

    @Override
    public SysDeptDto querySysDeptById(long deptId) {
        return sysDeptDao.querySysDeptById(deptId);
    }

    @Override
    public List<SysDeptDto2> queryAdminOrgOfUser() throws Exception {
        Long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        if (checkSa(String.valueOf(userId))) {
            return genFullPath(sysDeptDao.queryAllDept());
        }
        List<SysDeptDto2> adminList = sysDeptDao.queryAdminDeptOfUser(userId);
        // 先合并管理部门再查询关系才有用；
        adminList = combineDept(adminList);
        if (adminList.size() > 0) {
            List<Long> ids = new ArrayList<>();
            adminList.forEach(item -> {
                ids.add(item.getId());
            });
            List<SysDeptDto2> relatedDept = sysDeptDao.queryRelatedDept(ids);
            for (SysDeptDto2 item : relatedDept) {
                for (SysDeptDto2 subItem : adminList) {
                    if (subItem.getId().equals(item.getId())) {
                        item.setAdminTag(1);
                    }
                }
            }
            List<SysDeptDto2> list = genFullPath(relatedDept);
            list = list.stream().filter(item ->
                    item.getAdminTag() < 3
            ).collect(Collectors.toList());
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<SysDeptDto2> queryAdminOrgOfUser1(long userId) throws Exception {
        if (checkSa(String.valueOf(userId))) {
            return genFullPath(sysDeptDao.queryAllDept());
        }
        List<SysDeptDto2> adminList = sysDeptDao.queryAdminDeptOfUser(userId);
        // 先合并管理部门再查询关系才有用；
        adminList = combineDept(adminList);
        if (adminList.size() > 0) {
            List<Long> ids = new ArrayList<>();
            adminList.forEach(item -> {
                ids.add(item.getId());
            });
            List<SysDeptDto2> relatedDept = sysDeptDao.queryRelatedDept(ids);
            for (SysDeptDto2 item : relatedDept) {
                for (SysDeptDto2 subItem : adminList) {
                    if (subItem.getId().equals(item.getId())) {
                        item.setAdminTag(1);
                    }
                }
            }
            List<SysDeptDto2> list = genFullPath(relatedDept);
            list = list.stream().filter(item ->
                    item.getAdminTag() < 3
            ).collect(Collectors.toList());
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<SysDeptDto2> queryDeptOfUser() throws Exception {
        Long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        if (checkSa(String.valueOf(userId))) {
            return genFullPath(sysDeptDao.queryAllDept());
        }
        Optional<SysUserDo> userDoOpt = sysUserDao.findById(userId);
        if (userDoOpt.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.AUC, "");
        }
        SysUserDo sysUserDo = userDoOpt.get();
        List<SysDeptDto2> list = queryAdminOrgOfUser();
        List<Long> ids = new ArrayList<>();
        ids.add(sysUserDo.getDeptId());
        List<SysDeptDto2> bLDeptList = sysDeptDao.queryRelatedDept(ids);
        SysDeptDto2 blDto2 = null;
        for (SysDeptDto2 item : bLDeptList) {
            if (item.getId().equals(sysUserDo.getDeptId())) {
                blDto2 = item;
                blDto2.setAdminTag(4); // 临时标记，表示此条记录是用户的所属部门；
            } else {
                item.setAdminTag(3); // 设置为不可管理状态
            }
        }
        List<SysDeptDto2> adminDeptList = new ArrayList<>();
        list.forEach(item -> {
            if (item.getAdminTag().equals(1)) {
                adminDeptList.add(item);
            }
        });
        List<SysDeptDto2> combineList = new ArrayList<>();
        combineList.addAll(adminDeptList);
        combineList.add(blDto2);
        List<SysDeptDto2> cmbResult = combineDept(combineList);
        Boolean blDeptBeCombined = true;
        for (SysDeptDto2 item : cmbResult) {
            if (item.getId().equals(blDto2.getId())) {
                blDeptBeCombined = false;
            }
        }
        if (blDeptBeCombined) {
            return list;
        } else {
            // 发现所属部门没有被管理部门合并，赶紧的设置为不可管理
            blDto2.setAdminTag(3);
            List<SysDeptDto2> result = new ArrayList<>();
            Map<Long, SysDeptDto2> distinctResult = new HashMap<>();
            for (SysDeptDto2 item : cmbResult) {
                // 先放所属部门的子节点，以便当某个管理部门被合并了还能覆盖一下子
                for (SysDeptDto2 subItem : bLDeptList) {
                    if (subItem.getPath().contains(item.getPath())) {
                        distinctResult.put(subItem.getId(), subItem);
                    }
                }
                // 这里就覆盖一下子
                for (SysDeptDto2 subItem : list) {
                    if (subItem.getPath().contains(item.getPath())) {
                        distinctResult.put(subItem.getId(), subItem);
                    }
                }
            }
            Iterator<Map.Entry<Long, SysDeptDto2>> it = distinctResult.entrySet().iterator();
            while (it.hasNext()) {
                result.add(it.next().getValue());
            }
            return genFullPath(result);
        }
    }


    private List<SysDeptDto> querySysDeptSpread(long deptId) {
        List<SysDeptDto> deptDtos = sysDeptDao.querySysDeptByPid(deptId);
        deptDtos.forEach(dto -> {
            if (!dto.getLeaf()) {
                deptDtos.addAll(querySysDeptSpread(dto.getId()));
            }
        });
        return deptDtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeSysOrgPath(long currentDeptId, long targetDeptId) throws Exception {
        List<SysDeptDo> subDepts = sysDeptDao.findSysDeptDoByPid(currentDeptId);
        List<Long> subDeptIds = subDepts.stream().map(SysDeptDo::getId).collect(Collectors.toList());
        if (subDeptIds.contains(targetDeptId)) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "不能移动到部门");
        }
        String username =SubjectUtil.getCurrentUser(sessionCmpt).getUsername();
        Date current = new Date();
        SysDeptDo currentDept = sysDeptDao.getOne(currentDeptId);
        String path = "";
        if (targetDeptId != 0L) { //移动到顶级目录
            SysDeptDo targetDept = sysDeptDao.getOne(targetDeptId);
            path = targetDept.getPath();
        }
        if (currentDept.getPid() != 0L) { //修改原父部门
            Integer subDeptNums = sysDeptDao.findSysDeptDoByPid(currentDept.getPid()).size();
            if (subDeptNums.equals(1)) {
                SysDeptDo parentDept = sysDeptDao.getOne(currentDept.getPid());
                sysDeptDao.saveAndFlush(parentDept);
            }
        }

        currentDept.setPid(targetDeptId);
        currentDept.setPath(path + "/" + currentDept.getId());
        currentDept.setUpdateBy(username);
        currentDept.setLastUpdate(new Timestamp(current.getTime()));
        sysDeptDao.saveAndFlush(currentDept);

        //更新当前部门的所以子部门
        subDepts.forEach(e -> {
            e.setPath(currentDept.getPath() + "/" + e.getId());
        });
        sysDeptDao.saveAll(subDepts);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSysDept(long deptId) {
        Optional<SysDeptDo> op = sysDeptDao.findById(deptId);
        if (!op.isPresent()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "部门不存在");
        }
        List<SysUserDo> sysUserDos = sysUserDao.queryUserOfDept(deptId);
        if (sysUserDos != null && sysUserDos.size() > 0) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该部门或其子部门下存在用户,无法删除");
        }
        List<SysRoleDo> roleDos = sysRoleDao.querySysRoleByDeptId(deptId);
        List<SysDeptDto> sysDeptDtos = sysDeptDao.querySysDeptByPid(deptId);

        // 删除部门以及子部门
        sysDeptDao.deleteById(deptId);
        sysDeptDtos.forEach(e -> {
            sysDeptDao.deleteById(e.getId());
        });
        // 删除该部门下的角色
        roleDos.forEach(role -> {
            sysRoleDao.deleteById(role.getId());
            sysRoleLinkDeptDao.deleteSysRoleLinkDept(role.getId());
        });
        //删除控制该部门对应的角色部门关系
        sysRoleLinkDeptDao.deleteSysRoleLinkDeptByDeptId(deptId);
    }

    @Override
    public List<SysDeptDto> queryOrgList(String condition) {
        List<SysDeptDto> sysDeptDtos = sysDeptDao.queryOrgList(condition);
        List<String> pahtList = sysDeptDtos.stream().map(SysDeptDto::getPath).collect(Collectors.toList());
        sysDeptDtos = sysDeptDtos.stream().filter(e -> {
            pahtList.forEach(path -> {
                if (!path.equals(e.getPath()) && e.getPath().startsWith(path)) {
                    e.setRemove(true);
                    return;
                }
            });
            if (e.getRemove()) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return sysDeptDtos;
    }

    private SysDeptDo voToDo(SysDeptVo sysDeptVo) {
        SysDeptDo sysDeptDo = new SysDeptDo();
        sysDeptDo.setId(sysDeptVo.getId());
        sysDeptDo.setName(sysDeptVo.getName());
        sysDeptDo.setDeptType(sysDeptVo.getDeptType());
        sysDeptDo.setPid(sysDeptVo.getPid());
        sysDeptDo.setOrd(sysDeptVo.getOrd());
        return sysDeptDo;
    }

    private List<SysDeptDto2> combineDept(List<SysDeptDto2> list) {
        List<Map> list2 = new ArrayList<>();
        list.forEach(item -> {
            Map map = new HashMap();
            map.put("path", item.getPath());
            map.put("remove", false);
            map.put("deptDto", item);
            list2.add(map);
        });
        if (list2.size() > 1) {
            for (int i = 0; i < list2.size() - 1; i++) {
                Map mi = list2.get(i);
                if (mi.get("remove").equals(false)) {
                    for (int j = i + 1; j < list2.size(); j++) {
                        Map mj = list2.get(j);
                        if (mj.get("remove").equals(false)) {
                            if (mi.get("path").equals(mj.get("path"))) {
                                mi.put("remove", true);
                            } else if (String.valueOf(mi.get("path")).contains(String.valueOf(mj.get("path")))) {
                                mi.put("remove", true);
                            } else if (String.valueOf(mj.get("path")).contains(String.valueOf(mi.get("path")))) {
                                mj.put("remove", true);
                            }
                        }
                    }
                }
            }
        } else if (list.size() == 1) {
            return list;
        }
        List<SysDeptDto2> result = new ArrayList<>();
        list2.forEach(item -> {
            if (!item.get("remove").equals(true)) {
                result.add((SysDeptDto2) item.get("deptDto"));
            }
        });
        return result;
    }

    private List<SysDeptDto2> genFullPath(List<SysDeptDto2> list) {
        Map map = new HashMap();
        list.forEach(item -> {
            map.put(item.getId(), item.getName());
        });
        list.forEach(item -> {
            String r;
            r = (r = item.getPath()).substring(1, r.length() - 1);
            String[] rSeg = r.split("/");
            StringBuffer buffer = new StringBuffer("/");
            for (String i : rSeg) {
                buffer.append(map.get(Long.valueOf(i))).append("/");
            }
            item.setFullPath(buffer.toString());
        });
        return list;
    }

    @Override
    public List<SysOrgUserDto> queryDeptAndUserList(String pid) {
        List<SysOrgUserDto> list = new ArrayList<>();
        Long deptId;
        if (StringUtils.isBlank(pid)) {
            deptId=0L;
        } else {
            try {
                deptId = Long.valueOf(pid);
            } catch (NumberFormatException e) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
            }
        }

        List<SysDeptDto> sysDeptDtos = sysDeptDao.querySysDeptByPid(deptId);
        List<SysUserDo> sysUserDos = sysUserDao.querySysUserDoByDeptId(deptId);
        List<SysOrgUserDto> deptList = sysDeptDtos.stream().map(e -> {
            SysOrgUserDto sysOrgUserDto = new SysOrgUserDto();
            sysOrgUserDto.setDept(true);
            sysOrgUserDto.setId(e.getId());
            sysOrgUserDto.setName(e.getName());
            return sysOrgUserDto;
        }).collect(Collectors.toList());
        list.addAll(deptList);

        List<SysOrgUserDto> userList = sysUserDos.stream().map(e -> {
            SysOrgUserDto sysOrgUserDto = new SysOrgUserDto();
            sysOrgUserDto.setDept(false);
            sysOrgUserDto.setId(e.getId());
            Object nickName = JSONUtil.parseObj(e.getDetInfo()).get("nickName");
            if (nickName != null) {
                sysOrgUserDto.setName(nickName.toString());
            }
            sysOrgUserDto.setUserName(e.getUsername());
            return sysOrgUserDto;
        }).collect(Collectors.toList());
        list.addAll(userList);
        return list;
    }
}
