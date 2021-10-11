package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.do_.SysDicGrpDo;
import com.dev.platform.service.vo.SysDicGrpVo;

/**
 * @author yn_zhang
 * @version 1.0
 * @description: TODO
 * @date 2021/5/31 16:26
 */
public interface SysDicGrpService extends BasicService {
    //添加字段分组
    void addSysDicGrp(SysDicGrpVo sysDicGrpVo) throws Exception;
    //删除字典分组
    void deleteSysDicGrp(long dicGrpId);
    //修改字典分组
    void editSysDicGrp(SysDicGrpVo sysDicGrpVo) throws Exception;
    //查询字典分组
    List<SysDicGrpDo> querySysDicGrp();
}
