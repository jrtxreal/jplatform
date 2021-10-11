<!-- 列表 -->
<template>
  <div class="sys-role-list">
    <div class="role-list">
      <div class="oprate" @keyup.enter="searchRole">
        <div style="flex-grow: 1">
          <el-input
                  placeholder="请输入内容"
                  v-model="name"
                  style="width: 300px; padding-right: .5rem;"
                  clearable
                  @clear="searchRole"
          ></el-input>
          <el-button type="primary" @click="searchRole">
            查询
          </el-button>
        </div>

        <el-button
          v-if="checkPermit(map.sys_role_add)"
          type="primary"
          @click="showAddRolePanel"
        >
          添加
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
          <el-table-column prop="fullPath" label="部门"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope" width="180">
              <el-button
                      v-if="checkPermit2(map.sys_role_edit,scope.row.path)"
                      @click="getDetail(scope.row)" type="text" size="small">
                详情
              </el-button>
              <el-button
                v-if="checkPermit2(map.sys_role_delete,scope.row.path)"
                type="text"
                size="small"
                @click="delect(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="pageable.totalElements"
          :page-size="pageable.size"
          @current-change="handleCurrentChange"
          :current-page.sync="pageable.pageSize"
        ></el-pagination>
      </div>
    </div>

    <el-dialog width="600px" title="添加角色" :visible.sync="dialogFormVisible" :close-on-click-modal=false v-if="dialogFormVisible">
      <el-form
        v-if="dialogFormVisible"
        :model="ruleForm"
        ref="ruleForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="部门" prop="depart">
          <el-cascader
            v-model="ruleForm.depart"
            :options="departmentList"
            :props="cascaderPropsmn"
            :show-all-levels="false"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="角色名" prop="name">
          <el-input autocomplete="off" v-model="ruleForm.name"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetForm('ruleForm')">取 消</el-button>
        <el-button type="primary" @click="addSysRole('ruleForm')" :loading='isSave'>保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRoleList,
  addRole,
  getDeptTreeOfM,
  deleteRole,
} from '../../../../api/roleManage';
import udTree from '../../../../utils/udTreeUtil';
import checkPermit from '../../../../utils/udFrameWorkUtil';
import udFrameWorkUtil from "../../../../utils/udFrameWorkUtil";
import map from '../../../../map'

