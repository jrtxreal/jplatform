package com.dev.platform.controller;

import com.dev.platform.data.dto.SysRoleDto;
import com.dev.platform.data.dto.SysSrcDto;
import com.dev.platform.data.dto.SysUserDto;
import com.dev.platform.service.SysPermitToRoleService;
import com.dev.platform.service.SysRoleLinkDeptService;
import com.dev.platform.service.SysRoleService;
import com.dev.platform.service.SysUserLinkRoleService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.ResultCmpt;
import com.dev.platform.service.config.permit.PermitCmpt;
import com.dev.platform.service.def.AuthKeyDef;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.vo.SysRoleVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class SysRoleController extends BasicController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    ExceptionCmpt exceptionCmpt;
    @Autowired
    private SysUserLinkRoleService sysUserLinkRoleService;
    @Autowired
    private SysPermitToRoleService sysPermitToRoleService;
    @Autowired
    private SysRoleLinkDeptService sysRoleLinkDeptService;
    @Autowired
    private PermitCmpt permitCmpt;
    @PostMapping(value = "/u/web/sys/role/add/v1")
    public Object addSysRole(@RequestBody SysRoleVo sysRoleVo, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_add));
        try {
            sysRoleService.addRole(sysRoleVo, sysRoleVo.getDeptIds());
            return ok().setMsg("添加成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/role/edit/v1")
    public Object editSysRole(@RequestBody SysRoleVo sysRoleVo, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        try {
            sysRoleService.editRole(sysRoleVo, sysRoleVo.getDeptIds());
            return ok().setMsg("编辑成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/role/queryAll/v1")
    public Object queryAll(@RequestBody Map<String,Object> params, HttpServletRequest request) throws JsonProcessingException {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_query));
        long deptId;
        Integer pageNum;
        Integer size;
        String condition="";
        try {
            deptId =Long.parseLong(params.get("deptId").toString());
            pageNum =Integer.parseInt(params.get("page").toString());
            Object conditionObj =params.get("condition");
            if(conditionObj!=null&& StringUtils.isNotBlank(conditionObj.toString())){
                condition =conditionObj.toString();
            }
            size =Integer.parseInt(params.get("size").toString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        PageRequest pageRequest = PageRequest.of(pageNum-1, size);
        Page<SysRoleDto>  page= sysRoleService.queryRoleByDeptId(pageRequest, deptId,condition);
        return ok().setMsg("查询成功").setResult(page).toJson();
    }
    @GetMapping(value = "/u/web/sys/role/query/v1")
    public Object query(@RequestParam Long roleId, HttpServletRequest request) throws JsonProcessingException {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_query));
        SysRoleDto dto = sysRoleService.queryRoleById(roleId);
        return ok().setMsg("查询成功").setResult(dto).toJson();
    }

    @GetMapping(value = "/u/web/sys/role/queryUserRole/v1")
    public Object queryUserRole(@RequestParam Long roleId, HttpServletRequest request) throws Exception {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        List<SysUserDto> sysUserDtos = sysUserLinkRoleService.queryUserOfRole(roleId);
        return ok().setMsg("查询成功").setResult(sysUserDtos).toJson();
    }

    @PostMapping(value = "/u/web/sys/role/queryRoleOfUser/v1")
    public Object queryRoleOfUser(@RequestBody Map param, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        long userId;

        try {
            userId =Long.parseLong(param.get("userId").toString());

            List<SysRoleDto> sysRoleDtos = sysUserLinkRoleService.queryRoleOfUser(userId);
            return ok().setResult(sysRoleDtos);
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    @PostMapping(value = "/u/web/sys/role/queryAssignRoleForCurrentUser/v1")
    public Object queryAssignRoleForCurrentUser(HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        try {
            List<SysRoleDto> sysRoleDtos = sysUserLinkRoleService.queryRoleForCurrentUser();
            return ok().setResult(sysRoleDtos);
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }

    /**
     * 角色授权
     * @param sysRoleVo
     * @param request
     * @return
     */
    @PostMapping(value = "/u/web/sys/role/assign/v1")
    public Object assignOrg(@RequestBody SysRoleVo sysRoleVo, HttpServletRequest request) {
        try {
            sysRoleService.assignOrgToRole(sysRoleVo.getId(), sysRoleVo.getDeptIds());
            return ok().setMsg("授权成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/role/assignUser/v1")
    public Object assignUser(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        long roleId;
        String userIds="";
        try {
            roleId =Long.parseLong(params.get("roleId").toString());
            userIds =params.get("userIds").toString();

        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
        sysUserLinkRoleService.assignUserToRole(roleId,userIds);
        return ok();
    }

    /**
     * 用于用户管理模块角色tab点击保存按钮时调用
     * @param params
     * @param request
     * @return
     */
    @PostMapping(value = "/u/web/sys/role/assignRoleToUser/v1")
    public Object assignRoleToUser(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        long userId;
        String roleIds="";
        try {
            userId =Long.parseLong(params.get("userId").toString());
            roleIds =params.get("roleIds").toString();

        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
        sysUserLinkRoleService.assignRoleToUser(userId,roleIds);
        return ok();
    }

    /**
     * 删除角色和管理部门的关系
     * @param
     * @param
     * @return
     */
    @PostMapping(value = "/u/web/sys/role/deleteRoleDept/v1")
    public Object deleteRoleDept(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_delete));
       //@RequestParam long roleId,@RequestParam long deptId
        long roleId;

        try {
            roleId =Long.parseLong(params.get("roleId").toString());
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV,"参数错误");
        }
        sysRoleService.deleteRole(roleId);
        return ok();
    }

    /**
     * 禁用角色
     * @param
     * @param request
     * @return
     */
    @GetMapping(value = "/u/web/sys/role/disable/v1")
    public Object disableRole(@RequestParam Long roleId, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        try {
            sysRoleService.disableRole(roleId);
            return ok().setMsg("禁用成功").toJson();
        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
    }
    @PostMapping(value = "/u/web/sys/role/grantPrivilegesToRole/v1")
    public Object grantPrivilegesToRole(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        long roleId;
        String srcIds="";
        try {
            roleId =Long.parseLong(params.get("roleId").toString());
            srcIds =params.get("srcIds").toString();

        } catch (Exception e) {
            return  exceptionCmpt.handle(e);
        }
        List<Long> roleIds=new ArrayList<>();
        roleIds.add(roleId);
        List<Long>  permitIds=new ArrayList<>();
        if(StringUtils.isNotBlank(srcIds)){
            permitIds=Arrays.stream(srcIds.split(",")).map(e->Long.valueOf(e)).collect(Collectors.toList());
        }
        sysPermitToRoleService.grantPrivilegesToRole(permitIds,roleIds);
        return ok();
    }
    @GetMapping(value = "/u/web/sys/role/queryRolePermit/v1")
    public Object queryRolePermit(@RequestParam Long roleId, HttpServletRequest request) throws Exception {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_role_edit));
        List<SysSrcDto> sysSrcDtos = sysPermitToRoleService.querySrcOfRole(roleId);
        return ok().setMsg("查询成功").setResult(sysSrcDtos).toJson();
    }
}
