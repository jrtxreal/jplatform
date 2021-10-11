import key from "../def/key"
export default {
    save(state, obj) {
        let key = obj.key;
        if (key.indexOf(".") === -1) {
            state[obj.key] = obj.val;
        } else {
            let items = key.split(".");
            let val = null;
            let index = 0;
            items.forEach(item => {
                if (index === 0) {
                    if (!state[item]) {
                        state[item] = {}
                    }
                    val = state[item]
                } else if (index === items.length - 1) {
                    val[item] = obj.val;
                } else {
                    if (!val[item]) {
                        val[item] = {}
                    }
                    val = val[item]
                }
                index++;
            })
        }
    },
    reset(state){
        for(let key in state){
         state[key] = null;
        }
        let r;
        const initState = (r=sessionStorage.getItem(key.initStateJson))?JSON.parse(r):{};
        for(let key in initState){
            state[key] = initState[key];
        }
    }
}
