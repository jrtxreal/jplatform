package com.dev.platform.service.impl;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import com.dev.platform.common.util.PwdUtil;
import com.dev.platform.data.dao.SysUserDao;
import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.service.PersonalService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.vo.PersonalInfoVo;

/**
 * @Classname PersonalServiceImpl
 * @Description TODO
 * @Date 2021/6/15 9:12
 * @Author Mr.zsf
 */
@Service
public class PersonalServiceImpl  extends BasicServiceImpl implements PersonalService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    SessionCmpt sessionCmpt;
    @Override
    public PersonalInfoVo findPersonalInfoById(long userId) {
        SysUserDto sysUserDto = sysUserDao.queryUserByUserId(userId);
        PersonalInfoVo personalInfoVo=new PersonalInfoVo();
        personalInfoVo.setId(sysUserDto.getId());
        personalInfoVo.setUsername(sysUserDto.getUsername());
        personalInfoVo.setDeptId(sysUserDto.getDeptId());
        personalInfoVo.setDeptName(sysUserDto.getDeptName());
        return personalInfoVo;
    }

    @Override
    public void editPassword(long userId, String oldPassword, String newPassword) {
        Optional<SysUserDo> optional = sysUserDao.findById(userId);
        if(!optional.isPresent()){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"用户不存在");
        }
        SysUserDo sysUserDo = optional.get();
        if (!PwdUtil.PwdEq(oldPassword, sysUserDo.getPassword())) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"原密码错误");
        }
        sysUserDo.setPassword(PwdUtil.encryptPassword(newPassword));
        sysUserDao.save(sysUserDo);
    }

    @Override
    public void logout() {
        Object principal = SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        sessionCmpt.invalidate(String.valueOf(principal));
    }
}
