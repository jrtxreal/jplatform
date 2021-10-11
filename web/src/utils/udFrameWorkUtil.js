import udStoreUtil from "./udStoreUtil";
import udMsgUtil from "./udMsgUtil";

export default {
    initFrameWork(session) {
        udStoreUtil.setStore("sessionBo", session);
        const permits = session.permits;
        const permitMap = {};
        permits.forEach(item => {
            const permitSeg = item.split("@");
            if (permitMap[permitSeg[0]]) {
                permitMap[permitSeg[0]].push(permitSeg[1]);
            } else {
                permitMap[permitSeg[0]] = [permitSeg[1]];
            }
        })
        udStoreUtil.setStore("permitMap", permitMap);
    },

    handleEr(vm, e) {
        if (e.code === 3) {
            udMsgUtil.err(vm, e.msg);
        } else {
            console.log("Err:", e.msg);
        }
    },

    checkPermit(permit, deptPath) {
        const isSa = udStoreUtil.getStore("sessionBo.sa");
        if(isSa){
            return true;
        }
        const permitMap = udStoreUtil.getStore("permitMap");
        const deptList = permitMap[permit];
        if (deptList) {
            if(deptPath==="all"){
               return true;
            }else{
                for (let i = 0; i < deptList.length; i++) {
                    const item = deptList[i];
                    if(deptPath.indexOf(item) > -1){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