export default {
  name: 'RoleList',
  props: {
    deptId: {
      type: Number,
      default: 0,
    },
  },
  components: {},
  data() {
    return {
      map,
      ascriptTree: '',
      name: '',
      isSave:false,
      tableData: [],
      pageable: {
        totalElements: 0,
        size: 10,
        pageSize: 1,
      },
      current: 1,
      dialogFormVisible: false, // 新增弹框
      ruleForm: {
        depart: [],
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
      departmentList: [],
      cascaderPropsmn: {
        value: 'id',
        label: 'name',
        children: 'children',
        multiple: true,
        emitPath: false,
        checkStrictly: true
      },
      curentDepatId: 0,
      currentPage:1
    };
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {
    deptId: {
      handler: function (value, old) {
        if (value != 0) {
          const vm = this;
          var prma = {
            page: 1,
            size: 10,
            deptId: value,
          };
          vm.getRoteByDeptId(prma);
        }
      },
      immediate: true, //关键
      deep: true,
    },
  },
  methods: {
    checkPermit(permit){
      return udFrameWorkUtil.checkPermit(permit,'all');
    },
    checkPermit2(permit,path){
      return udFrameWorkUtil.checkPermit(permit,path);
    },
    showAddRolePanel(){
      const vm = this;
      vm.ruleForm.depart = [vm.deptId];
      vm.ruleForm.name = "";
      vm.dialogFormVisible = true;
    },
    isAdd: function () {
      return checkPermit.checkPermit('sys_role_add', 'all');
    },
    //zmn_获得角色列表
    getRoteByDeptId: function (prma) {
      const vm = this;
      getRoleList(prma)
        .then((data) => {
          if (data.code == 0) {
            vm.tableData = data.result.content;
            vm.pageable.totalElements = data.result.totalElements;
          }
        })
        .catch((error) => {
          vm.$message.error('获取角色列表失败，请联系管理员\n' + error);
        });
    },
    //zmn_根据部门id添加角色
    addRoleInfo: function (prma) {
      const vm = this;
      vm.isSave = true;
      addRole(prma)
        .then((data) => {
          if (data.code == 0) {
            vm.dialogFormVisible = false;
            vm.$message({
              message: '添加成功',
              type: 'success',
            });
            var prma = {
              page: 1,
              size: 10,
              deptId: vm.deptId,
            };
            vm.getRoteByDeptId(prma);
            vm.isSave = false;
          } else {
            vm.$message.error('添加角色失败，失败原因\n' + data.msg);
            vm.isSave = false;
          }
        })
        .catch((error) => {
          vm.$message.error('添加角色失败，请联系管理员\n' + error);
          vm.isSave = false;
        });
    },
    //zmn_获取部门列表
    getDeptTreeList: function () {
      const vm = this;
      getDeptTreeOfM()
        .then((data) => {
          //console.log(data);
          if (data.code == 0) {
            let roleList = udTree.build2(
              data.result,
              'id',
              'pid',
              null,
              'ord',
              'children',
            );
            if (roleList) {
              vm.departmentList = roleList.topLeaveList;
            }
          }
        })
        .catch((error) => {
          vm.$message.error('获取部门列表失败，请联系管理员\n' + error);
        });
    },
    // 查询
    searchRole() {
      const vm = this;
      var prma = {
        page: 1,
        size: 10,
        condition: vm.name,
        deptId: vm.deptId,
      };
      this.getRoteByDeptId(prma);
    },
    // 页码
    handleCurrentChange(index) {
      //console.log(index, '当前页码');
      const vm = this;
      var prma = {
        page: index,
        size: 10,
        deptId: vm.deptId,
      };
      vm.currentPage = index;
      vm.getRoteByDeptId(prma);
    },
    // 获取焦点
    handleFocus() {
      // console.log('sfsjdfhjs');
      // 请求部门列表数据
    },
    // 新增角色
    addSysRole(formName) {
      const vm = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let deptIdS = vm.ruleForm.depart.join(',');
          let prma = {
            name: vm.ruleForm.name,
            deptIds: deptIdS,
          };
          vm.addRoleInfo(prma);
        } else {
          // console.log('error submit!!');
          return false;
        }
      });
    },
    // 取消
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.dialogFormVisible = false;
    },
    // 删除
    delect(row) {
      const vm = this;
      var prma = {
        roleId: row.id,
        deptId: row.deptId,
      };
      this.$confirm('是否确定删除此角色?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
          vm.deleteRole(prma);
        })
        .catch(() => {
        });
    },
    deleteRole: function (prma) {
      const vm = this;
      //console.log(prma);
      deleteRole(prma)
        .then((data) => {
          //console.log(data);
          if (data.code == 0) {
            this.$message({
              type: 'success',
              message: '删除成功!',
            });
            var prma = {
              page: vm.currentPage,
              size: 10,
              deptId: vm.deptId,
            };
            //console.log(prma);
            this.getRoteByDeptId(prma);
          } else {
            vm.$message.error('删除角色失败，\n' + data.msg);
          }
        })
        .catch((error) => {
          vm.$message.error('删除角色失败，请联系管理员\n' + error);
        });
    },
    // 详情 - 跳转页面
    getDetail(row) {
      // 传递ID给父级
      const vm = this;
      const roleId = row;
      this.$emit('setId', roleId);
    },
    //检查权限
    getCheckPermit: function (item) {
      //console.log(item);
      return checkPermit.checkPermit('sys_role_delete', item.path);
    },
    dialigCloseClick:function(){
      const vm = this;
      // vm.ruleForm.depart = [];
      // vm.ruleForm.name = '';
    }
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {
    const vm = this;
    vm.getDeptTreeList();
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
<style scoped lang="scss">
@import './index.scss';
.sys-role-list{

}
</style>
