package com.dev.platform.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.dev.platform.data.do_.SysDicDo;

public interface SysDicDao extends JpaRepository<SysDicDo,Long> {
    //根据字典分组id查询字典
    @Query(value = "SELECT d.* FROM sys_dic d WHERE d.dic_grp_id = :dicGrpId \n" +
            "AND d.`key` LIKE CONCAT('%',:name,'%')", nativeQuery = true)
    List<SysDicDo> querySysDicByDicGrpId(long dicGrpId,String name);
}
