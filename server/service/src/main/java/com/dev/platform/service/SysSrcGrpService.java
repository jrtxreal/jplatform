package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.do_.SysSrcGrpDo;
import com.dev.platform.service.vo.SysSrcGrpVo;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysSrcGrpService extends BasicService {

    // <添加资源分类>
    void addSrcGrp(SysSrcGrpVo sysSrcGrpVo) throws Exception;

    // <查询所有资源分类>
    // 默认按排序号进行排序
    List<SysSrcGrpDo> queryAllSrcGrp();

    // <修改资源分组>
    // 可以编辑的字段[name,ord]，sysSrcGrpDo中id不能为空
    void editSrcGrp(SysSrcGrpVo sysSrcGrpVo) throws Exception;

    // <删除资源分组>
    // A.查询分组下的所有资源Id，B.删除资源分组，C.删除分组下的资源，D.删除资源和分组关系，E.删除资源和角色关系
    void deleteSrcGrp(long srcGrpId) throws Exception;

}
