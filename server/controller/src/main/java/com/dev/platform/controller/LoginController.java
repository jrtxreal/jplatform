package com.dev.platform.controller;

import com.dev.platform.service.pojo.BasicUserInfo;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dev.platform.common.util.AESUtil;
import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.common.util.RSAUtil;
import com.dev.platform.data.def.db.RedisDef;
import com.dev.platform.data.do_.SysGlobalSettingDo;
import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.service.AuthService;
import com.dev.platform.service.SysSettingService;
import com.dev.platform.service.SysUserService;
import com.dev.platform.service.bo.SessionBo;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.LoginTokenCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.def.AuthDef;
import com.dev.platform.service.def.DeviceTypeDef;
import com.dev.platform.service.def.LoginTypeDef;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.common.util.CaptchaUtil;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@RestController
public class LoginController extends BasicController {
    @Autowired
    private AuthService authService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    SessionCmpt sessionCmpt;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    SysSettingService sysSettingService;

    @RequestMapping(value = "/g/web/sys/login/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object login(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String ecStr = String.valueOf(map.get("ec"));
        String json = RSAUtil.decrypt(ecStr, AuthDef.private_key);
        Map realParams = JsonUtil.fromJson(Map.class, json);

        String deviceType = String.valueOf(realParams.get("deviceType"));
        if (isInvalidStr(deviceType)) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "请设置设备类型");
        }

        String loginType = String.valueOf(realParams.get("loginType"));
        if (isInvalidStr(loginType)) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, "请设置登录类型");
        }

        if (deviceType.equals(DeviceTypeDef.BROWSER) && LoginTypeDef.USERNAME_PASSWORD.equals(loginType)) {
            //验证码校验
            String state = String.valueOf(realParams.get("state"));
            if (isInvalidStr(state)) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, "请设置state");
            }
            String xpos_temp = String.valueOf(realParams.get("xpos"));
            if (isInvalidStr(xpos_temp)) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, "xpos参数缺失");
            }
            int xpos = 0;
            try {
                xpos = Integer.valueOf(xpos_temp);
            } catch (NumberFormatException e) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, "xpos无法转换成数字");
            }

            String prefix = "VerifyCode_";
            String s = String.valueOf(redisTemplate.opsForValue().get(prefix + state));
            if (isInvalidStr(s)) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, "验证码已失效");
            }
            Map map1 = JsonUtil.fromJson(Map.class, s);
            int X = (int) map1.get("xWidth");
            redisTemplate.delete(prefix + state);
            if (Math.abs(X - xpos) >= 10) {
                Map<String, Object> map2 = new HashMap<>();
                map2.put("code", 4);
                map2.put("msg", "滑块位置不正确,无法解锁");
                return ok().setResult(map2);
            }

        }
        LoginTokenCmpt loginTokenCmpt = authService.createLoginTokenCmpt(realParams);
        loginTokenCmpt.setLoginIp(request.getRemoteHost());
        String[] s = String.valueOf(loginTokenCmpt.getPrincipal()).split("_");
        String encryptUsername = s[0];
        String username = new String(AESUtil.decrypt(encryptUsername, AuthDef.key16.getBytes("utf-8"),
                AuthDef.iv_16));

        // 查询到全局配置，看是否启用了连续多次输错密码强制锁定用户
        List<SysGlobalSettingDo> sysGlobalSettingDoList = sysSettingService.getSysGlobalSetting();
        SysGlobalSettingDo loginSetting = null;
        for (SysGlobalSettingDo item : sysGlobalSettingDoList) {
            if (item.getKey().equals("login")) {
                loginSetting = item;
            }
        }
        String keySetJson = loginSetting.getVal();
        Map loginMap = fromJson(Map.class, keySetJson);
        boolean swpla = Boolean.valueOf(String.valueOf(loginMap.get("swpla")));
        try {
            SubjectUtil.getSubject().login(loginTokenCmpt);
            // swpla : series wrong password lock account连续输错密码锁定账户
            if(swpla){
                redisTemplate.delete(username + "_AccountLock"); //登录成功，将redis中记录立刻删除，重新计数
            }

        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
            if (null != e.getMessage() && e.getMessage().contains("did not match the expected credentials")) {
                if(swpla){
                    String s1 = redisTemplate.opsForValue().get(username + "_AccountLock");
                    int num = 0;
                    if (!isInvalidStr(String.valueOf(s1))){
                        num = Integer.parseInt(String.valueOf(s1));
                        num=num+1;
                        redisTemplate.opsForValue().set(username + "_AccountLock", String.valueOf(num), RedisDef.ACCOUNT_TIME_LOCK, TimeUnit.SECONDS);
                        if(num>=5){
                            sysUserService.lockUser(username);
                            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "连续输入用户名或密码错误次数过多，该账户已锁定"); // 密码错误
                        }
                    }else {
                        num=num+1;
                        redisTemplate.opsForValue().set(username + "_AccountLock", String.valueOf(num), RedisDef.ACCOUNT_TIME_LOCK, TimeUnit.SECONDS);
                    }
                    throw new ExceptionCmpt(ExceptionCmpt.BIZ, "用户名或密码错误,还有"+(5-num)+"次机会！"); // 密码错误
                }else {
                    throw new ExceptionCmpt(ExceptionCmpt.BIZ, "用户名或密码错误！"); // 密码错误
                }
            }else if(null != e.getMessage() && e.getMessage().contains("no this user")){
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "用户名或密码错误"); // 用户名错误
            }else if(null != e.getMessage() && e.getMessage().contains("sms code is invalidate")){
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "验证码已失效"); // 验证码失效
            }else if(null != e.getMessage() && e.getMessage().contains("this user has been locked")){
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该用户已被锁定"); // 用户已锁定
            }
        }
        SessionBo sessionBo = SubjectUtil.getSessionBo(sessionCmpt);
        BasicUserInfo basicUserInfo = sessionBo.getBasicUserInfo();
        if(basicUserInfo.getDisabled()){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该账号已被锁定，请联系管理员"); // 账号被锁定
        }
        basicUserInfo.setEncryptPwd(null);
        sessionBo.setJson(null);
        return ok().setResult(sessionBo);
    }
    @RequestMapping(value = "/g/web/sys/logout/v1", produces = "application/json; charset=utf-8",
            method = RequestMethod.GET)
    public Object logout(String token, HttpServletResponse response){
        SubjectUtil.logOut(sessionCmpt,token);
        return ok();
    }
    @RequestMapping(value = "/g/web/sys/getKaptcha/v1", produces = "application/json; charset=utf-8",
            method = RequestMethod.GET)
    public Object kaptcha(String state, HttpServletResponse response) {
        try {
            // 1.获取stateID
            if (state == null) {
                throw new ExceptionCmpt(ExceptionCmpt.DEV, "state必填");
            }
            int randNum = new Random().nextInt(22) + 1;
            // 随机选择剪切模版
            Random r = new Random();
            int num = r.nextInt(6) + 1;

            InputStream targetFile = new ClassPathResource("sliderimage/targets/" + randNum + ".jpg").getInputStream();
            InputStream tempImgFile = new ClassPathResource("sliderimage/templates/" + num + "-w.png").getInputStream();
            // 根据模板裁剪图片
            try {
                // 2.生成随机验证码
                Map<String, Object> kaptchaInfo = CaptchaUtil.pictureTemplatesCut(tempImgFile, targetFile);

                Map kaptchaCache = new HashedMap();
                Iterator iterator = kaptchaInfo.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
                    if (!entry.getKey().equals("slidingImage") && !entry.getKey().equals("backImage")) {
                        kaptchaCache.put(entry.getKey(), entry.getValue());
                    }
                }
                //缓存到redis
                String s = toJson(kaptchaCache);
                String prefix = "VerifyCode_";
                redisTemplate.opsForValue().set(prefix + state, s, 3 * 60, TimeUnit.SECONDS);

                // 移除横坐标送前端
                kaptchaInfo.remove("xWidth");
                return ok().setResult(kaptchaInfo);
            } catch (Exception e) {
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "验证码生成时发生了错误"); // 账号被锁定
            }

        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
    }

    /*获取短信验证码*/
    @RequestMapping(value = "/g/web/sys/getMsgCode/v1", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    public Object getMsgCode(@RequestParam String phone, HttpServletResponse response) {
        try {
            // 1.获取StateID
            if (phone == null) {
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "请填写手机号码");
            }
            // 2.查看手机号码是否存在
            SysUserDo sysUserDo = sysUserService.queryUserByPhone(phone);
            if (sysUserDo == null) {
                throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该用户(手机号码)不存在");
            }

            // 3.从redis中读取短信验证码
            String smsCode = redisTemplate.opsForValue().get("SMS_CODE_" + phone);
            if (smsCode != null) {
                Map map = fromJson(Map.class, smsCode);
                long vTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(map.get("validateTime").toString()).getTime();
                long current = new Date().getTime();
                if (current - vTime < 120*1000) {
                    throw new ExceptionCmpt(ExceptionCmpt.BIZ, "短信验证再次发送需要有120秒间隔");
                }
            }
            // 2.生成随机验证码
            String validateCode = String.format("%06d", (int) (Math.random() * 1000000));
            String validateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Map<String,Object> map = new HashMap();
            map.put("validateCode", validateCode);
            map.put("validateTime", validateTime);
            String JStr = toJson(map);
            Map<String,String> contentMap = new HashMap<>();
            contentMap.put("code",validateCode);
            return ok().setResult(null);
        } catch (Exception e) {
            return exceptionCmpt.handle(e);
        }
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

}
