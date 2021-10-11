package com.dev.platform.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.common.util.JsonUtil;
import com.dev.platform.data.dao.SysSrcDao;
import com.dev.platform.data.dao.SysSrcLinkSrcGrpDao;
import com.dev.platform.data.do_.SysSrcDo;
import com.dev.platform.data.do_.SysSrcLinkSrcGrpDo;
import com.dev.platform.data.dto.SysSrcDto;
import com.dev.platform.data.dto.SysSrcGroupDto;
import com.dev.platform.service.SysSrcService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;
import com.dev.platform.service.util.ValidateUtil;
import com.dev.platform.service.vo.SysSrcVo;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Service
public class SysSrcServiceImpl extends BasicServiceImpl implements SysSrcService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Resource
    SysSrcDao sysSrcDao;
    @Resource
    SysSrcLinkSrcGrpDao sysSrcLinkSrcGrpDao;

    @Override
    public List<SysSrcDto> queryAllSrc() {
        return sysSrcDao.queryAllSrc();
    }

//    @Override
//    public List<SysSrcDto> querySrcOfDeptAdmin() {
//        return sysSrcDao.querySrcOfDeptAdmin();
//    }

    @Override
    public List<SysSrcDto> querySrcOfSrcGrp(long srcGrpId,String srcName) {
        try{
            JsonUtil.toJson(sysSrcDao.querySrcOfSrcGrp(srcGrpId,srcName));
        }catch (Exception e){}
        return sysSrcDao.querySrcOfSrcGrp(srcGrpId,srcName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSrcToSrcGrp(SysSrcVo sysSrcVo, long srcGrpId) throws Exception {
        ValidateUtil.validate(sysSrcVo);
        SysSrcDo sysSrcDo = new SysSrcDo();
        sysSrcDo.setDes(sysSrcVo.getDes());
        sysSrcDo.setCode(sysSrcVo.getCode());
        sysSrcDo.setName(sysSrcVo.getName());
        sysSrcDo.setDeprecated(sysSrcVo.getDeprecated());
        Date current = new Date();
        sysSrcDo.setCreateTime(new Timestamp(current.getTime()));
        sysSrcDo.setLastUpdate(new Timestamp(current.getTime()));
        sysSrcDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        try {
            sysSrcDao.saveAndFlush(sysSrcDo);
        } catch (Exception e) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "code重复");
        }
        SysSrcLinkSrcGrpDo sysSrcLinkSrcGrpDo = new SysSrcLinkSrcGrpDo();
        sysSrcLinkSrcGrpDo.setSrcGrpId(srcGrpId);
        sysSrcLinkSrcGrpDo.setSrcId(sysSrcDo.getId());
        sysSrcLinkSrcGrpDo.setOrd(sysSrcVo.getOrd());
        sysSrcLinkSrcGrpDao.saveAndFlush(sysSrcLinkSrcGrpDo);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSrc(long srcId) {
        sysSrcDao.deleteById(srcId);
        SysSrcLinkSrcGrpDo sysSrcLinkSrcGrpDo = new SysSrcLinkSrcGrpDo();
        sysSrcLinkSrcGrpDo.setSrcId(srcId);
        sysSrcLinkSrcGrpDao.deleteAll(sysSrcLinkSrcGrpDao.findAll(Example.of(sysSrcLinkSrcGrpDo)));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editSrc(SysSrcVo sysSrcVo) throws Exception {
        ValidateUtil.validate(sysSrcVo);
        SysSrcDo cdt = new SysSrcDo();
        Optional<SysSrcDo> sysSrcOptional = sysSrcDao.findById(sysSrcVo.getId());
        if (sysSrcOptional.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "该资源不存在或已被删除");
        }
        SysSrcDo sysSrcDo = sysSrcOptional.get();
        sysSrcDo.setDes(sysSrcVo.getDes());
        sysSrcDo.setCode(sysSrcVo.getCode());
        sysSrcDo.setName(sysSrcVo.getName());
        if (sysSrcVo.getDeprecated() == null) {
            sysSrcDo.setDeprecated(false);
        } else {
            sysSrcDo.setDeprecated(sysSrcVo.getDeprecated());
        }
        Date current = new Date();
        sysSrcDo.setLastUpdate(new Timestamp(current.getTime()));
        sysSrcDo.setUpdateBy(SubjectUtil.getCurrentUser(sessionCmpt).getUsername());
        sysSrcDao.saveAndFlush(sysSrcDo);
        SysSrcLinkSrcGrpDo cdt2 = new SysSrcLinkSrcGrpDo();
        cdt2.setSrcId(sysSrcDo.getId());
        Optional<SysSrcLinkSrcGrpDo> sysSrcLinkSrcGrpDoOpt =  sysSrcLinkSrcGrpDao.findOne(Example.of(cdt2));
        if(!sysSrcLinkSrcGrpDoOpt.isEmpty()){
            SysSrcLinkSrcGrpDo editTarget = sysSrcLinkSrcGrpDoOpt.get();
            editTarget.setOrd(sysSrcVo.getOrd());
            sysSrcLinkSrcGrpDao.saveAndFlush(editTarget);
        }
    }

    @Override
    public List<SysSrcDto> querySrcOfUdRole(long roleId) {
        return sysSrcDao.querySrcOfUdRole(roleId);
    }

    @Override
    public List<SysSrcDto> querySrcOfUser(long userId) {
        return sysSrcDao.querySrcOfUser(userId);
    }

  //  @Override
 //   public List<SysSrcDto> querySrcOfUdRoleWithCheck(long roleId) {
//        //'A. 查询部门管理员拥有的资源，B. 查询自定义角色拥有的资源，C. 在部门管理员拥有的资源资源中找到自定义角色的资源，并加上选中状态
//        List<SysSrcDto> srcOfDeptRole = sysSrcDao.querySrcOfDeptAdmin();
//        List<SysSrcDto> srcOfUdRole = sysSrcDao.querySrcOfUdRole(roleId);
//        srcOfDeptRole.forEach(item -> {
//            srcOfUdRole.forEach(sItem -> {
//                if (sItem.getId() == item.getId()) {
//                    item.setCheckTag(1);
//                } else {
//                    item.setCheckTag(0);
//                }
//            });
//        });
//        return srcOfDeptRole;
//    }

    @Override
    public List<SysSrcGroupDto> queryUserPermit(long roleId) throws Exception {
        Long userId = SubjectUtil.getCurrentUserId(sessionCmpt);
        //判断用户类型
        List<SysSrcDto> sysSrcDtos;
        if(checkSa(String.valueOf(userId))){
            sysSrcDtos = sysSrcDao.queryAllSrc();
        }else {
           sysSrcDtos=sysSrcDao.querySrcOfUser(userId);
        }
        List<SysSrcDto> roleSrc = sysSrcDao.querySrcOfUdRole(roleId);
        List<Long> srcIds = roleSrc.stream().map(SysSrcDto::getId).collect(Collectors.toList());
        // checkTag
        //查询用户权限下的资源
        Map<String, List<SysSrcDto>> menuGroupMap = sysSrcDtos.stream().collect(Collectors.groupingBy(v -> v.getSrcGrpId() + "_" + v.getSrcGrpName()+"_"+v.getOrd()));
        List<SysSrcGroupDto> dtos = menuGroupMap.keySet().stream().map(key -> {
            String[] keyArr = key.split("_");
            String groupId = keyArr[0];
            String groupName = keyArr[1];
            Integer ord = Integer.valueOf(keyArr[2]);
            SysSrcGroupDto groupDto = new SysSrcGroupDto();
            groupDto.setId("null".equals(groupId)?null:Long.valueOf(groupId));
            groupDto.setName(groupName);
            groupDto.setOrd(ord);
            List<SysSrcDto> subSrcList = menuGroupMap.get(key);
            subSrcList.forEach(e->{
                if(srcIds.contains(e.getId())){
                    e.setCheckTag(1); //选中
                }else {
                    e.setCheckTag(0);
                }
            });
            groupDto.setList(subSrcList);
            return groupDto;
        }).collect(Collectors.toList());
        dtos.sort(Comparator.comparing(SysSrcGroupDto::getOrd));
        return dtos;
    }
}
