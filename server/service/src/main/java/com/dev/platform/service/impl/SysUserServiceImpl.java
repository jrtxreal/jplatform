package com.dev.platform.service.impl;

import cn.hutool.json.JSONUtil;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.common.util.PwdUtil;
import com.dev.platform.common.util.Regexp;
import com.dev.platform.data.dao.*;
import com.dev.platform.data.do_.*;
import com.dev.platform.data.dto.DeptGroupUserDto;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.data.dto.SysUserDto3;
import com.dev.platform.service.SysSettingService;
import com.dev.platform.service.SysUserService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.UserSettingDef;
import com.dev.platform.service.def.ValidateGroupType;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysUserVo;

import java.sql.Timestamp;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Description:一句话描述这个类
 * @PackgeName: com.dev.platform.paperless.service.impl
 * @ClassName: SysUserServiceImpl
 * @Author: fanjunhui
 * @Date: 2021/05/31 16:26
 * @Version: V 1.0
 */

@Service
public class SysUserServiceImpl extends BasicServiceImpl implements SysUserService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Resource
    SysUserDao sysUserDao;
    @Resource
    SysUserLinkDeptDao sysUserLinkDeptDao;
    @Resource
    SysUserLinkRoleDao sysUserLinkRoleDao;
    @Resource
    SysUserSettingDao sysUserSettingDao;
    @Autowired
    SysSettingService sysSettingService;
    @Resource
    SysDeptDao sysDeptDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(SysUserVo sysUserVo) throws Exception {
        ValidateUtil.validate(sysUserVo, ValidateGroupType.add);
        SysUserDo sysUserDo = new SysUserDo();
        Date current = new Date();
        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
        SysGlobalSettingDo passwordSetting = null;
        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
            if (item.getKey().equals("password")) {
                passwordSetting = item;
            }
        }
        String detInfo = sysUserVo.getDetInfo();
        Map map = fromJson(Map.class, detInfo);
        if(map.get("phone")==null){
            throw new ExceptionCmpt(ExceptionCmpt.DEV,"手机号码不能为空");
        }
        String username = String.valueOf(map.get("phone"));
        if (!Pattern.compile(Regexp.REGEX_MOBILE).matcher(username).matches()) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV,"手机号码必须全为数字");
        }
        if(username.length() != 11){
            throw new ExceptionCmpt(ExceptionCmpt.DEV,"手机号码必须是11位");
        }
        String keySetJson = passwordSetting.getVal();
        checkPwdSec(username, sysUserVo.getPassword(), keySetJson);
        sysUserDo.setUsername(username);

        Example<SysUserDo> of = Example.of(sysUserDo);
        List<SysUserDo> all = sysUserDao.findAll(of);
        if(all.size() > 0){
            throw new ExceptionCmpt(ExceptionCmpt.DEV,"该账号已存在");
        }
        sysUserDo.setPassword(PwdUtil.encryptPassword(sysUserVo.getPassword()));
        sysUserDo.setCreateTime(new Timestamp(current.getTime()));
        sysUserDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserDo.setDisabled(false); //默认添加的用户为不禁用状态

        sysUserDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysUserDo.setDetInfo(sysUserVo.getDetInfo());
        sysUserDo.setDeptId(sysUserVo.getDeptId());


        SysUserDo sysUserDo1 = sysUserDao.saveAndFlush(sysUserDo);

        // 默认配置
        SysUserSettingDo settingDo = new SysUserSettingDo();
        settingDo.setUserId(sysUserDo.getId());
        settingDo.setKey(UserSettingDef.SORT_TYPE);
        settingDo.setVal(UserSettingDef.SORT_TYPE_ASC);
        settingDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserSettingDao.insertOrderSortType(sysUserDo.getId(), UserSettingDef.ORDER_TYPE, UserSettingDef.ORDER_TYPE_FILE_NAME, new Timestamp(current.getTime()));
        sysUserSettingDao.insertOrderSortType(sysUserDo.getId(), UserSettingDef.SORT_TYPE, UserSettingDef.SORT_TYPE_ASC, new Timestamp(current.getTime()));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(long userId) {
        sysUserDao.deleteById(userId);  // 删除用户表中的用户信息
        SysUserLinkRoleDo sysUserLinkRoleDo = new SysUserLinkRoleDo();
        sysUserLinkRoleDo.setUserId(userId);
        List<SysUserLinkRoleDo> userRoleList = sysUserLinkRoleDao.findAll(Example.of(sysUserLinkRoleDo));
        if (userRoleList.size() > 0) {
            sysUserLinkRoleDao.deleteInBatch(userRoleList);
        }
        SysUserSettingDo sysUserSettingDo = new SysUserSettingDo();
        sysUserSettingDo.setUserId(userId);
        List<SysUserSettingDo> userSettings = sysUserSettingDao.findAll(Example.of(sysUserSettingDo));
        if (userSettings.size() > 0) {
            sysUserSettingDao.deleteInBatch(userSettings);
        }
    }

    @Override
    public void editBasicUserInfo(SysUserVo sysUserVo) throws Exception {
        ValidateUtil.validate(sysUserVo, ValidateGroupType.edit);
        Optional<SysUserDo> sysUserDoOpt = sysUserDao.findById(sysUserVo.getId());
        if (sysUserDoOpt.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该用户不存在或已被删除");
        }

        SysUserDo sysUserDo = sysUserDoOpt.get();
//        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
//        SysGlobalSettingDo passwordSetting = null;
//        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
//            if (item.getKey().equals("password")) {
//                passwordSetting = item;
//            }
//        }
//        String keySetJson = passwordSetting.getVal();
//        checkPwdSec(sysUserDo.getUsername(), sysUserVo.getPassword(), keySetJson);
        Date current = new Date();
        sysUserDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysUserDo.setDisabled(sysUserVo.isDisabled());
        sysUserDo.setDeptId(sysUserVo.getDeptId());
        sysUserDo.setDetInfo(sysUserVo.getDetInfo());
        sysUserDao.saveAndFlush(sysUserDo);
    }

    @Override
    public void changeUserPwdByAdmin(long userId, String newUserPwd, String adminPwd) throws Exception {

        SysUserDo adminUser = sysUserDao.findById(SubjectUtil.getCurrentUserId(sessionCmpt)).get();
        if (!PwdUtil.PwdEq(adminPwd, adminUser.getPassword())) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "管理员密码错误");
        }
        Optional<SysUserDo> sysUserDoOpt = sysUserDao.findById(userId);
        if (sysUserDoOpt.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该用户不存在或已被删除");
        }
        SysUserDo sysUserDo = sysUserDoOpt.get();
        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
        SysGlobalSettingDo passwordSetting = null;
        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
            if (item.getKey().equals("password")) {
                passwordSetting = item;
            }
        }
        String keySetJson = passwordSetting.getVal();
        checkPwdSec(sysUserDo.getUsername(), newUserPwd, keySetJson);
        Date current = new Date();
        sysUserDo.setPassword(PwdUtil.encryptPassword(newUserPwd));
        sysUserDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysUserDao.saveAndFlush(sysUserDo);
    }

    @Override
    public void changeUserPwd(String newUserPwd, String oldPwd) throws Exception {
        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
        SysGlobalSettingDo passwordSetting = null;
        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
            if (item.getKey().equals("password")) {
                passwordSetting = item;
            }
        }
        String keySetJson = passwordSetting.getVal();
        SysUserDo sysUserDo = sysUserDao.findById(SubjectUtil.getCurrentUserId(sessionCmpt)).get();
        checkPwdSec(sysUserDo.getUsername(), newUserPwd, keySetJson);
        if (!PwdUtil.PwdEq(oldPwd, sysUserDo.getPassword())) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "原密码错误");
        }

        Date current = new Date();
        sysUserDo.setPassword(PwdUtil.encryptPassword(newUserPwd));
        sysUserDo.setLastUpdate(new Timestamp(current.getTime()));
        sysUserDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysUserDao.saveAndFlush(sysUserDo);
    }

    @Override
    public Page<SysUserDto> queryUserByDeptIdByPage(PageRequest pageRequest, long deptId, String username) throws Exception {
        if (StringUtils.isBlank(username)) {
            username = null;
        }
        List<SysDeptDto> list = sysDeptDao.queryParentChildByDeptId(deptId);
        Map<Long, String> map = new HashMap<>();
        for (SysDeptDto sysDeptdto : list) {
            map.put(sysDeptdto.getId(), sysDeptdto.getName());
        }
        Optional<SysDeptDo> optional = sysDeptDao.findById(deptId);
        if (optional.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "该部门不存在");
        }
        String basePath = optional.get().getPath();
        List<Long> exclusion = new ArrayList<>();
        exclusion.add(0L);
        String saStr = getSaStr();
        if(StringUtils.isNotBlank(saStr)){
            Arrays.asList(saStr.split("[,]")).forEach(item->{
                exclusion.add(Long.valueOf(item));
            });
        }
        Page<SysUserDto> sysUserDtos = sysUserDao.queryUserByDeptIdByPage(exclusion,basePath, username, pageRequest);
        for (SysUserDto sysUserDto : sysUserDtos.toList()) {
            String path = sysUserDto.getPath();
            sysUserDto.setFullPath((getFullPath(path, map)));
        }

        return sysUserDtos;
    }

    @Override
    public Page<SysUserDto> queryUserByPage(Pageable pageable, long deptId, String username) throws Exception {
        long adminDeptId = SubjectUtil.getCurrentUser(sessionCmpt).getDeptId();
        long userId = SubjectUtil.getCurrentUserId(sessionCmpt);

        //查询包含自己的父节点和子节点，放入map
        List<SysDeptDto> list = sysDeptDao.queryParentChildByDeptId(adminDeptId);
        Map<Long, String> map = new HashMap<>();
        for (SysDeptDto sysDeptdto : list) {
            map.put(sysDeptdto.getId(), sysDeptdto.getName());
        }

        SysDeptDo sysDeptDo = sysDeptDao.findById(adminDeptId).get();
        String adminPath = sysDeptDo.getPath();
        String path = "";
        if (deptId != 0) {
            path = sysDeptDao.findById(deptId).get().getPath();
        } else {
            path = adminPath;
        }
        if (StringUtils.isBlank(username)) {
            username = null;
        }
        if (checkSa(String.valueOf(userId))) {
            path = "/";
        }
        List<Long> exclusions = new ArrayList<>();
        exclusions.add(0L);
        String saStr = getSaStr();
        if(StringUtils.isNotBlank(saStr)){
            Arrays.asList(saStr.split("[,]")).forEach(item->{
                exclusions.add(Long.valueOf(item));
            });
        }
        Page<SysUserDto> sysUserDtos = sysUserDao.queryUserByPage(exclusions,pageable, path, username);

        for (SysUserDto sysUserDto : sysUserDtos.toList()) {
            try {
                String tmpPath = sysUserDto.getPath();
                sysUserDto.setCanDelete(true);
                //sysUserDto.setFullPath((getFullPath(tmpPath, map)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sysUserDtos;
    }

    @Override
    public SysUserDo queryUserById(long userId) throws Exception {
        SysUserDo sysUserDo = sysUserDao.findById(userId).get();
        return sysUserDo;
    }

    @Override
    public SysUserDo queryUserByPhone(String phone) throws Exception {
        List<SysUserDo> list = sysUserDao.queryUserByPhone(phone);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<SysUserDto3> queryUserByDeptId(long deptId, String condition) {
        SysDeptDo sysDeptDo = sysDeptDao.findById(deptId).get();
        String basePath = sysDeptDo.getPath();
        List<SysUserDto3> sysUserDtos = sysUserDao.queryUserByDeptId(basePath, condition);

        return sysUserDtos;
    }

    @Override
    public List<DeptGroupUserDto> searchDeptGroupUser(String condition) throws Exception {
        List<DeptGroupUserDto> list = new ArrayList<>();
        List<SysUserDto> sysUserDtos = sysUserDao.searchUser(condition);
        //List<SysDeptDto> sysDeptDtos = sysUserDao.searchDept(condition);
        long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        // 查询包含自己的父节点和子节点，放入map
        // List<SysDeptDto> deptTree = sysUserLinkDeptDao.queryAllParentDept(sysDeptDtos.stream().map(e -> String.valueOf(e.getId())).collect(Collectors.joining("|")));
        // Map<Long, String> map = new HashMap<>();
        // for (SysDeptDto sysDeptDo : deptTree) {
        //   map.put(sysDeptDo.getId(), sysDeptDo.getName());
        // }

        sysUserDtos.forEach(e -> {  //处理人员
            DeptGroupUserDto dto = new DeptGroupUserDto();
            dto.setType("user");
            Object nickName = JSONUtil.parseObj(e.getDetInfo()).get("nickName");
            if (nickName != null) {
                dto.setName(nickName.toString());
            }
            dto.setId(e.getId());
            dto.setUserName(e.getUsername());
            dto.setFullPath(e.getDeptName());
            list.add(dto);
        });
        return list;
    }

    public String getFullPath(String path, Map map) {
        String[] split = path.split("/");
        StringBuilder sp = new StringBuilder();
        for (String a : split) {
            if (!"".equals(a)) {
                sp.append("/");
                sp.append(map.get(Long.valueOf(a)));
            }
        }
        return sp.toString();
    }

    @Override
    public SysUserDo verifySMS(String mobile, String code) {
        List<SysUserDo> sysUserDos = sysUserDao.queryUserByPhone(mobile);
        if (sysUserDos == null || sysUserDos.size() != 1) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "验证失败，请找管理员重置密码");
        }
        SysUserDo userDo = sysUserDos.get(0);
        userDo.setPassword(null);
        return userDo;
    }

    @Override
    public void forgotPwd(String mobile, String newPwd) throws Exception {
        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
        SysGlobalSettingDo passwordSetting = null;
        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
            if (item.getKey().equals("password")) {
                passwordSetting = item;
            }
        }
        String keySetJson = passwordSetting.getVal();
        List<SysUserDo> sysUserDos = sysUserDao.queryUserByPhone(mobile);
        SysUserDo userDo = sysUserDos.get(0);
        checkPwdSec(userDo.getUsername(), newPwd, keySetJson);
        Date current = new Date();
        userDo.setPassword(PwdUtil.encryptPassword(newPwd));
        userDo.setLastUpdate(new Timestamp(current.getTime()));
        userDo.setUpdateBy(userDo.getUsername());
        sysUserDao.saveAndFlush(userDo);
    }

    @Override
    public void lockUser(String username) throws Exception {
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setUsername(username);

        ExampleMatcher em = ExampleMatcher.matching();
        Example example = Example.of(sysUserDo, em);
        Optional<SysUserDo> sysUserOpt = sysUserDao.findOne(example);
        if (!sysUserOpt.isEmpty()) {
            sysUserDo = sysUserOpt.get();
            sysUserDo.setDisabled(true);
            sysUserDao.saveAndFlush(sysUserDo);
        }
    }

    @Override
    public void editAvatar(String image) throws Exception {
        SysUserDo sysUserDo = sysUserDao.findById(SubjectUtil.getCurrentUserId(sessionCmpt)).get();
        String detInfo = sysUserDo.getDetInfo();
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotBlank(detInfo)) {
            map = JSONUtil.toBean(detInfo, Map.class);
            map.put("avatar", image);
        } else {
            map.put("avatar", image);
        }
        sysUserDo.setDetInfo(JSONUtil.toJsonStr(map));
        sysUserDao.saveAndFlush(sysUserDo);
    }
}


