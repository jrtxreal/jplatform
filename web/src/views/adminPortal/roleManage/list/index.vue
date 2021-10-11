<!-- 列表 -->
<template>
  <div>
    <div class="role-list">
      <div class="oprate">
        <el-input
          placeholder="请输入内容"
          v-model="rolename"
          style="width:300px"
        >
          <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
        <el-button type="primary" @click="dialogFormVisible = true">
          添加角色
        </el-button>
      </div>
      <div class="ascription">{{ ascriptTree }}</div>
      <div class="table">
        <el-table :data="tableData" stripe style="width: 100%" border>
          <el-table-column
            prop="name"
            label="角色名"
            width="180"
          ></el-table-column>
          <el-table-column prop="deptName" label="部门"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope" width="180">
              <el-button @click="getDetail(scope.row)" type="text" size="small">
                查看
              </el-button>
              <el-button type="text" size="small" @click="delect(scope.row)">
                删除
              </el-button>
            </template>
          </el-table-column>
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

    <el-dialog title="添加角色" :visible.sync="dialogFormVisible">
      <el-form
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="部门" prop="depart">
          <el-cascader
            :options="options"
            :props="props"
            clearable
            v-model="ruleForm.depart"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="角色名" prop="name">
          <el-input autocomplete="off" v-model="ruleForm.name"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
        <el-button type="primary" @click="addRole('ruleForm')">
          保 存
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// import getRoleList from '../../../../../../api/roleManage';

export default {
  name: 'RoleList',
  props: {
    deptId: {
      type: String,
      default: '',
    },
  },
  components: {},
  data() {
    return {
      ascriptTree: '/A部门',
      rolename: '',
      tableData: [
        {
          id: 0,
          name: '小王子',
          deptName: 'B-612部门',
        },
        {
          id: 1,
          name: '国王',
          deptName: '地球部门',
        },
      ],
      pageable: {},

      current: 1,

      dialogFormVisible: false, // 新增弹框
      props: { multiple: true },
      ruleForm: {
        depart: '',
        name: '',
      },
      rules: {
        depart: [{ required: true, message: '请选择部门', trigger: 'change' }],
        name: [
          {
            required: true,
            message: '请输入角色名',
            trigger: ['blur', 'change'],
          },
          {
            min: 1,
            max: 15,
            message: '长度在 1 到 15 个字符',
            trigger: ['blur', 'change'],
          },
        ],
      },
      options: [
        {
          value: 1,
          label: '东南',
          children: [
            {
              value: 2,
              label: '上海',
              children: [
                { value: 3, label: '普陀' },
                { value: 4, label: '黄埔' },
                { value: 5, label: '徐汇' },
              ],
            },
            {
              value: 7,
              label: '江苏',
              children: [
                { value: 8, label: '南京' },
                { value: 9, label: '苏州' },
                { value: 10, label: '无锡' },
              ],
            },
            {
              value: 12,
              label: '浙江',
              children: [
                { value: 13, label: '杭州' },
                { value: 14, label: '宁波' },
                { value: 15, label: '嘉兴' },
              ],
            },
          ],
        },
        {
          value: 17,
          label: '西北',
          children: [
            {
              value: 18,
              label: '陕西',
              children: [
                { value: 19, label: '西安' },
                { value: 20, label: '延安' },
              ],
            },
            {
              value: 21,
              label: '新疆维吾尔族自治区',
              children: [
                { value: 22, label: '乌鲁木齐' },
                { value: 23, label: '克拉玛依' },
              ],
            },
          ],
        },
      ],
    };
  },
  // 监听属性 类似于data概念
  computed: {
    //
  },
  // 监控data中的数据变化
  watch: {},
  methods: {
    // 获得角色列表
    getList(param) {
      // getRoleList({
      //   page: 1,
      //   deptId: this.deptId,
      //   name: this.rolename,
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

    // 新增角色
    addRole(formName) {
      // this.dialogFormVisible = true;

      this.$refs[formName].validate(valid => {
        if (valid) {
          console.log(this.ruleForm);
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    // 取消
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogFormVisible = false;
    },

    // 详情
    getDetail(row) {
      // 传递ID给父级
      console.log(row.id);
      const roleId = row.id;
      this.$emit('setId', roleId);
    },

    // 删除
    delect() {
      this.$confirm('是否确定删除此角色?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          // 调用删除接口
          // delect(){}
          this.$message({
            type: 'success',
            message: '删除成功!',
          });

          this.getList();
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除',
          });

          this.getList();
        });
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
  created() {
    this.getList();
  },
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素）
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
<style lang="scss">
@import './index.scss';
</style>
