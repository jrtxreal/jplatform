package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.dto.SysSrcDto;
import com.dev.platform.data.dto.SysSrcGroupDto;
import com.dev.platform.service.vo.SysSrcVo;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysSrcService extends BasicService{
    // <在某资源分组下添加资源>
    void addSrcToSrcGrp(SysSrcVo sysSrcVo,long srcGrpId)  throws Exception;

    // <查询所有资源>
    // 按组返回
    List<SysSrcDto>  queryAllSrc();

//    // <查询部门管理员的资源范围>
//    //按组返回
//    List<SysSrcDto> querySrcOfDeptAdmin();

    // <查询某分组下资源>
    List<SysSrcDto> querySrcOfSrcGrp(long srcGrpId,String srcName);

    // <查询某角色的资源>
    List<SysSrcDto> querySrcOfUdRole(long roleId);


    //  <查询带选中状态的自定义角色资源>
    //  A. 查询部门管理员拥有的资源，B. 查询自定义角色拥有的资源，C. 在部门管理员拥有的资源资源中找到自定义角色的资源，并加上选中状态
    //  List<SysSrcDto> querySrcOfUdRoleWithCheck(long roleId);

    // <查询某用户的资源>
    List<SysSrcDto> querySrcOfUser(long userId);


    // <修改资源>
    void editSrc(SysSrcVo sysSrcVo) throws Exception;

    // <删除资源>
    void deleteSrc(long srcId);

    List<SysSrcGroupDto> queryUserPermit(long roleId) throws Exception;

}
