<!--  -->
<template>
  <div class="">
    <el-table
      :data="tableData"
      stripe
      style="width: 100%"
      border
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
      height="600"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="fileName" label="文件名"></el-table-column>
      <el-table-column prop="fullPath" label="文件位置"></el-table-column>
      <el-table-column
        prop="createBy"
        label="创建人"
        width="100"
      ></el-table-column>
      <!-- <el-table-column
        prop="delName"
        label="删除人"
        width="100"
      ></el-table-column> -->
      <el-table-column prop="lastUpdate" label="删除时间"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope" width="100">
          <el-button
            @click="oprateFile(scope.row, 'recycle')"
            type="text"
            size="small"
          >
            恢复
          </el-button>
          <el-button
            type="text"
            size="small"
            @click="oprateFile(scope.row, 'redel')"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- <el-pagination
      background
      layout="prev, pager, next"
      :total="pageable.totalElements"
      :page-size="pageable.size"
      @current-change="handleCurrentChange"
      :current-page.sync="pageable.pageSize"
    ></el-pagination> -->
  </div>
</template>

<script>
export default {
  components: {},
  props: {
    tableData: Array,
    pageable: Object,
  },
  data() {
    return {};
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  methods: {
    handleSelectionChange(ids) {
      this.$emit('callBackSelect', ids);
    },

    oprateFile(row, type) {
      this.$emit('callBackOprate', row, type);
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
</style>
