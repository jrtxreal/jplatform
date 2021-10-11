import Vue from 'vue'
import Vuex from 'vuex'
import persistedstate from "vuex-persistedstate"
import mut from "./mut";
Vue.use(Vuex)
import state_la from "./stateSeg/state_la";
import state_src from "./stateSeg/state_src";
import state_session from "./stateSeg/state_session"
import key from "../def/key";
import state_nav from "./stateSeg/state_nav";
import state_dept from "./stateSeg/state_dept";
import state_dic from "./stateSeg/state_dic";
import state_user from "./stateSeg/state_user";
/*状态机*/
const state = {
    permitMap:{},
    la: {...state_la},
    src: {...state_src},
    sessionBo: {...state_session},
    nav:{...state_nav},
    dept:{...state_dept},
    dic:{...state_dic},
    user:{...state_user},
}
window.sessionStorage.setItem(key.initStateJson, JSON.stringify(state));
const plugin_storage = persistedstate({storage: window.sessionStorage});
const store = new Vuex.Store({
    state,
    mutations: {save:mut.save,reset:mut.reset},
    plugins: [plugin_storage]
})

export default store
