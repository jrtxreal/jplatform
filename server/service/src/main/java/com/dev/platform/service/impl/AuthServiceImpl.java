package com.dev.platform.service.impl;
import com.dev.platform.service.pojo.BasicUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.dev.platform.common.util.AESUtil;
import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.common.util.PwdUtil;
import com.dev.platform.common.util.UUIDUtil;
import com.dev.platform.data.dao.*;
import com.dev.platform.data.do_.SysDeptDo;
import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysSrcDto;
import com.dev.platform.service.AuthService;
import com.dev.platform.service.bo.SessionBo;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.LCmpt;
import com.dev.platform.service.config.shiro.*;
import com.dev.platform.service.def.AuthDef;
import com.dev.platform.service.def.LoginTypeDef;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Service
public class AuthServiceImpl extends BasicServiceImpl implements AuthService {
    @Resource
    SysUserDao sysUserDao;
    @Autowired
    Environment env;
    @Autowired
    SessionCmpt sessionCmpt;
    @Resource
    SysUserLinkDeptDao sysUserLinkDeptDao;
    @Resource
    SysUserLinkRoleDao sysUserLinkRoleDao;
    @Resource
    SysDeptDao sysDeptDao;
    @Resource
    SysSrcDao sysSrcDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public LoginTokenCmpt createLoginTokenCmpt(Map<String, Object> realParams) throws Exception {
        String username = String.valueOf(realParams.get("username"));
        if (isInvalidStr(username)) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "用户名不能为空");
        }
        String password = String.valueOf(realParams.get("password"));
        if (isInvalidStr(password)) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "密码不能为空");
        }
        String loginType = String.valueOf(realParams.get("loginType"));
        if (isInvalidStr(loginType)) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "请设置登录类型");
        }
        String deviceType = String.valueOf(realParams.get("deviceType"));
        if (isInvalidStr(deviceType)) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "请设置设备类型");
        }

        String principal = AESUtil.byteToHexString(AESUtil.encrypt(username, AuthDef.key16.getBytes("utf-8"),
                AuthDef.iv_16));
        principal += "_" + UUIDUtil.getUUIDWithDelimiter();
        return new LoginTokenCmpt(principal, password, loginType, deviceType);
    }

    @Override
    public AuthenticationInfo handleLogin(LoginTokenCmpt loginToken) throws Exception {
        String principal = (String) loginToken.getPrincipal();
        // license验证
        String[] principalSegments = principal.split("_");
        Set<String> logins = sessionCmpt.getLoginUsers();
        LCmpt lCmpt = new LCmpt();
        lCmpt.analyse(env.getProperty("com.dev.platform.registry.license"));
        lCmpt.checkDate();
        String loginUser = principal.split("_")[0];
        if (!logins.contains(loginUser)) {
            lCmpt.checkMaxLogins(Long.valueOf(logins.size()));
        }

        if (LoginTypeDef.USERNAME_PASSWORD.equals(loginToken.getLoginType())) {
            String username = principalSegments[0];
            username = new String(AESUtil.decrypt(String.valueOf(username),
                    AuthDef.key16.getBytes(), AuthDef.iv_16), "utf-8");
            BasicUserInfo basicUserInfo = findBasicUserInfo(username);
            if (basicUserInfo == null) {
                throw new AuthenticationException("no this user");
            }
            if (basicUserInfo.getDisabled()) {
                throw new AuthenticationException("this user has been locked");
            }
            UdAuthenticationInfo authenticationInfoCo = new UdAuthenticationInfo(principal, basicUserInfo.getEncryptPwd(),
                    UdRealm.class.getSimpleName());
            return authenticationInfoCo;
        } else {
            String phone = principalSegments[0];
            phone = new String(AESUtil.decrypt(String.valueOf(phone),
                    AuthDef.key16.getBytes(), AuthDef.iv_16), "utf-8");
            String s = redisTemplate.opsForValue().get("SMS_CODE_" + phone);
            if (s == null) {
                throw new AuthenticationException("sms code is invalidate");
            }
            Map map = JsonUtil.fromJson(Map.class, s);
            Object validateCode = map.get("validateCode");
            List<SysUserDo> list = sysUserDao.queryUserByPhone(phone);
            if (list == null || list.isEmpty()) {
                throw new AuthenticationException("no this user");
            }
            if (list.get(0).getDisabled()) {
                throw new AuthenticationException("this user has been locked");
            }
            UdAuthenticationInfo authenticationInfoCo = new UdAuthenticationInfo(principal, validateCode,
                    UdRealm.class.getSimpleName());
            return authenticationInfoCo;
        }

    }

    @Override
    public Boolean handleLoginCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws Exception {
        LoginTokenCmpt loginToken = (LoginTokenCmpt) token;

        if (LoginTypeDef.USERNAME_PASSWORD.equals(loginToken.getLoginType())) {
            String originPassword = (String) token.getCredentials();
            String encryptPassword = (String) info.getCredentials();
            if (PwdUtil.PwdEq(originPassword, encryptPassword)) {
                String[] principalSegments = String.valueOf(token.getPrincipal()).split("_");
                String username = principalSegments[0];
                username = new String(AESUtil.decrypt(String.valueOf(username), AuthDef.key16.getBytes(), AuthDef.iv_16),
                        "utf-8");
                SessionBo sessionBo = createSessionBo(username);
                sessionBo.setLoginDevice(loginToken.getDeviceType());
                sessionBo.setLoginIp(loginToken.getLoginIp());
                sessionBo.setLoginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                sessionBo.setJson("{}");
                sessionBo.setToken(String.valueOf(token.getPrincipal()));
                sessionCmpt.saveSession(String.valueOf(token.getPrincipal()), sessionBo);
                return true;
            }
        } else {
            String originSMSCode = (String) token.getCredentials();
            String rightPassword = (String) info.getCredentials();
            // 前端传过来的短信验证码和redis中正确的验证码对比
            if (rightPassword.equals(originSMSCode)) {
                String[] principalSegments = String.valueOf(token.getPrincipal()).split("_");
                String phone = principalSegments[0];
                phone = new String(AESUtil.decrypt(String.valueOf(phone), AuthDef.key16.getBytes(), AuthDef.iv_16),
                        "utf-8");
                List<SysUserDo> list = sysUserDao.queryUserByPhone(phone);
                SessionBo sessionBo = createSessionBo(list.get(0).getUsername());
                sessionBo.setLoginDevice(loginToken.getDeviceType());
                sessionBo.setLoginIp(loginToken.getLoginIp());
                sessionBo.setLoginTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                sessionBo.setJson("{}");
                sessionBo.setToken(String.valueOf(token.getPrincipal()));
                sessionCmpt.saveSession(String.valueOf(token.getPrincipal()), sessionBo);
                return true;
            }
        }

        return false;

    }

    @Override
    public AuthenticationInfo handleUrlRequest(UrlTokenCmpt ticketToken) throws Exception {
        SessionBo sessionBo = sessionCmpt.getSession(String.valueOf(ticketToken.getPrincipal()));
        if (sessionBo == null) {
            throw new UnauthenticatedException();
        }
        return new UdAuthenticationInfo(ticketToken.getPrincipal(), ticketToken.getCredentials(), UdRealm.class.getSimpleName());
    }

    @Override
    public AuthorizationInfo handleAuthorize(PrincipalCollection principals) throws Exception {
        SessionBo sessionBo = sessionCmpt.getSession(String.valueOf(principals.getPrimaryPrincipal()));
        if (sessionBo == null) {
            throw new AuthenticationException();
        }
        Set<String> roles = sessionBo.getRoles();
        Set<String> permits = sessionBo.getPermits();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permits);
        return simpleAuthorizationInfo;
    }

    @Override
    public BasicUserInfo findBasicUserInfo(String username) throws Exception {
        SysUserDo sysUserDo = findUserByName(username);
        if (sysUserDo == null) {
            return null;
        } else {
            return packBasicUserInfo(sysUserDo);
        }
    }

    @Override
    public SessionBo createSessionBo(String username) throws Exception {
        SessionBo sessionBo = new SessionBo();
        SysUserDo sysUserDo = findUserByName(username);
        BasicUserInfo basicUserInfo = packBasicUserInfo(sysUserDo);
        sessionBo.setBasicUserInfo(basicUserInfo);
        sessionBo.setPermits(findPermitsOfUser(sysUserDo.getId()));
        sessionBo.setRoles(findRolesOfUser(sysUserDo.getId()));
        sessionBo.setSa(checkSa(String.valueOf(basicUserInfo.getId())));
        return sessionBo;
    }

    /**
     * 获取用户角色列表
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> findRolesOfUser(Long userId) {
        try {
            //查询用户已有角色
            List<SysRoleDto> sysRoleDtos = sysUserLinkRoleDao.queryRoleOfUser(userId);
            if (sysRoleDtos.isEmpty()) {
                return new HashSet<>();
            }
            if (checkSa(String.valueOf(userId))) {
                return new HashSet<>();
            }
            //查询路径映射Map
            List<SysDeptDo> all = sysDeptDao.findAll();
            Map<Long, String> map = new HashMap<>();
            for (SysDeptDo sysDeptDo : all) {
                map.put(sysDeptDo.getId(), sysDeptDo.getName());
            }
            Set<String> set = new HashSet<>();
            for (SysRoleDto sysRoleDto : sysRoleDtos) {
                String path = sysRoleDto.getPath();
                sysRoleDto.setCanDelete(false);
                set.add(sysRoleDto.getRoleId() + "@" + path);
            }
            return set;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 获取用户权限列表
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> findPermitsOfUser(Long userId) {
        Set<String> set = new HashSet<>();
        List<SysSrcDto> sysSrcDtos = sysSrcDao.querySrcOfUser(userId);
        if (checkSa(String.valueOf(userId))) {
            return new HashSet<>();
        }
        sysSrcDtos.forEach(item -> {
            String deptPath = "";
            if (StringUtils.isNotBlank(item.getDeptPath())) {
                deptPath = item.getDeptPath();
            }
            set.add(item.getCode() + "@" + item.getDeptPath());
        });
        return set;

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
//        System.out.println(sp);
        return sp.toString();
    }

    /**
     * 判断是否为空字符串
     *
     * @param str
     * @return
     */
    private Boolean isInvalidStr(String str) {
        if ("null".equals(str)) {
            return true;
        } else {
            if (str.trim().equals("")) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 封装basicUserInfo类
     *
     * @param sysUserDo
     * @return
     * @throws Exception
     */
    private BasicUserInfo packBasicUserInfo(SysUserDo sysUserDo) throws Exception {
        BasicUserInfo basicUserInfo = new BasicUserInfo();
        String json = sysUserDo.getDetInfo();
        Map detInfo = JsonUtil.fromJson(Map.class, json);
        Object r;
        basicUserInfo.setId(sysUserDo.getId());
        basicUserInfo.setEmail((r = detInfo.get("email")) == null ? null : String.valueOf(r));
        basicUserInfo.setPhone((r = detInfo.get("phone")) == null ? null : String.valueOf(r));
        basicUserInfo.setAvatar((r = detInfo.get("avatar")) == null ? null : String.valueOf(r));
        basicUserInfo.setNickName((r = detInfo.get("nickName")) == null ? null : String.valueOf(r));
        basicUserInfo.setUsername(sysUserDo.getUsername());
        basicUserInfo.setEncryptPwd(sysUserDo.getPassword());
        basicUserInfo.setDeptId(sysUserDo.getDeptId());
        basicUserInfo.setDisabled(sysUserDo.getDisabled());
        return basicUserInfo;
    }

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     * @throws Exception
     */
    private SysUserDo findUserByName(String username) throws Exception {
        SysUserDo sysUserDo = new SysUserDo();
        sysUserDo.setUsername(username);
        ExampleMatcher em = ExampleMatcher.matching();
        Example example = Example.of(sysUserDo, em);
        Optional<SysUserDo> sysUserOpt = sysUserDao.findOne(example);
        if (sysUserOpt.isEmpty()) {
            return null;
        } else {
            sysUserDo = sysUserOpt.get();
            return sysUserDo;
        }
    }
}
