package com.dev.platform.service.def;

import java.util.HashMap;
import java.util.Map;

import com.dev.platform.common.util.AESUtil;
import com.dev.platform.common.util.JsonUtil;

/**
 * @author : Qin_ZhiYuan
 * @version : 1.0
 * @description : TODO
 * @date : 2021/7/19 15:54
 */
public class AuthKeyDef {
    public static final String sys_user_add = "sys_user_add";
    public static final String sys_user_delete = "sys_user_delete";
    public static final String sys_user_edit = "sys_user_edit";
    public static final String sys_user_query = "sys_user_query";
    public static final String sys_role_add = "sys_role_add";
    public static final String sys_role_delete = "sys_role_delete";
    public static final String sys_role_edit = "sys_role_edit";
    public static final String sys_role_query = "sys_role_query";
    public static final String sys_src_add = "sys_src_add";
    public static final String sys_src_delete = "sys_src_delete";
    public static final String sys_src_edit = "sys_src_edit";
    public static final String sys_src_query = "sys_src_query";
    public static final String sys_src_grp_add = "sys_src_grp_add";
    public static final String sys_src_grp_delete = "sys_src_grp_delete";
    public static final String sys_src_grp_edit = "sys_src_grp_edit";
    public static final String sys_src_grp_query = "sys_src_grp_query";
    public static final String sys_dept_add = "sys_dept_add";
    public static final String sys_dept_edit = "sys_dept_edit";
    public static final String sys_dept_delete = "sys_dept_delete";
    public static final String sys_dept_query = "sys_dept_query";
    public static final String sys_dic_add = "sys_dic_add";
    public static final String sys_dic_delete = "sys_dic_delete";
    public static final String sys_dic_edit = "sys_dic_edit";
    public static final String sys_dic_query = "sys_dic_query";
    public static final String sys_dic_grp_add = "sys_dic_grp_add";
    public static final String sys_dic_grp_delete = "sys_dic_grp_delete";
    public static final String sys_dic_grp_edit = "sys_dic_grp_edit";
    public static final String sys_dic_grp_query = "sys_dic_grp_query";
    public static final String sys_global_setting = "sys_global_setting";
    public static final String sys_log_query = "sys_log_query";
    public static final String sys_recycle_query = "sys_recycle_query";
    public static final String sys_recycle_delete = "sys_recycle_delete";
    public static void main(String[] args) throws Exception{
        Map kv = new HashMap<>();
        kv.put(sys_user_add,sys_user_add);
        kv.put(sys_user_delete,sys_user_delete);
        kv.put(sys_user_edit,sys_user_edit);
        kv.put(sys_user_query,sys_user_query);
        kv.put(sys_role_add,sys_role_add);
        kv.put(sys_role_delete,sys_role_delete);
        kv.put(sys_role_edit,sys_role_edit);
        kv.put(sys_role_query,sys_role_edit);
        kv.put(sys_src_add,sys_role_edit);
        kv.put(sys_src_delete,sys_role_edit);
        kv.put(sys_src_edit,sys_role_edit);
        kv.put(sys_src_query,sys_role_edit);
        kv.put(sys_src_grp_add,sys_role_edit);
        kv.put(sys_src_grp_delete,sys_role_edit);
        kv.put(sys_src_grp_edit,sys_role_edit);
        kv.put(sys_src_grp_query,sys_role_edit);
        kv.put(sys_dept_add,sys_role_edit);
        kv.put(sys_dept_edit,sys_role_edit);
        kv.put(sys_dept_delete,sys_role_edit);
        kv.put(sys_dept_query,sys_role_edit);
        kv.put(sys_dic_add,sys_role_edit);
        kv.put(sys_dic_delete,sys_role_edit);
        kv.put(sys_dic_edit,sys_role_edit);
        kv.put(sys_dic_query,sys_role_edit);
        kv.put(sys_dic_grp_add,sys_role_edit);
        kv.put(sys_dic_grp_delete,sys_role_edit);
        kv.put(sys_dic_grp_edit,sys_role_edit);
        kv.put(sys_dic_grp_query,sys_role_edit);
        kv.put(sys_global_setting,sys_role_edit);
        kv.put(sys_log_query,sys_role_edit);
        kv.put(sys_recycle_query,sys_role_edit);
        kv.put(sys_recycle_delete,sys_role_edit);
        String ak = AESUtil.byteToHexString(AESUtil.encrypt(JsonUtil.toJson(kv),AuthDef.key16_for_ak.getBytes("utf-8"),AuthDef.iv_16_for_ak));
        System.out.println(ak);
    }
}
