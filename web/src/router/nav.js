import nav_cmn from "./navSeg/nav_cmn"
import nav_la from "./navSeg/nav_la";
import nav_sys from "./navSeg/nav_sys";
import nav_msg from "./navSeg/nav_msg";
export default {
    ...nav_cmn,
    ...nav_la,
    ...nav_sys,
    ...nav_msg
};
