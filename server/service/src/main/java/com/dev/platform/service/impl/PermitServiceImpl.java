package com.dev.platform.service.impl;

import com.dev.platform.service.PermitService;
import com.dev.platform.service.config.shiro.SessionCmpt;
import com.dev.platform.service.util.SubjectUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/7/19 20:27
 */
@Service
public class PermitServiceImpl extends BasicServiceImpl implements PermitService {
    @Autowired
    SessionCmpt sessionCmpt;
    @Override
    public boolean checkPermit(String permit) {
        try{
           String userId =  String.valueOf(SubjectUtil.getCurrentUserId(sessionCmpt));
           if(checkSa(userId)){
               return true;
           }
           try{
               SubjectUtil.getSubject().checkPermission(permit);
               return true;
           }catch (Exception e){
               return false;
           }
        }catch (Exception e){
            return false;
        }
    }
}
