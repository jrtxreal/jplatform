<template>
        <el-tree
            class="filter-tree"
            :data="treeData"
            :props="treeProps"
            :node-key="nodeKey"
            :expand-on-click-node="expandOnClickNode"
            :default-expand-all="defaultExpandAll"
            :current-node-key="currentNodeKey"
            :accordion="accordion"
            :highlight-current="true"
            @node-click="handleNodeClick" @current-change="handleCurrentChange">
                <span slot-scope="{node,data}">
                    <i v-if="!data.children"
                        class = 'el-icon-document'
                        style = "color: #333;" > </i>
                    <i v-else :class="!node.expanded?'el-icon-document-add':'el-icon-document-remove'"
                        style = "color: #333;"></i>
                    <span :title="node.label">{{node.label.length<11?node.label:node.label.substr(0,8)+"..."}}</span >
                </span>
        </el-tree>
</template>
<script>



export default {
    name:'dep-tree',
    props:{
        treeData:{
            type:Array,
            default:[],
        },
        currentNodeKey:{
            type:[String, Number],
            default:[]
        },
        treeProps:{
         type:Object,
         default: [],
        },
        nodeKey:{
            type:String,
            default:""
        },
        expandOnClickNode:{
            type:Boolean,
            default:true
        },
        defaultExpandAll:{
            type:Boolean,
            default:false
        },
        accordion:{
            type:Boolean,
            default:true
        }
    },
    data(){
        return{
            departmentTreeData:[{
                value:'1',
                label: 'A部门',
                children: [{
                    value:'1-1',
                    label: 'A1部门',
                },{
                    value:'1-2',
                    label: 'A2部门',
                },{
                    value:'1-3',
                    label: 'A3部门',
                },{
                    value:'1-4',
                    label: 'A4部门',
                }]
            },{
                value:'2',
                label: 'B部门',
                children: [{
                    value:'2-1',
                    label: 'B1部门',
                },{
                    value:'2-2',
                    label: 'B2部门',
                }]
            },{
                value:'3',
                label: 'C部门',
                children: [{
                    value:'3-1',
                    label: 'C1部门',
                },{
                    value:'3-2',
                    label: 'C2部门',
                }]
            }],
            // defaultProps: {
            //     children: 'children',
            //     label: 'name',
            // },
        }
    },
    mounted(){

    },
    created() {
        const vm =this
    },
    methods:{
        handleCurrentChange(data){
            this.$emit('current-change',data)
        },
        handleNodeClick(data) {
            this.$emit('selectNode',data)
        },

    }
}
</script>
