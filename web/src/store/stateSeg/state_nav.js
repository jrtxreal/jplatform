import nav from "../../router/nav";
import map from "../../map"
export default {
   activePath:null,
   activeCmpt:null,
   activeTab:nav[map.sys_user_query].name,
   navTabs:[nav[map.sys_user_query]],
   currentRoutePath:null,
   currentCmptPath:null,
   cached:[]
}
