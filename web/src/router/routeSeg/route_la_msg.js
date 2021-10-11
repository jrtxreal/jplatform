import map from "../../map";
import nav from "../nav";
import MsgTpl from "../../views/adminPortal/msg/MsgTpl";
export default [{
    name: map.msg_tpl_query,
    path: nav[map.msg_tpl_query].url,
    component: MsgTpl,
    meta:{
        keepAlive:true
    }
}]
