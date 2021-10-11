package com.dev.platform.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import com.dev.platform.data.dto.SysDeptDto;
import com.dev.platform.data.dto.SysRoleDto;

public interface SysRoleLinkDeptService extends BasicService {



   // '1.<查询当前角色的管理部门，带当前用户可关闭标志>
      List<SysDeptDto> queryDeptForRole(long roleId);

    //'2.<查询当前部门下的角色，对应的是管理角色>
      Page<SysRoleDto> queryRoleOfDept(PageRequest pageRequest, long deptId);

      void deleteRoleDept(long deptId,long roleId);
}
