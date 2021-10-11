<!-- 添加角色下的用户 -->
<template>
  <div class="accountWarp">
    <div class="oprate">
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </div>

    <div class="select-account">
      <el-divider content-position="left">当前角色下的用户</el-divider>
      <div class="accountList">
        <el-tag
          v-for="item in selectaccList"
          :key="item.id"
          closable
          @close="handleClose(item)"
          class="accitem"
        >
          {{ item.name }}
        </el-tag>
      </div>
    </div>

    <div class="account-list">
      <div class="search">
        <el-form :model="form" ref="accountForm" :inline="true">
          <el-form-item label="用户名:">
            <el-input v-model="form.accountName"></el-input>
          </el-form-item>

          <el-form-item label="部门:">
            <el-input v-model="form.departName"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="onSearch('accountForm')">
              查询
            </el-button>
            <el-button @click="resetForm('accountForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="search">
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          border
          stripe
        >
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column
            prop="accountName"
            label="用户名"
            width="120"
          ></el-table-column>
          <el-table-column
            prop="departName"
            label="部门"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="1000"
          :page-size="10"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page.sync="current"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      tableData: [
        {
          id: 1,
          accountName: '小王子',
          departName: '上海市普陀区金沙江路 1518 弄',
        },
        {
          id: 2,
          accountName: '地理学家',
          departName: '上海市普陀区金沙江路 1518 弄',
        },
        {
          id: 3,
          accountName: '商人',
          departName: '上海市普陀区金沙江路 1518 弄',
        },
        {
          id: 4,
          accountName: '白蛇',
          departName: '上海市普陀区金沙江路 1518 弄',
        },
      ],

      selectaccList: [
        {
          id: 1,
          name: '小王子@A部门',
        },
        {
          id: 2,
          name: '地理学家',
        },
        {
          id: 4,
          name: '白蛇',
        },
      ],

      multipleSelection: [], // id

      form: {
        accountName: '',
        departName: [],
      },

      current: 1,
    };
  },
  // 监听属性 类似于data概念
  computed: {
    //
  },
  // 监控data中的数据变化
  watch: {
    selectaccList: {
      handler(newVal) {
        this.multipleSelection = newVal.map(item => {
          return item.id;
        });
      },
      immediate: true,
      deep: true,
    },

    multipleSelection: {
      handler(newVal) {
        this.multipleSelection = newVal;
        this.lasterSelect();
      },
      // immediate: true,

      deep: true,
    },
  },
  methods: {
    // 获得当前角色的用户
    getList() {
      // getAccountList({
      //   page: 1,
      //   name: this.accountName,
      //   departName:this.departName,
      //   ...param,
      // }).then(res => {
      //     const { code, result } = res;
      //     let list = [],
      //       pages = {};
      //     if (code === 0 && result) {
      //       const { content = [], pageable = {} } = result;
      //       list = [].concat(content);
      //       pages = Object.assign({}, pageable);
      //     }
      //     this.tableData = list;
      //     this.pageable = pages;
      // });
      //   .catch(err => {});
    },

    // 关闭标签
    handleClose(item) {
      // const { id } = item;
      this.selectaccList.splice(this.selectaccList.indexOf(item), 1);
      // this.multipleSelection.splice(this.selectaccList.indexOf(id), 1); // id
    },

    // 查询
    onSearch() {
      console.log(this.form);
      // this.getList({ ...this.form });
    },

    // 重置
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.getList({ page: 1 });
    },

    // 保存
    onSubmit() {
      save()
        .then(res => {
          this.getList();
        })
        .catch(err => {});
    },

    // 用户选择
    handleSelectionChange(val) {
      // this.multipleSelection = val;
    },

    // 设置选中状态
    lasterSelect() {
      let selected = [].concat(this.multipleSelection); // 选中的值
      this.tableData.forEach(row => {
        const { id } = row;
        if (selected.length > 0 && selected.indexOf(id) !== -1) {
          selected.splice(selected.indexOf(id), 1);
          this.$refs.multipleTable.toggleRowSelection(row, true);
        } else {
          this.$refs.multipleTable.toggleRowSelection(row, false);
        }
      });
    },

    // 切换某行的状态
    toggleSelection() {
      // 使用第二个参数来控制该行是都选中
      this.$refs.multipleTabletoggleRowSelection(row, true);
    },

    // 页码
    handleCurrentChange(index) {
      console.log(index);
      this.getList({ page: index });
    },

    handleSizeChange() {},
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {},
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素）
  mounted() {
    this.lasterSelect();
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

.accountWarp {
  display: flex;
  flex-direction: column;
  padding: 0 20px 0px 0px;
}

.oprate {
  height: auto;
  display: flex;
  justify-content: flex-end;
  padding: 0 0 15px;
}

.select-account {
  position: relative;
  .el-divider--horizontal {
    height: 130px;
    display: block;
    margin: 24px 0;
    border: 1px solid;
    background: #fff !important;
  }

  .accountList {
    position: absolute;
    top: 24px;
    width: 100%;
    height: 130px;
    padding: 15px 0px 0 4%;
    box-sizing: border-box;

    .accitem {
      display: inline-block;
      width: 20%;
      line-height: 30px;
      text-align: center;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      border-radius: 5px;
      cursor: pointer;
      margin: 10px 3% 0px 0px;
    }
  }
}

.el-form {
  .el-form-item {
    margin-right: 30px;
    .el-input {
      width: 150px !important;
    }

    .el-cascader {
      width: 150px !important;
    }
  }
}
</style>
