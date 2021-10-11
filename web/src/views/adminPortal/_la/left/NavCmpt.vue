<template>
  <el-menu
    :default-openeds="['sys']"
    :default-active="activeTab"
    @select="select"
    class="el-menu-vertical-demo"
  >
    <!--        <el-menu-item :index="nav[map.index].name">-->
    <!--            <i class="el-icon-s-home"></i>-->
    <!--            <span slot="title">{{nav[map.index].title}}</span>-->
    <!--        </el-menu-item>-->
    <el-submenu index="sys">
      <template slot="title">
        <i class="el-icon-setting"></i>
        <span>系统管理</span>
      </template>

      <el-menu-item v-if="checkPermit(map.sys_user_query)" :index="nav[map.sys_user_query].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_user_query].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="checkPermit(map.sys_role_query)" :index="nav[map.sys_role_query].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_role_query].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="sa"  :index="nav.sys_src_query.name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_src_query].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="checkPermit(map.sys_dept_query)" :index="nav[map.sys_dept_query].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_dept_query].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="sa" :index="nav[map.sys_dic_query].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_dic_query].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="checkPermit(map.sys_global_setting)"  :index="nav[map.sys_global_setting].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_global_setting].title }}</span>
      </el-menu-item>
      <el-menu-item v-if="checkPermit(map.sys_log_query)" :index="nav[map.sys_log_query].name">
        <i class="el-icon-document"></i>
        <span>{{ nav[map.sys_log_query].title }}</span>
      </el-menu-item>
    </el-submenu>
<!--    <el-menu-item v-if="checkPermit(map.sys_recycle_query)" :index="nav[map.sys_recycle_query].name">-->
<!--      <i class="el-icon-document"></i>-->
<!--      <span>{{ nav[map.sys_recycle_query].title }}</span>-->
<!--    </el-menu-item>-->
    <!--        <el-menu-item :index="nav.msg_tpl_query.name">-->
    <!--            <i class="el-icon-s-promotion"></i>-->
    <!--            <span slot="title">{{nav.msg_tpl_query.title}}</span>-->
    <!--        </el-menu-item>-->
  </el-menu>
</template>

<script>
import nav from '../../../../router/nav';
import map from '../../../../map';
import udStoreUtil from '../../../../utils/udStoreUtil';
import udFrameWorkUtil from "../../../../utils/udFrameWorkUtil";

export default {
  name: 'NavCmpt',
  data() {
    return {
      nav,
      map,
    };
  },
  computed: {
    sa:{
      get() {
        console.log(udStoreUtil.getStore('sessionBo.sa'));
        return udStoreUtil.getStore('sessionBo.sa');
      },
      set(val) {
        udStoreUtil.setStore('sessionBo.sa', val);
      },
    },
    activeTab: {
      get() {
        return udStoreUtil.getStore('nav.activeTab');
      },
      set(val) {
        udStoreUtil.setStore('nav.activeTab', val);
      },
    },
    navTabs: {
      get() {
        return udStoreUtil.getStore('nav.navTabs');
      },
      set(val) {
        udStoreUtil.getStore('nav.navTabs', val);
      },
    },
  },
  watch: {
    watch: {
      activeTab() {
        console.log('activeTab发生了变化');
      },
      navTabs() {
        console.log('navtabs发生了变化');
      },
    },
  },
  methods: {
    checkPermit(permit){
      return udFrameWorkUtil.checkPermit(permit,"all");
    },
    select(index) {
      const vm = this;
      const navTabs = vm.navTabs;
      let include = false;
      navTabs.forEach(item => {
        if (item.name === index) {
          include = true;
          vm.activeTab = index;
        }
      });
      if (!include) {
        navTabs.push(nav[index]);
        vm.navTabs = navTabs;
        vm.activeTab = index;
      }
    },
  },
};
</script>

<style scoped></style>
