import map from "../../map";
import nav from "../nav";
import Login from "../../views/Login"
import _404 from "../../views/404"
import Index from "../../views/Index";
export default [
    {
        name: map.login,
        path: nav[map.login].url,
        component: Login,
    },
    {
        name: map._404,
        path: nav[map._404].url,
        component: _404,
    },
    {
        path: '*',
        redirect: nav[map._404].url,
    },
]
