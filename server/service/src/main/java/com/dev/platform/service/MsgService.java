package com.dev.platform.service;

import com.dev.platform.data.dto.MsgDto;
import com.dev.platform.service.vo.MsgVo;
import com.dev.platform.service.vo.QueryMsgVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface MsgService extends BasicService {
    // <发送消息>
    void sendMsg(MsgVo msgVo, long fromId, String toIds);

    // <标记为已读>
    void setRead(long msgFromToId);

    // <撤回消息>
    void cancelMsg(long msgId);


    // <查看消息列表及阅读状态>
    // 可根据消息来源，消息去向，消息内容关键字，消息标题等查询消息
    Page<MsgDto> queryMsg(QueryMsgVo QueryMsgVo, PageRequest pageRequest);
}
