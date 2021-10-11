package com.dev.platform.service;

import com.dev.platform.data.do_.SysLogDo;
import com.dev.platform.service.vo.SysLogVo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface SysLogService extends BasicService{
  //<添加日志>
  void addSysLog(SysLogVo sysLogVo) throws Exception;

  //<查询日志>
  Page<SysLogDo> querySysLogByPage(PageRequest pageRequest, String username);
}
