export default {
    // 带索引的可序列化树对象
    build(originList, idTag, pidTag, keyTag, orderTag, childrenTag) {
        //1.映射
        const idMap = {};
        const keyMap = {};
        originList.forEach(item => {
            idMap[item[idTag]] = item;
        })
        //2.构建
        const topLevel = [];
        originList.forEach(item => {
            const parent = idMap[item[pidTag]];
            if (parent) {
                if (parent[childrenTag]) {
                    parent[childrenTag].push(item[idTag]);
                } else {
                    parent[childrenTag] = [];
                    parent[childrenTag].push(item[idTag]);
                }
            } else {
                topLevel.push(item[idTag]);
            }
        })
        const collector = [];
        const sortFunc = (tops, collector) => {
            tops.sort((a, b) => {
                if (idMap[a][orderTag] > idMap[b][orderTag]) {
                    return 1;
                } else if (idMap[a][orderTag] == idMap[b][orderTag]) {
                    return 0;
                } else {
                    return -1;
                }
            })
            tops.forEach(item => {
                collector.push(idMap[item]);
                if (idMap[item][childrenTag] && idMap[item][childrenTag].length) {
                    sortFunc(idMap[item][childrenTag], collector)
                }
            })
        }
        sortFunc(topLevel, collector);
        if (keyTag) {
            originList.forEach(item => {
                keyMap[item[keyTag]] = item;
            })
        }
        const roots = [];
        topLevel.forEach(item => {
            roots.push(idMap[item]);
        })
        const obj = {};
        obj.idMap = idMap;
        if (keyTag) {
            obj.keyMap = keyMap;
        }
        obj.orderList = collector;
        obj.topLeaveList = roots;
        return obj;
    },
    // 不带索引的内存树对象
    build2(originList, idTag, pidTag, keyTag, orderTag, childrenTag) {
        //1.映射
        const idMap = {};
        const keyMap = {};
        originList.forEach(item => {
            idMap[item[idTag]] = item;
        })
        //2.构建
        const topLevel = [];
        originList.forEach(item => {
            const parent = idMap[item[pidTag]];
            if (parent) {
                if (parent[childrenTag]) {
                    parent[childrenTag].push(item);
                } else {
                    parent[childrenTag] = [];
                    parent[childrenTag].push(item);
                }
            } else {
                topLevel.push(item);
            }
        })
        const collector = [];
        const sortFunc = (tops, collector) => {
            tops.sort((a, b) => {
                if (idMap[a[idTag]][orderTag] > idMap[b[idTag]][orderTag]) {
                    return 1;
                } else if (idMap[a[idTag]][orderTag] == idMap[b[idTag]][orderTag]) {
                    return 0;
                } else {
                    return -1;
                }
            })
            tops.forEach(item => {
                collector.push(idMap[item[idTag]]);
                if (idMap[item[idTag]][childrenTag] && idMap[item[idTag]][childrenTag].length) {
                    sortFunc(idMap[item[idTag]][childrenTag], collector)
                }
            })
        }
        sortFunc(topLevel, collector);
        if (keyTag) {
            originList.forEach(item => {
                keyMap[item[keyTag]] = item;
            })
        }
        const roots = [];
        topLevel.forEach(item => {
            roots.push(idMap[item[idTag]]);
        })
        const obj = {};
        obj.topLeaveList = roots;
        obj.idMap = idMap;
        if (keyTag) {
            obj.keyMap = keyMap;
        }
        obj.orderList = collector;
        return obj;
    }
}
