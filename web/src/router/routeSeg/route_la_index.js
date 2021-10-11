import map from "../../map";
import nav from "../nav";
import Index from "../../views/Index";

export default [{
        name: map.index,
        path: nav[map.index].url,
        component: Index,
        meta:{
            keepAlive:true
        }
    }]
