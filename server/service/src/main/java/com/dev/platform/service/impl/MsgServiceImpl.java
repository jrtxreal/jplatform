package com.dev.platform.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import com.dev.platform.data.dao.MsgDao;
import com.dev.platform.data.dao.MsgFromToDao;
import com.dev.platform.data.do_.MsgDo;
import com.dev.platform.data.do_.MsgFromToDo;
import com.dev.platform.data.dto.MsgDto;
import com.dev.platform.service.MsgService;
import com.dev.platform.service.config.ExceptionCmpt;
import com.dev.platform.service.vo.MsgVo;
import com.dev.platform.service.vo.QueryMsgVo;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/6/1 10:52
 */
@Service
public class MsgServiceImpl extends BasicServiceImpl implements MsgService {
    @Resource
    MsgDao msgDao;
    @Resource
    MsgFromToDao msgFromToDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendMsg(MsgVo msgVo, long fromId, String toIds) {
        List<MsgFromToDo> list = new ArrayList<>();
        MsgDo msgDo = new MsgDo();
        Timestamp sendTime = new Timestamp(new Date().getTime());
        msgDo.setSendTime(sendTime);
        msgDo.setDetInfo(msgVo.getDetInfo());
        String[] ids = toIds.split("[,]");
        Arrays.asList(ids).forEach(item -> {
            Long.valueOf(item);
        });
    }

    @Override
    public void setRead(long msgFromToId) {
        Optional<MsgFromToDo> msgFromToDoOptional = msgFromToDao.findById(msgFromToId);
        if (msgFromToDoOptional.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "消息不存在或已被撤回");
        }
        MsgFromToDo msgFromToDo = msgFromToDoOptional.get();
        msgFromToDo.setReadTag(true);
        msgFromToDao.saveAndFlush(msgFromToDo);
    }

    @Override
    public void cancelMsg(long msgId) {
        Optional<MsgDo> msgDoOptional = msgDao.findById(msgId);
        if (msgDoOptional.isEmpty()) {
            throw new ExceptionCmpt(ExceptionCmpt.BIZ, "当前消息不不存在，无法取消");
        }
        MsgDo msgDo = msgDoOptional.get();
        msgDo.setCancelTime(new Timestamp(new Date().getTime()));

        // 删除与该消息相关的收发关系
        msgFromToDao.deleteMsgFromToByMsgId(msgId);
        msgDao.saveAndFlush(msgDo);
    }

    @Override
    public Page<MsgDto> queryMsg(QueryMsgVo queryMsgVo, PageRequest pageRequest) {
        Map params = new HashMap<>();
        if (queryMsgVo.getFromUser() != null) {
            params.put("fromUser", queryMsgVo.getFromUser());
        }
        if (queryMsgVo.getToUser() != null) {
            params.put("toUser", queryMsgVo.getToUser());
        }
        if (StringUtils.isNotBlank(queryMsgVo.getDetInfo())) {
            params.put("detInfo", queryMsgVo.getDetInfo());
        }
        return msgDao.queryMsg(params, pageRequest);
    }
}
