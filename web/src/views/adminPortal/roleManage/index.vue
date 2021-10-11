<!--  -->
<template>
  <div class="role-contain">
    <div class="role-left">
      <div class="custom-tree-container">
        <el-tree
          :data="list"
          node-key="id"
          :render-content="renderContent"
          accordion
          :props="defaultProps"
          ref="tree"
          :check-on-click-node="true"
          @node-click="handleNodeClick"
          show-checkbox
        ></el-tree>
      </div>
      <div class="left-buttom">
        <el-button class="role-btn" @click="pro">维护组织</el-button>
      </div>
    </div>

    <el-divider direction="vertical"></el-divider>

    <div class="role-right">
      <role-detail
        v-if="roleId"
        :roleId="roleId"
        :deptId="deptId"
        v-on:setId="setRoleId"
      ></role-detail>

      <role-list v-else v-on:setId="setRoleId" :deptId="deptId"></role-list>

      <!-- <component :is="changeComponent" :roleId="roleId"></component> -->
    </div>
  </div>
</template>

<script>
import RoleEmpty from './empty';
import RoleList from './list/index';
import RoleDetail from './detail/index';

export default {
  components: {
    'role-empty': RoleEmpty,
    'role-list': RoleList,
    'role-detail': RoleDetail,
  },
  data() {
    return {
      // 树结构
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      list: [],
      isDetail: false,
      isEmpty: false,
      comName: '',

      deptId: '',
      roleId: '',
    };
  },

  // 监听属性 类似于data概念
  computed: {
    // 检测到list或id的改变
    changeComponent() {
      let name = '';

      // if (this.list && this.length > 0) {
      name = this.roleId ? 'role-detail' : 'role-list';
      // } else {
      // name = 'role-empty';
      // }

      // debugger;
      return name;
    },
  },

  // 监控data中的数据变化
  watch: {},

  methods: {
    // 自定义DOM树
    renderContent(h, { node, data, store }) {
      return (
        <span class="custom-tree-node">
          {data.children && data.children.length > 0 ? (
            <i class="el-icon-folder-add"></i>
          ) : (
            <i class="el-icon-document"></i>
          )}

          <span>{node.label}</span>
        </span>
      );
    },

    // 获取部门列表
    getDepartList() {
      const data = [
        {
          id: 1,
          label: 'A部门',
          children: [
            {
              id: 4,
              label: 'A1部门',
            },
          ],
        },
        {
          id: 2,
          label: 'B部门',
          children: [
            {
              id: 5,
              label: 'B1部门',
            },
            {
              id: 6,
              label: 'B2部门',
            },
          ],
        },
      ];

      this.list = data;
      this.setRoleId(1);
    },

    // TODO 状态不能正常选中  可能是样式冲突
    // 点击树节点, 清除roleId 返回角色列表页
    handleNodeClick(data) {
      console.log(data);
      // const { id } = data;
    },

    // 通过key获取树节点
    getCheckedKeys() {
      console.log(this.$refs.tree.getCheckedKeys());
    },

    // 通过key设置树节点
    setCheckedKeys(id) {
      this.$refs.tree.setCheckedKeys([id]);
    },

    // 设置所选部门ID 用于请求角色列表
    setDeptId(id) {
      this.deptId = String(id);
    },

    // 设置所选角色ID 用于跳转详情页
    setRoleId(id) {
      this.roleId = String(id);
    },

    pro() {
      console.log('维护组织');
    },
  },

  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {
    this.getDepartList();
  },
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素）
  mounted() {
    // this.setCheckedKeys(1);
  },
  //  更新之前
  beforeUpdate() {},
  //  更新之后
  updated() {},
  //  销毁之前
  beforeDestroy() {},
  //  销毁完成
  destroyed() {},
  //  如果页面有keep-alive缓存功能，这个函数会触发
  activated() {},
};
</script>
<style lang="scss" scoped>
//@import url(); 引入公共css类
@import './index.scss';
</style>
