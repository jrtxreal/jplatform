package com.dev.platform.service;

import com.dev.platform.service.vo.PersonalInfoVo;

/**
 * @Classname PersonalService
 * @Description TODO
 * @Date 2021/6/15 9:06
 * @Author Mr.zsf
 */
public interface PersonalService extends BasicService {
    // '1。<获取个人信息>
    PersonalInfoVo findPersonalInfoById(long userId);

    // '2.<修改密码>
    void editPassword(long userId, String oldPassword, String newPassword);

    //  '3.<退出登录>
    void logout();
}
