import map from "../../map";
import nav from "../nav";
import LayoutWrapper from "../../views/adminPortal/_la/LayoutWrapper";
import route_la_index from "./route_la_index";
import route_la_msg from "./route_la_msg";
import route_la_sys from "./route_la_sys";
export default [
    {
        name: map.base,
        path: nav[map.base].url,
        component: LayoutWrapper,
        redirect:{name:map.sys_user_query},
        children:[
            ...route_la_index,
            ...route_la_msg,
            ...route_la_sys
        ]
    },
]
