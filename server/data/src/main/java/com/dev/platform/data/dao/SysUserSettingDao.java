package com.dev.platform.data.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;

import com.dev.platform.data.do_.SysUserSettingDo;

public interface SysUserSettingDao extends JpaRepository<SysUserSettingDo,Long> {

    // <根据用户id删除用户和个人设置间的关系>
//    @Modifying
//    @Query("delete from SysUserSettingDo sus  WHERE sus.userId = :userId")
//    void deleteSysUserSettingByUserId(long userId);


    @Query(value = "SELECT us.* FROM sys_user_setting us WHERE us.user_id = :userId", nativeQuery = true)
    SysUserSettingDo findUserSettingByUserId(long userId);

    @Query(value = "SELECT us.* FROM sys_user_setting us WHERE us.user_id = :userId and us.key =:orderType", nativeQuery = true)
    SysUserSettingDo findOrderTypeByIdAndOrder(long userId, String orderType);

    @Query(value = "SELECT us.* FROM sys_user_setting us WHERE us.user_id = :userId and us.key =:sortType", nativeQuery = true)
    SysUserSettingDo findSortTypeByIdAndOrder(long userId, String sortType);

    @Modifying
    @Query(value = "update sys_user_setting us  set us.val =:order_type WHERE us.user_id =:userId and us.key =:orderType", nativeQuery = true)
    void updateOrderType(long userId, String orderType, String order_type);

    @Modifying
    @Query(value = "update sys_user_setting us set us.val =:sort_type WHERE us.user_id = :userId and us.key =:sortType", nativeQuery = true)
    void updateSortType(long userId, String sortType, String sort_type);

    @Modifying
    @Query(value = "insert into sys_user_setting (user_id,`key`, val, last_update) values(:userId,:key,:val,:date)", nativeQuery = true)
    void insertOrderSortType(long userId, String key, String val, Timestamp date);
}
