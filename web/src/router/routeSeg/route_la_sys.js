import map from "../../map";
import nav from "../nav";
import SysDic from "../../views/adminPortal/dic/SysDic";
import SysDept from "../../views/adminPortal/dept/SysDept";
import SysLog from "../../views/adminPortal/log/SysLog";
import SysRole from "../../views/adminPortal/role/SysRole";
import SysUser from "../../views/adminPortal/user/SysUser";
import GlobalSetting from "../../views/adminPortal/setting/GlobalSetting";
import SysSrc from "../../views/adminPortal/src/SysSrc";
import SysRecycle from "../../views/adminPortal/recycle";


export default [{
    name: map.sys_dic_query,
    path: nav[map.sys_dic_query].url,
    component: SysDic,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_dept_query,
    path: nav[map.sys_dept_query].url,
    component: SysDept,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_log_query,
    path: nav[map.sys_log_query].url,
    component: SysLog,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_role_query,
    path: nav[map.sys_role_query].url,
    component: SysRole,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_user_query,
    path: nav[map.sys_user_query].url,
    component: SysUser,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_global_setting,
    path: nav[map.sys_global_setting].url,
    component: GlobalSetting,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_src_query,
    path: nav[map.sys_src_query].url,
    component: SysSrc,
    meta: {
        keepAlive: true
    }
}, {
    name: map.sys_recycle_query,
    path: nav[map.sys_recycle_query].url,
    component: SysRecycle,
    meta: {
        keepAlive: true
    }
}]
