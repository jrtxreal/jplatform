<template>
  <div class="dictMgr">
    <template v-if="navList.length == 0">
      <div class="nodata_card">
        <el-button
          icon="el-icon-circle-plus-outline"
          @click="addVisible = true"
        >
          添加字典分组
        </el-button>
<!--        <el-dialog title="添加字典分组" :visible.sync="addVisible" width="500px">-->
<!--          <add-dict @addDictSection="childEvent"></add-dict>-->
<!--        </el-dialog>-->
      </div>
    </template>
    <template v-else>
      <mn-tabs></mn-tabs>
    </template>
  </div>
</template>

<script>
import addDict from './components/AddDict.vue';
import mnTabs from './components/MNTabs.vue';
import {getDictSections} from '../../../api/dict'

export default {
  name: 'SysDic',
  data() {
    return {
      navList: [],
      addVisible: false,
    };
  },
  components: { addDict,mnTabs},
  methods: {
    childEvent: function (value) {
      const vm = this;
      vm.addVisible = false;
      var item = {
        id: vm.navList.length + 1,
        name: value.sectionName,
      };
      vm.navList.push(item);
    },
    getDictSection:function(prma){
      const vm = this;
      getDictSections(prma).then((data=>{
        if(data.code == 0){
          vm.navList = data.result.dicGrpList
        }
      })).catch((error=>{}))
    }
  },
  created:function(){
    const vm = this;
    vm.getDictSection();
  }
};
</script>

<style lang="scss">
  .dictMgr{
    height: 100%;
    display: flex;
    .nodata_card {
      box-sizing: border-box;
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
</style>
