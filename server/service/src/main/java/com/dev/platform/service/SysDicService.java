package com.dev.platform.service;

import java.util.List;

import com.dev.platform.data.do_.SysDicDo;
import com.dev.platform.service.vo.SysDicVo;

/**
 * @author yn_zhang
 * @version 1.0
 * @description: TODO
 * @date 2021/5/31 16:22
 */
public interface SysDicService extends BasicService {

    //添加字典
    void addSysDic(SysDicVo sysDicVo) throws Exception;
    //删除字典
    void deleteSysDic(long dicId);
    //修改字典
    void editSysDic(SysDicVo sysDicVo) throws Exception;
    //查询字典分组下的字典
    List<SysDicDo> querySysDicByDicGrpId(long dicGrpId,String name);

}
