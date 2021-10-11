package com.dev.platform.data.dao;

import com.dev.platform.data.do_.SysLogDo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SysLogDao extends JpaRepository<SysLogDo, Long> {
    @Query(value = "select * from sys_log sl where 1=1\n" +
            "and (case when :condition is null or sl.module like :#{'%'+#condition+'%'} then 1 else 0 end)=1\n" +
            "order by sl.request_time desc",
            countQuery = "select count(1) from sys_log sl where 1=1\n" +
                    "and (case when :condition is null or sl.module like :#{'%'+#condition+'%'} then 1 else 0 end)=1\n"
            , nativeQuery = true)
    Page<SysLogDo> querySysLogByPage(Pageable pageRequest, String condition);
}
