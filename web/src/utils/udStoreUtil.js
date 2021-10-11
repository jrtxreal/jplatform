import store from "../store";

export default {
    resetStore() {
        store.commit('reset');
    },
    setStore(key, val) {
        const obj = { key: key, val: val };
        store.commit('save', obj);
    },
    getStore(key) {
        if (key.indexOf('.') == -1) {
            return store.state[key];
        } else {
            let index = 0;
            let val = null;
            key.split('.').forEach(item => {
                if (index == 0) {
                    val = store.state[item];
                } else {
                    val = val[item];
                }
                index++;
            });
            return val;
        }
    }
}
