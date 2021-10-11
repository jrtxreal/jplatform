<!-- 搜索 -->
<template>
  <div class="search">
    <div class="res-breadcrumb">
      <span class="br-box" @click="goback">
        <i class="el-icon-arrow-left"></i>
        <span class="br-name">返回</span>
        <span class="br-name">{{ `搜索：${searchText}` }}</span>
      </span>
    </div>

    <el-table
      ref="multipleTable"
      :data="list"
      tooltip-effect="dark"
      style="width: 100%"
      @row-click="row => forward(row)"
      highlight-current-row
      :row-class-name="tableRowClassName"
    >
      <el-table-column align="left" width="55px">
        <template slot-scope="scope">
          <el-checkbox
            v-if="!scope.row.type !== 'user'"
            :checked="selectKeys.indexOf(scope.row.id) != -1"
            @change="(status, event) => handleChange(scope.row, status, event)"
          ></el-checkbox>
        </template>
      </el-table-column>
      <el-table-column
        prop="name"
        label="名称"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="fullPath"
        label="所属"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="userName"
        label="账号"
        show-overflow-tooltip
      ></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    searchText: String,
    list: Array,
    selectKeys: Array,
  },
  components: {},
  data() {
    return {};
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  methods: {
    // row-class-name
    tableRowClassName({ row }) {
      if (row.type !== 'user') {
        return 'curOption';
      }
    },

    goback() {
      this.$emit('back');
    },

    forward(row) {
      // 返给父组件，去切换Path
      if (row.typw !== 'user') {
        this.$emit('toPathFn', forward, row);
      }
    },

    handleChange(row, status, event) {
      // 返给父组件 - 所选的人
      if (event) {
        event.stopPropagation();
        event.preventDefault();
      }

      // 父组件
      this.$emit('selectFn', row, status, event);
    },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {},
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素，但mounted不能保证所有的子组件都挂载成功）
  mounted() {},
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

.res-breadcrumb {
  height: auto;
  line-height: 30px;
  margin-bottom: 6px;
  display: flex;
  flex-flow: row wrap;
  box-sizing: border-box;
  font-size: 14px;

  .br-box {
    display: flex;
    flex-direction: row;
    align-items: center;
  }

  .br-name {
    margin-left: 4px;
    cursor: pointer;
  }
}
</style>
