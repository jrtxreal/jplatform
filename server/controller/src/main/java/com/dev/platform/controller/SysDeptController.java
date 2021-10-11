package com.dev.platform.controller;
import com.dev.platform.controller.vo.PageRequestVo;
import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysOrgUserDto;
import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.service.SysDeptService;
import com.dev.platform.service.SysRoleService;
import com.dev.platform.service.SysUserService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysDeptVo;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
public class SysDeptController extends BasicController {
    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;

    // 查询部门列表
    @GetMapping(value = "/u/web/sys/dept/query/v1")
    public Object query(@RequestParam long deptId, HttpServletRequest request) throws JsonProcessingException {
        SysDeptDto dto = sysDeptService.querySysDeptById(deptId);
        return ok().setMsg("查询成功").setResult(dto).toJson();
    }

    // 查询用户管理部门
    @PostMapping(value = "/u/web/sys/dept/queryAdminDeptOfUser/v1")
    public Object queryAdminDeptOfUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
       return ok().setResult(sysDeptService.queryAdminOrgOfUser());
    }

    // 根据用户userId,查询用户管理部门
    @PostMapping(value = "/u/web/sys/dept/queryAdminDeptOfUser1/v1")
    public Object queryAdminDeptOfUser1(@RequestBody Map params) throws Exception {
        long userId;
        try {
            userId =Long.parseLong(params.get("userId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        return ok().setResult(sysDeptService.queryAdminOrgOfUser1(userId));
    }


    // 查询用户管理部门和所属部门
    @PostMapping(value = "/u/web/sys/dept/queryDeptOfUser/v1")
    public Object queryDeptOfUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ok().setResult(sysDeptService.queryDeptOfUser());
    }

    @PostMapping(value = "/u/web/sys/dept/add/v1")
    public Object addSysdept(@RequestBody SysDeptVo sysDeptVo, HttpServletRequest request) {
        try {
            sysDeptService.addSysDept(sysDeptVo);
            return ok().setMsg("添加成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/dept/edit/v1")
    public Object editSysdept(@RequestBody SysDeptVo sysDeptVo, HttpServletRequest request) {
        try {
            sysDeptService.editSysDept(sysDeptVo);
            return ok().setMsg("编辑成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/dept/queryAll/v1")
    public Object queryAll(@RequestParam Map<String, Object> params, HttpServletRequest request) throws JsonProcessingException {
        boolean spreadTag;
        long deptId;
        try {
            spreadTag =Boolean.parseBoolean(params.get("spreadTag").toString());
            deptId =Long.parseLong(params.get("deptId").toString());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        List<SysDeptDto> sysDeptDtos = sysDeptService.querySysDept(deptId, spreadTag);
        return ok().setMsg("查询成功").setResult(sysDeptDtos).toJson();
    }

//    @PostMapping(value = "/u/web/sys/dept/tree/v1")
//    public Object tree(@RequestParam Map<String, Object> params, HttpServletRequest request) throws Exception {
//        return ok().setResult(sysDeptService.queryDeptOfUser());
//    }


    /**
     * 变更组织路径
     * @param
     * @param request
     * @return
     */
    @PostMapping(value = "/u/web/sys/dept/changePath/v1")
    public Object changePath(@RequestParam(value = "currentDeptId",required = true) long currentDeptId,
                             @RequestParam(value = "targetDeptId",required = true) long targetDeptId, HttpServletRequest request) {
        try {
            sysDeptService.changeSysOrgPath(currentDeptId, targetDeptId);
            return ok().setMsg("变更成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    /**
     * 删除部门
     * @param
     * @param request
     * @return
     */
    @GetMapping(value = "/u/web/sys/dept/delete/v1")
    public Object deleteDept(@RequestParam long deptId, HttpServletRequest request) {
        try {
            sysDeptService.deleteSysDept(deptId);
            return ok().setMsg("删除成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    /**
     *
     *@Description:
     *@Param null:
     *@Return:
     *@Author: Mr.zsf
     *@Date: 2021/6/22 14:02
     *@Version: 1.0
     */
    @GetMapping(value = "/u/web/sys/dept/queryOrgAndUserList/v1")
    public Object queryOrgAndUserList(@RequestParam(required = false) String deptId,HttpServletRequest request) {
        try {
            List<SysOrgUserDto> list = sysDeptService.queryDeptAndUserList(deptId);
            return ok().setMsg("查询成功").setResult(list).toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    /**
     * web端后台查询部门下的用户（包括子部门的用户）
     * @param params
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/u/web/sys/dept/queryUserOfDept/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object queryUserOfDept(@RequestBody Map<String,Object> params,HttpServletRequest request) throws Exception{
        PageRequestVo pageRequestVo = new PageRequestVo();
        Long deptId = null;
        String condition = "";
       try{
           Object r;
           org.apache.commons.beanutils.BeanUtils.populate(pageRequestVo,params);
           deptId = (r=params.get("deptId"))!=null?Long.valueOf(String.valueOf(r)):null;
           if(deptId==null){
               throw new ExceptionCmpt(ExceptionCmpt.DEV,"deptId不能为空");
           }
           condition =(r=params.get("condition"))!=null?String.valueOf(r):null;
       }catch (Exception e){
              throw new ExceptionCmpt(ExceptionCmpt.DEV,MsgDef.PARAM_ERROR);
       }
        ValidateUtil.validate(pageRequestVo);
        PageRequest pageRequest = PageRequest.of(pageRequestVo.getPage()-1,pageRequestVo.getSize());
        Page<SysUserDto> page = sysUserService.queryUserByDeptIdByPage(pageRequest,deptId,condition);
        return ok().setResult(page);
    }
    @RequestMapping(value = "/u/web/sys/dept/queryRoleOfDept/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object queryRoleOfDept(@RequestBody Map<String,Object> params,HttpServletRequest request) throws Exception{
        PageRequestVo pageRequestVo = new PageRequestVo();
        Long deptId = null;
        try{
            Object r;
            org.apache.commons.beanutils.BeanUtils.populate(pageRequestVo,params);
            deptId = (r=params.get("deptId"))!=null?Long.valueOf(String.valueOf(r)):null;
            if(deptId==null){
                throw new ExceptionCmpt(ExceptionCmpt.DEV,"deptId不能为空");
            }
        }catch (Exception e){
            throw new ExceptionCmpt(ExceptionCmpt.DEV,MsgDef.PARAM_ERROR);
        }
        ValidateUtil.validate(pageRequestVo);
        PageRequest pageRequest = PageRequest.of(pageRequestVo.getPage(),pageRequestVo.getSize());
        Page<SysRoleDto> page = sysRoleService.queryRoleByDeptId(pageRequest,deptId,null);
        return ok().setResult(page);
    }
    /**
     * 手机端
     *@Description: 部门列表
     *@Param null:
     *@Return:
     *@Author: Mr.zsf
     *@Date: 2021/6/22 14:51
     *@Version: 1.0
     */
    @GetMapping(value = "/u/mobile/sys/dept/queryOrgList/v1")
    public Object queryOrgList(@RequestParam(required = false) String condition, HttpServletRequest request) {
        try {
            List<SysDeptDto> list = sysDeptService.queryOrgList(condition);
            return ok().setMsg("查询成功").setResult(list).toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
}
