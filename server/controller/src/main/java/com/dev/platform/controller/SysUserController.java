package com.dev.platform.controller;
import cn.hutool.core.util.RandomUtil;
import com.dev.platform.data.dao.RedisDao;
import com.dev.platform.data.do_.SysUserDo;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.data.dto.SysUserDto3;
import com.dev.platform.service.SysUserLinkRoleService;
import com.dev.platform.service.SysUserService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.permit.PermitCmpt;
import com.dev.platform.service.def.AuthKeyDef;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.def.SMSTplDef;
import com.dev.platform.service.vo.SysUserVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Classname SysUserController
 * @Description TODO
 * @Date 2021/6/16 13:39
 * @Author Mr.zsf
 */
@RestController
public class SysUserController extends BasicController {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Resource
    RedisDao redisDao;
    @Autowired
    SysUserLinkRoleService sysUserLinkRoleService;
    @Autowired
    PermitCmpt permitCmpt;
    @PostMapping(value = "/u/web/sys/user/add/v1")
    public Object addUser(@RequestBody SysUserVo sysUserVo, HttpServletRequest request) {
        try {
            checkPermit(permitCmpt.get(AuthKeyDef.sys_user_add));
            sysUserService.addUser(sysUserVo);
            return ok().setMsg("添加成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/user/edit/v1")
    public Object editSysUser(@RequestBody SysUserVo sysRoleVo, HttpServletRequest request) {
        try {
            checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
            sysUserService.editBasicUserInfo(sysRoleVo);
            return ok().setMsg("编辑成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    @GetMapping(value = "/u/web/sys/user/delete/v1")
    public Object deleteUser(@RequestParam long userId, HttpServletRequest request) {
        try {
            checkPermit(permitCmpt.get(AuthKeyDef.sys_user_delete));
            sysUserService.deleteUser(userId);
            return ok().setMsg("删除成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    @GetMapping(value = "/u/web/sys/user/queryUserById/v1")
    public Object queryUserById(@RequestParam long userId, HttpServletRequest request) {
        try {
            SysUserDo sysUserDo = sysUserService.queryUserById(userId);
            return ok().setMsg("查询成功").setResult(sysUserDo).toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    /**
     * 删除用户和角色的关系
     * @param
     * @return
     */
    @PostMapping(value = "/u/web/sys/role/deleteUserRole/v1")
    public Object deleteRoleDept(@RequestBody Map<String, Object> params) {

        long roleId;
        long userId;
        try {
            roleId =Long.parseLong(params.get("roleId").toString());
            userId =Long.parseLong(params.get("userId").toString());
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
        sysUserLinkRoleService.deleteUserLinkRoleByUserIdAndRoleId(userId,roleId);
        return ok();
    }

    @PostMapping(value = "/u/web/sys/user/queryRoleUser/v1")
    public Object queryUserByPage(@RequestBody Map<String,Object> params, HttpServletRequest request) throws Exception {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_user_edit));
        long deptId=0L;
        Integer pageNum;
        Integer size;
        String username="";
        try {
            Object dept = params.get("deptId");
            if(dept!=null&&StringUtils.isNotBlank(dept.toString())){
                deptId =Long.parseLong(params.get("deptId").toString());
            }
            username=params.get("username").toString();
            pageNum =Integer.parseInt(params.get("page").toString());
            size =Integer.parseInt(params.get("size").toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<SysUserDto> sysUserDtos = sysUserService.queryUserByPage(pageRequest, deptId, username);
        return ok().setResult(sysUserDtos);
    }

    //重置密码
    @PostMapping(value = "/u/mobile/sys/user/resetPwd/v1")
    public Object resetPwd(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String adminPwd="";
        String newUserPwd="";
        long userId;
        try {
            newUserPwd = params.get("newUserPwd").toString();
            adminPwd = params.get("adminPwd").toString();
            userId = Long.parseLong(params.get("deptId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }

       sysUserService.changeUserPwdByAdmin(userId, newUserPwd,adminPwd);
        return ok().setMsg("重置成功").toJson();
    }

    //web端重置密码
    @PostMapping(value = "/u/web/sys/user/resetPwd/v1")
    public Object resetWebPwd(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String adminPwd="";
        String newUserPwd="";
        long userId;
        try {
            newUserPwd = params.get("newUserPwd").toString();
            adminPwd = params.get("adminPwd").toString();
            userId = Long.parseLong(params.get("userId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }

        sysUserService.changeUserPwdByAdmin(userId, newUserPwd,adminPwd);
        return ok().setMsg("重置成功").toJson();
    }

    /**
     * 手机端
     *@Description: 部门下的用户包含子部门
     *@Param null:
     *@Return:
     *@Author: Mr.zsf
     *@Date: 2021/6/22 14:51
     *@Version: 1.0
     */
    @PostMapping(value = "/u/mobile/sys/user/queryDeptUser/v1")
    public Object queryDeptUser(@RequestBody Map<String, Object> params,HttpServletRequest request) throws JsonProcessingException {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_user_edit));
        String condition="";
        long deptId;
        try {
            condition = String.valueOf(params.get("condition"));
            deptId = Long.parseLong(params.get("deptId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }

        List<SysUserDto3> userDtos = sysUserService.queryUserByDeptId(deptId, condition);
        return ok().setMsg("查询成功").setResult(userDtos).toJson();

    }
    @PostMapping(value = "/u/web/sys/user/changeUserPwd/v1")
    public Object changeUserPwd(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String newPwd="";
        String oldPwd="";
        try {
            oldPwd = String.valueOf(params.get("oldPwd"));
            newPwd = String.valueOf(params.get("newPwd"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysUserService.changeUserPwd(newPwd,oldPwd);
        return ok().setMsg("修改成功").toJson();
    }

    @PostMapping(value = "/g/web/sys/user/sendSMS/v1")
    public Object sendSMS(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String mobile="";
        try {
            mobile = params.get("mobile").toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }

        String smsTemplateCode= SMSTplDef.SMS_FIND_PWD_TPL;
        String smsCode = redisDao.get(smsTemplateCode + "_" + mobile);
//        System.out.println("smsCode==="+smsCode);
//        System.out.println(mobile);
        if(StringUtils.isBlank(smsCode)){
            String code= RandomUtil.randomNumbers(4);
            String content="{\"code\":\""+code+"\",\"product\":\"\"}";
//            boolean sendFlag = TaoBao.sendMessage(content, mobile, smsTemplateCode);
//            if(!sendFlag){
//                throw new ExceptionCmpt(ExceptionCmpt.BIZ,"短信发送失败");
//            }
            redisDao.putWithTimeOut(smsTemplateCode+"_"+mobile,code,5*60);
        }
        return ok().setMsg("短信发送成功").toJson();
    }

    @PostMapping(value = "/g/web/sys/user/verifySMS/v1")
    public Object verifySMS(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String mobile="";
        String code="";
        try {
            mobile = params.get("mobile").toString().trim();
            code = params.get("code").toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        String smsTemplateCode=SMSTplDef.SMS_FIND_PWD_TPL;
        String smsCode = redisDao.get(smsTemplateCode + "_" + mobile);
//        System.out.println(smsCode+"      "+code);
        if(StringUtils.isBlank(smsCode)){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"验证码已经过期");
        }
        if(!smsCode.equals(code)){
            throw new ExceptionCmpt(ExceptionCmpt.BIZ,"验证码错误");
        }
        SysUserDo userDo = sysUserService.verifySMS(mobile, code);
        redisDao.delete(smsTemplateCode + "_" + mobile);
        return ok().setMsg("验证通过").setResult(userDo).toJson();
    }

    @PostMapping(value = "/g/web/sys/user/forgotPwd/v1")
    public Object forgotPwd(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
        String mobile="";
        String newPwd="";
        try {
            mobile = params.get("mobile").toString();
            newPwd = String.valueOf(params.get("newPwd"));

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysUserService.forgotPwd(mobile,newPwd);
        return ok().setMsg("修改密码成功").toJson();
    }

   //修改头像
   @PostMapping(value = "/u/web/sys/user/editAvatar/v1")
   public Object editAvatar(@RequestBody Map<String, Object> params,HttpServletRequest request) throws Exception {
      String image="";
       try {
           image = params.get("image").toString();
       } catch (Exception e) {
           e.printStackTrace();
           throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
       }

       sysUserService.editAvatar(image);
       return ok().setMsg("修改头像成功").toJson();
   }
}
