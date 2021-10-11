package com.dev.platform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.SysSrcLinkSrcGrpDao;
import com.dev.platform.data.do_.SysSrcLinkSrcGrpDo;
import com.dev.platform.service.SysSrcLinkSrcGrpService;

import java.util.ArrayList;
import java.util.List;
/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
@Service
public class SysSrcLinkSrcGrpServiceImpl extends BasicServiceImpl implements SysSrcLinkSrcGrpService {
    @Resource
    private SysSrcLinkSrcGrpDao sysSrcLinkSrcGrpDao;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveSrcInBatch(List<Long> srcIds, long sourceSrcGrpId, long destSrcGrpId) throws Exception{
        sysSrcLinkSrcGrpDao.deleteSrcLinkSrcGrp(sourceSrcGrpId,srcIds); // 先根据srcIds和sourceSrcGrpId, 进行批量删除
        List<SysSrcLinkSrcGrpDo> sysSrcLinkSrcGrpDoList = new ArrayList<>();
        srcIds.forEach(item->{
            SysSrcLinkSrcGrpDo sysSrcLinkSrcGrpDo = new SysSrcLinkSrcGrpDo();
            sysSrcLinkSrcGrpDo.setSrcId(item);
            sysSrcLinkSrcGrpDo.setSrcGrpId(destSrcGrpId);
            sysSrcLinkSrcGrpDoList.add(sysSrcLinkSrcGrpDo);
        });
        sysSrcLinkSrcGrpDao.saveAll(sysSrcLinkSrcGrpDoList); // 再根据srcIds和destSrcGrpId,进行批量插入
        sysSrcLinkSrcGrpDao.flush();
    }
}
