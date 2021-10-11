package com.dev.platform.data.dao;

import com.dev.platform.data.do_.MsgFromToDo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/5/31 17:45
 */
public interface MsgFromToDao extends JpaRepository<MsgFromToDo,Long> {
    // <根据消息id删除消息收发关系>
    @Modifying
    @Query(value="DELETE FROM MsgFromToDo mft WHERE mft.msgId  = :msgId")
    void deleteMsgFromToByMsgId(long msgId);
}
