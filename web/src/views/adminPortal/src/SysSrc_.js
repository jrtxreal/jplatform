import groupSrc from './GroupSrc'
import srcManage from "../../../api/srcManage";
import map from '../../../map'

export default {
    name: map.sys_src_query,
    components: {groupSrc},
    data() {
        return {
            isNews: 3,// 1表示资源分组数据界面  2代表添加分组界面  3代表分组用户维护界面
            isPage: 1,
            editGroup: false,//点击资源分组维护跳转
            dialogFormVisible: false,
            centerDialogVisible: false,
            dialogFormVisibleContent: false,
            dialogFormMove: false,
            isNewsT: false,
            formLabelWidth: '100px',
            sectionList: [],
            groupContent: {},
            contentID: '',
            // childByHint:this.srcList,//传入子组件数据
            form: {
                name: '',
                style: '',
                num: 0,
            },
            formContent: {
                name: '',
                code: '',
                ord: '',
                src: ''
            },
            formDetail: {
                name: '',
                des: '',
                deprecated: '',
                code: '',
                ord:0,
                createTime: '',
                lastUpdate: '',
                updateBy: ''

            },
            tabPosition: 'left',
            srcList: [],//资源分组列表数据
            // {id: 1, name: '资源分组A',}, {id: 2, name: '资源分组AAB',}, {id: 3, name: '资源分组BBA'}
            tableData: [],//分组内容数据
            statusType: 0,
            message: '',
            selectContent: [],//分组内容已选择数据
            moveSection: '',//移动分组默认显示组
            butStatus: true,//默认显示true为编辑 （false 显示取消）
            readOnly: true,//默认只读
            srcAllHint: false,//组件切换
            inputSrcName: '',//搜索框内容
            groupRules: {},
            contentRules: {
                name: [
                    {required: true, message: '分组名称不能为空', trigger: 'blur'},
                ],
                code: [
                    {required: true, message: '编号不能为空', trigger: 'blur'},
                ],
            },
        }
    },
    computed: {},
    watch: {},
    mounted() {
    },
    created() {
        const vm = this
        vm.getSrcGroupList()

    },
    methods: {
        addGroup() {
            const vm = this;
            vm.dialogFormVisible = true;
        },
        addSave: function (e) {
            const vm = this
            // vm.dialogFormVisible = false
            // vm.srcList.push(vm.form)
            vm.$refs[e].validate((valid) => {
                if (valid) {
                    vm.getGroupAdd()
                } else {
                    return false;
                }
            });
        },
        getGroupAdd: function () {
            const vm = this
            srcManage.srcGroupAdd({
                "name": vm.form.name,
                "ord": vm.form.num
            }).then(data => {
                if (data.code == 0) {
                    vm.showMessage("资源分组添加成功", 'success')
                    vm.isNews = 1
                    vm.dialogFormVisible = false
                    vm.form.name = ''
                    vm.form.num = ''
                    vm.getSrcGroupList()
                }
            }).catch(err => {
                vm.showMessage(err, 'warning')
            })
        },
        //资源分组list
        getSrcGroupList: function () {
            const vm = this
            // isNull 2代表没有分组数据标记  1 代表存在分组数据
            let isNull = 1
            if (vm.srcList.length == 0) {
                isNull = 2
            }
            srcManage.srcGroup({}).then(data => {
                if (data.code == 0) {
                    vm.srcList = data.result
                    if (vm.srcList.length > 0) {
                        if (isNull == 2) {
                            vm.isNews = 1
                            vm.moveSection = vm.srcList[0].id
                            vm.statusType = vm.srcList[0].id
                            vm.groupContent = vm.srcList[0]
                            vm.getSrcGroupContent(vm.statusType)
                        } else {
                            vm.getSrcGroupContent(vm.statusType)
                        }
                    } else {
                        vm.isNews = 3
                    }
                }
            }).catch(data => {
                console.log("err错误提getSrcGroupList", data)

            })
        },
        //资源分组表格内容
        getSrcGroupContent: function (e) {
            const vm = this
            srcManage.srcGroupContent({'srcGrpId': e}).then(data => {
                vm.tableData = data.result
                vm.tableData.forEach(item =>{
                    item.deprecated = item.deprecated ? "1" : "2"
                })
            }).catch(err => {
                console.log("err错误提示getSrcGroupContent", err)
            })
        },
        butItemSection: function (e) {
            const vm = this
            vm.isNewsT = false
            vm.groupContent = e
            vm.statusType = e.id
            vm.getSrcGroupContent(vm.statusType)
            vm.moveSection = e.id
        },
        //分组内部表格详情
        detailClick: function (e) {
            const vm = this
            vm.isNewsT = true
            // e.deprecated = e.deprecated ? "1" : "2"
            vm.formDetail=e
        },
        //分组内部表格数据删除事件
        deleteClick: function (e) {
            const vm = this
            vm.contentID = e.id
            vm.centerDialogVisible = false
            vm.message = '确认删除此资源？'
            vm.centerDialogVisible = true
        },
        //分组内容
        getGroupContentDelete: function () {
            const vm = this
            srcManage.srcGroupContentDel({"srcId": vm.contentID}).then(data => {
                if (data.code == 0) {
                    vm.showMessage("数据删除成功", 'success')
                    vm.getSrcGroupContent(vm.statusType)
                }
            }).catch(err => {
                vm.showMessage(err, 'warning')
                console.log(err)
            })

        },
        //资源数据多选事件
        selectChange: function (e) {
            const vm = this
            let ids = ''
            if (e.length > 0) {
                e.forEach(item => {
                    ids = ids + ',' + item.id
                })
            }
            vm.selectContent = ids.slice(1)
            // vm.selectContent = e


        },
        //提示信息确认
        messageClick: function () {
            const vm = this
            vm.centerDialogVisible = false
            if (vm.message == '确认删除此资源？') {
                vm.getGroupContentDelete()
            }
        },
        //移动资源数据
        moveContent: function () {
            const vm = this
            if (vm.selectContent.length > 0) {
                vm.dialogFormMove = true

            } else {
                vm.showMessage("请选择要移动的资源！", 'warning')
                // vm.message = '请选择要移动的资源？'
                // vm.centerDialogVisible = true
            }

        },
        getGroupContentMove: function () {
            const vm = this
            if (vm.moveSection == vm.statusType) {
                vm.showMessage("资源已存在当前分组", 'warning')
            } else {
                srcManage.srcGroupContentMove({
                    "sourceSrcGrpId": vm.statusType,
                    "destSrcGrpId": vm.moveSection,
                    "srcIds": vm.selectContent,
                }).then(data => {
                    if (data.code == 0) {
                        vm.showMessage("移动分组成功", 'success')
                        vm.getSrcGroupContent(vm.statusType)
                    }

                }).catch(err => {
                    vm.showMessage(err, 'warning')
                    console.log(err)
                })
            }
        },
        //添加资源内容数据事件
        addContent: function () {
            const vm = this
            // vm.dialogFormVisible = true
            vm.formContent.ord = vm.tableData.length + 1;
            vm.dialogFormVisibleContent = true
        },
        //搜索框查询事件
        searchSrcName: function () {
            const vm = this
            vm.getSrcGroupSearchContent()

        },
        getSrcGroupSearchContent: function (e) {
            const vm = this
            srcManage.srcGroupContent({'srcGrpId': vm.statusType, "srcName": vm.inputSrcName}).then(data => {
                vm.tableData = data.result

            }).catch(err => {
                console.log("err错误提示getSrcGroupContent", err)
            })
        },
        //搜索资源内容数据
        getSearchSrcContent: function () {

        },
        //移动分组
        moveClick: function () {
            const vm = this
            vm.dialogFormMove = false
            vm.getGroupContentMove()
        },
        //资源分组维护
        sectionMaintainClick: function () {
            const vm = this
            vm.editGroup = true
            vm.srcAllHint = true
        },
        //资源分组维护编辑事件
        editClick: function () {
            const vm = this
            vm.butStatus = !vm.butStatus
            vm.readOnly = !vm.readOnly
        },

        addContentSave: function (e) {
            const vm = this
            vm.$refs[e].validate((valid) => {
                if (valid) {
                    vm.getSrcGroupContentAdd()
                } else {
                    return false;
                }
            });
        },
        //添加资源分组内容
        getSrcGroupContentAdd: function () {
            const vm = this
            srcManage.srcGroupContentAdd({
                "name": vm.formContent.name,
                "code": vm.formContent.code,
                "des": vm.formContent.src,
                "ord":vm.formContent.ord,
                "deprecated": false,
                "srcGrpId": vm.statusType
            }).then(data => {
                if (data.code == 0) {
                    vm.showMessage('资源数据添加成功', 'success')
                    vm.dialogFormVisibleContent = false
                    vm.formContent.src = ''
                    vm.formContent.code = ''
                    vm.formContent.name = ''
                    vm.getSrcGroupContent(vm.statusType)
                }
            }).catch(err => {
                vm.showMessage(err, 'warning')
            })
        },
        //资源分组维护保存事件
        saveClick: function () {
            const vm = this
            if(vm.butStatus==false){
                vm.getEdtSave()
            }
        },
        //添加分组点击事件
        addSectionClick: function () {
            const vm = this
            vm.form.num =vm.srcList.length + 1
            vm.dialogFormVisible = true

        },
        //资源分组内容编辑保存
        getEdtSave: function () {
            const vm = this
            srcManage.srcGroupContentEdt({
                "id": vm.formDetail.id,
                "name": vm.formDetail.name,
                "code": vm.formDetail.code,
                "des": vm.formDetail.des,
                "ord":vm.formDetail.ord,
                "deprecated": vm.formDetail.deprecated == "1" ? true : false
            }).then(data => {
                if (data.code == 0) {
                    vm.butStatus = !vm.butStatus
                    vm.readOnly = !vm.readOnly
                    vm.showMessage('资源数据编辑成功', 'success')
                }
            }).catch(err => {
                vm.showMessage(err, 'warning')
                console.log(err)

            })
        },
        //资源分组维护返回页面事件
        backPage: function () {
            const vm = this
            vm.isNewsT = false

        },
        //无数据情况-资源分组维护点击事件
        srcManageClick: function () {
            const vm = this
            vm.isNews = 2
        },

        //子组件传值
        childByHint: function (e) {
            const vm = this
            vm.$nextTick(() => {
                vm.srcAllHint = e
                vm.editGroup = false
                // vm.getSrcGroupList()
                if (vm.srcList.length > 0) {
                    vm.isNews = 1
                    // vm.moveSection = vm.srcList[0].id
                } else {
                    vm.isNews = 3
                }

            })
        },
        refOn:function(e){
            const vm =this
            vm.getSrcGroupList()
        },
        listRule: function (e) {
            const vm = this
            let statusT = 1
            vm.getSrcGroupList()
            //判断删除完后进入资源分组添加首页
            if (vm.srcList.length == 1) {
                vm.childByHint(false)
            }
            //判断删除资源分组默认显示下一个
            vm.srcList.forEach((item, index) => {
                if (item.id == e.id) {
                    if (vm.srcList.length - 1 == index) {
                        statusT = vm.srcList[0].id
                        vm.groupContent = vm.srcList[0]
                    } else {
                        statusT = vm.srcList[index + 1].id
                        vm.groupContent = vm.srcList[index + 1]
                    }
                }
            })
            vm.statusType = statusT
        },
        showMessage: function (message, type) {

            this.$message({
                message: message,
                type: type
            });
        },

    },


}
