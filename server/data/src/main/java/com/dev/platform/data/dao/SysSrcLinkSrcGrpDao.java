package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysSrcLinkSrcGrpDo;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface SysSrcLinkSrcGrpDao extends JpaRepository<SysSrcLinkSrcGrpDo,Long> {
    // <根据资源分组id删除资源和资源分组关系>
    @Modifying
    @Query("delete from SysSrcLinkSrcGrpDo sls  WHERE sls.srcGrpId = :srcGrpId")
    void deleteSrcLinkSrcGrpBySrcGrpId(long srcGrpId);

    // <根据资源分组id和资源id集合删除资源和资源分组关系>
    @Modifying
    @Query("delete from SysSrcLinkSrcGrpDo sls  WHERE sls.srcGrpId = :srcGrpId and sls.srcId in :srcIds")
    void deleteSrcLinkSrcGrp(long srcGrpId, List<Long> srcIds);

}
