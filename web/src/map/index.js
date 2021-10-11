
/*系统资源配置表*/
import map_cmn from "./mapSeg/map_cmn";
import map_sys_src from "./mapSeg/map_sys_src"
import map_sys_dept from "./mapSeg/map_sys_dept";
import map_sys_setting from "./mapSeg/map_sys_setting";
import map_sys_user from "./mapSeg/map_sys_user";
import map_sys_role from "./mapSeg/map_sys_role";
import map_sys_log from "./mapSeg/map_sys_log";
import map_sys_dic from "./mapSeg/map_sys_dic";
import map_msg from "./mapSeg/map_msg";
import map_sys_recycle from "./mapSeg/map_sys_recycle"

export default {
  ...map_sys_src,
  ...map_cmn,
  ...map_sys_dept,
  ...map_sys_setting,
  ...map_sys_user,
  ...map_sys_role,
  ...map_sys_log,
  ...map_sys_dic,
  ...map_msg,
  ...map_sys_recycle
};
