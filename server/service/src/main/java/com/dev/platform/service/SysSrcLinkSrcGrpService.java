package com.dev.platform.service;

import java.util.List;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysSrcLinkSrcGrpService extends BasicService{
    // <将某分组下的资源移动到另外一个分组>
    // 分为AB两步：
    // A.先根据srcIds和sourceSrcGrpId, 进行批量删除
    // B.再根据srcIds和destSrcGrpId,进行批量插入
    void moveSrcInBatch(List<Long> srcIds, long sourceSrcGrpId, long destSrcGrpId) throws Exception;
}
