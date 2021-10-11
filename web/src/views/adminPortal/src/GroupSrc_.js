import srcManage from "../../../api/srcManage";
import map from "../../../map";
import {formatDate} from "element-ui/src/utils/date-util";

export default {
    name: "GroupSrc",
    props: {
        contentSrc:{
            type: Object,
            default: function () {
                return []
            }
        }
    },
    data() {
        return {
            isNews: 3,
            srcList: [],//资源分组列表数据
            tableData: [],//分组内容数据
            form: {
                name: '',
                past: '',
                num: '',
            },
            formLabelWidth: '120px',
            butStatus: true,//默认显示true为编辑 （false 显示取消）
            // statusType: 1,
            // moveSection:'',
            formDetailG: {
                name: '',
                ord: '',
                createTime: '',
                lastUpdate: '',
                updateBy: '',
            },
            message: '',
            readOnlyG: true,
            dialogFormVisibleG: false,
            centerDialogVisibleG: false,
        }
    },
    computed: {},
    watch: {
        // vm.formDetailG = vm.contentSrc
        contentSrc: {
            handler: function (value, old) {
                this.formDetailG = this.contentSrc
            },
            immediate: true, //关键
            deep: true,
        }
    },
    mounted() {
    },
    created() {
        const vm = this
        // vm.getSrcGroupListG(1)

            // if (vm.srcList.length > 0) {
            // vm.statusType = vm.srcList[0].id
            // }\
        // if (vm.srcList.length > 0) {
        //     vm.isNews = 1
        //     vm.moveSection = vm.srcList[0].id
        // }

    },
    methods: {
        //资源分组点击事件
        butItemSectionG: function (e) {
            const vm = this
            // vm.statusType = e.id
            // vm.moveSection = e.id
            // console.log("资源组数据-点击数据显示", e)

        },
        //添加分组点击事件
        addSectionClick: function () {
            const vm = this
            vm.dialogFormVisibleG = true

        },
        //获取分组数据 index 1 表示第一次获取， 2表示不是第一次获取
        // getSrcGroupListG: function (index) {
        //     const vm = this
        //     srcManage.srcGroup({}).then(data => {
        //         vm.srcList = data.result
        //         if (vm.srcList.length > 0) {
        //             vm.isNews = 1
        //             vm.moveSection = vm.srcList[0].id
        //             if (index == 1) {
        //                 vm.statusType = vm.srcList[0].id
        //                 vm.formDetailG = vm.srcList[0]
        //             }
        //             // vm.getSrcGroupContent(vm.statusType)
        //         }
        //     }).catch(data => {
        //         console.log("err错误提getSrcGroupList", data)
        //
        //     })
        //
        // },
        //资源分组添加
        // getGroupAdd: function () {
        //     const vm = this
        //     srcManage.srcGroupAdd({
        //         "name": vm.form.name,
        //         "ord": vm.form.num
        //     }).then(data => {
        //         if (data.code == 0) {
        //             vm.showMessageG("资源分组添加成功", 'success')
        //             vm.dialogFormVisibleG = false
        //             vm.form.name = ''
        //             vm.form.num = ''
        //             if(vm.srcList.length > 0) {
        //                 vm.getSrcGroupListG(2)
        //             }
        //             else {
        //                 vm.getSrcGroupListG(1)
        //             }
        //
        //         }
        //
        //     }).catch(err => {
        //         vm.showMessageG(err, 'warning')
        //     })
        //
        // },

        //添加分组保存事件
        addSave: function () {
            const vm = this
            vm.getGroupAdd()
            // vm.dialogFormVisibleG = false
            // vm.showMessageG("资源分组添加成功", 'success')
        },
        //编辑分组资源事件
        editClick: function () {
            const vm = this
            vm.butStatus = !vm.butStatus
            vm.readOnlyG = !vm.readOnlyG

        },
        //编辑分组资源
        getGroupEdit: function () {
            const vm = this
            srcManage.srcGroupEdit({
                "id": vm.formDetailG.id,
                "name": vm.formDetailG.name,
                "ord": vm.formDetailG.ord
            }).then(data => {
                if (data.code == 0) {
                    vm.butStatus = !vm.butStatus
                    vm.readOnlyG = !vm.readOnlyG
                    vm.showMessageG("资源分组保存成功", 'success')
                    vm.$emit('groupListRef')
                    // vm.getSrcGroupListG(2)
                }
            }).catch(err => {
                vm.showMessageG(err, 'warning')

            })
        },
        //保存分组资源
        saveClick: function () {
            const vm = this
            vm.getGroupEdit()
            // vm.butStatus = !vm.butStatus
            // vm.readOnlyG = !vm.readOnlyG
            // vm.showMessageG("资源分组保存成功", 'success')
        },
        //删除分组资源
        deleteClick: function () {
            const vm = this
            vm.message = '是否确认删除此资源分组？'
            vm.centerDialogVisibleG = true

        },
        backPageG: function () {
            const vm = this
            vm.$emit('childHint', false)

        },
        //资源分组删除
        getGroupDelete: function () {
            const vm = this
            srcManage.srcGroupDelete({
                "srcGrpId": vm.formDetailG.id,
            }).then(data => {
                if (data.code == 0) {
                    vm.centerDialogVisibleG = false
                    vm.showMessageG("资源分组删除成功", 'success')
                    vm.$emit('sectionListRule', vm.formDetailG)
                    // let statusT = 1
                    // //判断删除完后进入资源分组添加首页
                    // if(vm.srcList.length==1){
                    //   vm.backPageG()
                    // }
                    // //判断删除资源分组默认显示下一个
                    //     vm.srcList.forEach((item, index) => {
                    //         if (item.id == vm.formDetailG.id) {
                    //             if (vm.srcList.length - 1 == index) {
                    //                 statusT = vm.srcList[0].id
                    //                 vm.formDetailG = vm.srcList[0]
                    //             } else {
                    //                 statusT = vm.srcList[index + 1].id
                    //                 vm.formDetailG = vm.srcList[index + 1]
                    //             }
                    //         }
                    //
                    //     })
                    //
                    // vm.formDetailG.id = statusT
                }

            }).catch(err => {
                vm.showMessageG(err, 'warning')

            })

        },
        //资源分组-数据确认删除事件
        messageClickG: function () {
            const vm = this
            vm.getGroupDelete()
            // vm.showMessageG("资源分组删除成功", 'success')
            // vm.centerDialogVisibleG = false
            // let statusT = 1
            // vm.srcList.forEach((item, index) => {
            //     if (item.id == vm.statusType) {
            //         if (vm.srcList.length - 1 == index) {
            //             statusT = vm.srcList[0].id
            //             vm.formDetailG = vm.srcList[0]
            //         } else {
            //             statusT = vm.srcList[index + 1].id
            //             vm.formDetailG = vm.srcList[index + 1]
            //         }
            //
            //     }
            //
            // })
            // vm.statusType = statusT
            // console.log("11111111", vm.formDetailG, vm.statusType, vm.srcList.length)

        },
        //无数据情况-资源分组维护点击事件
        // srcManageClickG: function () {
        //     const vm = this
        //     vm.isNews = 2
        // },
        showMessageG: function (message, type) {
            this.$message({
                message: message,
                type: type
            });
        },

    },

}
