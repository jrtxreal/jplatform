package com.dev.platform.data.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

import com.dev.platform.data.do_.MsgDo;
import com.dev.platform.data.dto.MsgDto;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface MsgDao extends JpaRepository<MsgDo, Long> {
    @Query(value = "SELECT m.*,su.id as from_user,su.username as from_user_name FROM msg m\n" +
            "LEFT JOIN msg_from_to mft on mft.msg_id = m.id\n" +
            "LEFT JOIN sys_user su on su.id = mft.id\n" +
            "WHERE 1=1\n" +
            "and (CASE WHEN :#{#params['fromUser']} is null OR mft.from_user = :#{#params['fromUser']} THEN 1 ELSE 0 END)=1\n"+
            "and (CASE WHEN :#{#params['toUser']} is null OR mft.to_user = :#{#params['toUser']} THEN 1 ELSE 0 END)=1\n"+
            "and (CASE WHEN :#{#params['detInfo']} is null OR m.det_info LIKE :#{'%' + #params['detInfo'] + '%'} THEN 1 ELSE 0 END)=1\n",
            countQuery = "count(1)  FROM msg m\n" +
                    "LEFT JOIN msg_from_to mft on mft.msg_id = m.id\n" +
                    "LEFT JOIN sys_user su on su.id = mft.id\n" +
                    "WHERE 1=1\n" +
                    "and (CASE WHEN :#{#params['fromUser']} is null OR mft.from_user = :#{#params['fromUser']} THEN 1 ELSE 0 END)=1\n"+
                    "and (CASE WHEN :#{#params['toUser']} is null OR mft.to_user = :#{#params['toUser']} THEN 1 ELSE 0 END)=1\n"+
                    "and (CASE WHEN :#{#params['detInfo']} is null OR m.det_info LIKE :#{'%' + #params['detInfo'] + '%'} THEN 1 ELSE 0 END)=1\n",
            nativeQuery = true)
    Page<MsgDto> queryMsg(Map params, Pageable pageRequest);
}
