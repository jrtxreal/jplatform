package com.dev.platform.controller;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dev.platform.service.SysPermitToRoleService;
import com.dev.platform.service.SysSrcGrpService;
import com.dev.platform.service.SysSrcLinkSrcGrpService;
import com.dev.platform.service.SysSrcService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.permit.PermitCmpt;
import com.dev.platform.service.def.AuthKeyDef;
import com.dev.platform.service.def.MsgDef;
import com.dev.platform.service.vo.SysSrcGrpVo;
import com.dev.platform.service.vo.SysSrcVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : 资源管理
 * @date : 2021/6/2 10:23
 */
@RestController
public class SrcController extends BasicController {
    @Autowired
    SysSrcService sysSrcService;
    @Autowired
    SysSrcGrpService sysSrcGrpService;
    @Autowired
    SysPermitToRoleService sysPermitToRoleService;
    @Autowired
    SysSrcLinkSrcGrpService sysSrcLinkSrcGrpService;
    @Autowired
    PermitCmpt permitCmpt;

    // <添加资源分类>
    @RequestMapping(value = "/u/web/srcGrp/add/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcGrpAdd(@RequestBody SysSrcGrpVo sysSrcGrpVo,
                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        sysSrcGrpService.addSrcGrp(sysSrcGrpVo);
        return ok();
    }

    // <查询所有资源分类>
    @RequestMapping(value = "/u/web/srcGrp/queryAll/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcGrpQueryAll(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ok().setResult(sysSrcGrpService.queryAllSrcGrp());
    }

    // <修改资源分类>
    @RequestMapping(value = "/u/web/srcGrp/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcGrpEdit(@RequestBody SysSrcGrpVo sysSrcGrpVo,
                             HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        sysSrcGrpService.editSrcGrp(sysSrcGrpVo);
        return ok();
    }

    // <删除资源分组>
    @RequestMapping(value = "/u/web/srcGrp/delete/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcGrpDelete(@RequestBody Map<String, Object> params,
                               HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        Long srcGrpId = null;
        Object r;
        try {
            srcGrpId = (r = params.get("srcGrpId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            if (srcGrpId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysSrcGrpService.deleteSrcGrp(srcGrpId);
        return ok();
    }

    // <查询所有资源>
    @RequestMapping(value = "/u/web/src/queryAll/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcQueryAll(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ok().setResult(sysSrcService.queryAllSrc());
    }

    //  <查询某分组下资源>
    @RequestMapping(value = "/u/web/src/queryOfGrp/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcQueryOfGrp(@RequestBody Map<String, Object> params,
                                HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long srcGrpId = null;
        String srcName = null;
        try {
            Object r;
            srcName = (r = params.get("srcName")) != null ? String.valueOf(r) : null;
            srcGrpId = (r = params.get("srcGrpId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            if (srcGrpId == null) {
                throw new Exception();
            }
            if(StringUtils.isBlank(srcName)){
                srcName = null;
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        return ok().setResult(sysSrcService.querySrcOfSrcGrp(srcGrpId,srcName));
    }

    //  <在某资源分组下添加资源>
    @RequestMapping(value = "/u/web/src/addToGrp/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcAddToGrp(@RequestBody Map<String, Object> params,
                              HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        Long srcGrpId = null;
        SysSrcVo sysSrcVo = new SysSrcVo();
        try {
            BeanUtils.populate(sysSrcVo, params);
            Object r;
            srcGrpId = (r = params.get("srcGrpId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            if (srcGrpId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysSrcService.addSrcToSrcGrp(sysSrcVo, srcGrpId);
        return ok();
    }

    //  <删除资源>
    @RequestMapping(value = "/u/web/src/delete/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcDelete(@RequestBody Map<String, Object> params,
                            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        Long srcId = null;
        try {
            Object r;
            srcId = (r = params.get("srcId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            if (srcId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysSrcService.deleteSrc(srcId);
        return ok();
    }

    //  <修改资源>
    @RequestMapping(value = "/u/web/src/edit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcEdit(@RequestBody SysSrcVo sysSrcVo,
                          HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit("~~~~~~");
        sysSrcService.editSrc(sysSrcVo);
        return ok();
    }

    //  <查询某角色的资源>
    @RequestMapping(value = "/g/web/src/queryOfUdRole/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcQueryOfUdRole(@RequestBody Map<String, Object> params,
                                   HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long roleId = null;
        try {
            roleId = params.get("roleId") != null ? Long.valueOf(String.valueOf(roleId)) : null;
            if (roleId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysSrcService.querySrcOfUdRole(roleId);
        return ok();
    }

    //  <查询某用户资源>
    @RequestMapping(value = "/u/web/src/queryOfUser/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcQueryOfUser(@RequestBody Map params,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Long userId = null;
        try {
            Object o = params.get("userId");
            if(o!=null){
                userId = Long.valueOf(String.valueOf(o));
            }

            if (userId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }

        return ok().setMsg("查询成功").setResult(sysSrcService.querySrcOfUser(userId));
    }

    //  <批量授权>
    @RequestMapping(value = "/g/web/permitToRole/grant/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object permitToRoleGrant(@RequestBody Map<String, Object> params,
                                    HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        checkPermit(permitCmpt.get(AuthKeyDef.sys_src_edit));
        String srcIds = null;
        String roleIds = null;
        List<Long> srcIdList = new ArrayList<>();
        List<Long> roleIdList = new ArrayList<>();
        try {

            srcIds = String.valueOf(params.get("srcIds"));
            Arrays.asList(srcIds.split("[,]")).forEach(item -> {
                srcIdList.add(Long.valueOf(item));
            });
            roleIds = String.valueOf(params.get("roleIds"));
            Arrays.asList(roleIds.split("[,]")).forEach(item -> {
                roleIdList.add(Long.valueOf(item));
            });
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysPermitToRoleService.grantPrivilegesToRole(srcIdList, roleIdList);
        return ok();
    }

    //  <将某分组下的资源移动到另外一个分组>
    @RequestMapping(value = "/u/web/src/srcMoveInBatch/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.POST)
    public Object srcMoveInBatch(@RequestBody Map<String, Object> params,
                                 HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // only sa has this permission
        checkPermit("~~~~~");
        String srcIds = null;
        Long sourceSrcGrpId = null;
        Long destSrcGrpId = null;
        List<Long> srcIdList = new ArrayList<>();
        Object r;
        try {
            srcIds = String.valueOf(params.get("srcIds"));
            Arrays.asList(srcIds.split("[,]")).forEach(item -> {
                srcIdList.add(Long.valueOf(item));
            });
            sourceSrcGrpId = (r = params.get("sourceSrcGrpId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            destSrcGrpId = (r = params.get("destSrcGrpId")) != null ? Long.valueOf(String.valueOf(r)) : null;
            if (sourceSrcGrpId == null || destSrcGrpId == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.DEV, MsgDef.PARAM_ERROR);
        }
        sysSrcLinkSrcGrpService.moveSrcInBatch(srcIdList, sourceSrcGrpId, destSrcGrpId);
        return ok();
    }

    @RequestMapping(value = "/u/web/src/queryUserPermit/v1",
            produces = "application/json; charset=utf-8",
            method = RequestMethod.GET)
    public Object queryUserPermit(@RequestParam long roleId,HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return ok().setResult(sysSrcService.queryUserPermit(roleId));
    }
}
