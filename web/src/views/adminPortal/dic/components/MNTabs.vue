<template>
  <div class="mntabs">
    <div class="container">
      <div class="left">
        <div class="navHeader">
          <el-button-group style="display: flex; padding-bottom: .5rem;padding-right: 10px;">
              <el-button  style="flex-grow: 1" @click="addButton" icon="el-icon-plus">添加分组</el-button>
              <el-button  style="flex-grow: 1" :type="editMode?'primary':'normal'" @click="testButton" icon="el-icon-edit">编辑分组</el-button>
          </el-button-group>
        </div>
        <div class="nav-footer-wrapper">
          <div class="navfloter">
            <template v-for="(item, index) in mnTabList">
              <div :key="index"  @click="tabButtion(index)"  :class="{navTtem:true,active: index ===  currentTab}">
                <i style="padding-top:9px; padding-right:.5rem; color: darkgray;" class="el-icon-s-tools"></i>
                {{ item.name }}
              </div>
            </template>
          </div>
        </div>
      </div>
      <div class="right">
        <template v-if="editMode">
          <!-- 字典分组编辑 -->
          <dic-section  :sectionInfo="currentItem"></dic-section>
        </template>
        <template v-else>
          <!-- 字典详情列表 -->
          <dict-details :itemInfo="currentItem"></dict-details>
        </template>
      </div>
    </div>
    <el-dialog title="添加字典分组" :visible.sync="addVisible" width="400px">
      <add-dict @addDictSection="childEvent" v-if="addVisible"></add-dict>
    </el-dialog>
  </div>
</template>

<script>
import dictDetails from './DictDetails.vue';
import dicSection from './SysDictSection.vue';
import addDict from './AddDict.vue';
import {getDictSections} from '../../../../api/dict';
import udStoreUtil from "../../../../utils/udStoreUtil";

export default {
  name: 'mntabs',
  components: { dictDetails,dicSection, addDict },
  data() {
    return {
      isAdd: false,
      addVisible:false,
      currentTab:0,
      currentItem:{},
      mnTabList:[]
    };
  },
  computed:{
    editMode:{
      get(){
        return udStoreUtil.getStore("dic.goList.editMode")
      },
      set(val){
        udStoreUtil.setStore("dic.goList.editMode",val)
      }
    },
    addDicVo:{
      get(){
        return udStoreUtil.getStore("dic.goAdd.dicGrpVo")
      },
      set(val){
        udStoreUtil.setStore("dic.goAdd.dicGrpVo",val)
      }
    },
    refresh:{
      get(){
        return udStoreUtil.getStore("dic.goList.refresh")
      }
    },
  },
  watch:{
    refresh(n,o){
      const vm = this;
      vm.getDictSection();
    }
  },
  created:function(){
    const vm = this;
    vm.getDictSection();
  },
  methods: {
    addButton: function () {
      //添加字典分组
      const vm = this;
      const dicVo = {
        name:'',
        ord:'',
        exclusive:false
      };
      dicVo.ord = vm.mnTabList.length + 1;
      vm.addDicVo = dicVo;
      vm.addVisible = true;
    },
    //字典分组维护
    testButton: function () {
      const vm = this;
      vm.currentItem = vm.mnTabList[vm.currentTab];
      vm.editMode = true;
    },
    //添加字典分组
    childEvent: function (value) {
      const vm = this;
      vm.addVisible = false;
      vm.getDictSection();
    },
    //左侧分组点击事件
    tabButtion:function(index){
      const vm = this;
      //更换当前页面上的数据
      vm.currentTab = index;
      vm.currentItem = vm.mnTabList[index];
    },
    getDictSection:function(prma){
      const vm = this;
      getDictSections(prma).then((data=>{
        //console.log(data);
        if(data.code == 0){
          vm.mnTabList = data.result.dicGrpList
          const select = vm.mnTabList[vm.currentTab];
          if(select){
            vm.currentItem = select;
          }else{
            vm.currentTab = 0;
            vm.currentItem = vm.mnTabList[vm.currentTab];
          }
        }
      })).catch((error=>{
        console.log(error);
      }))
    }
  }
};
</script>

<style lang="scss">
.mntabs {
  height: calc(100% - 20px);
  width: calc(100% - 20px);
  margin-top: 10px;
  margin-left: 10px;
  display: flex;
  .container{
    height: 100%;
    width: 100%;
    display: flex;
    .left{
      width: 230px;
      border-right: 2px dashed darkgray;
      display: flex;
      flex-direction: column;
      align-items: stretch;
      .navHeader{
        .el-button{
          padding: 12px 15px;
        }
      }
      .nav-footer-wrapper{
        padding-right: 2px;
        flex-grow: 1;
        overflow: scroll;
        scrollbar-width:thin;
        .navfloter{
          border: 1px solid rgba(0,0,0,0.12);
          .active{
            background-color: #e0f2f1 !important;
          }
          .navTtem{
            box-sizing: border-box;
            display: flex;
            height: 48px;
            font-size: 14px;
            line-height: 32px;
            padding: 8px 16px;
            border-bottom: 1px solid rgba(0,0,0,0.12);
            transition: color 0.3s,background-color 0.3s;
            color: #606266;
          }
          .navTtem:last-child{
            border-bottom: none;
          }
          .navTtem:hover{
            background-color: #e4e4e4;
          }
        }
      }
    }
    .right{
      .section{
        padding: .5rem;
      }
      scrollbar-width:thin;
      flex-grow: 1;
      .el-table__body-wrapper{
        scrollbar-width:thin;
      }
    }
  }
}
</style>
