<!-- 角色详情页面 -->
<template>
  <div class="role-detail">
    <div style="padding:20px;">
      <el-page-header @back="goBack" :content="ascriptTree  + '/' + form.name">
      </el-page-header>
    </div>

    <!-- <div class="ascription">{{ ascriptTree }}</div> -->
    <div class="content">
      <el-tabs v-model="tabIndex" type="border-card">
        <el-tab-pane label="编辑" name="edit" class="editWarp">
          <div v-if="tabIndex==='edit'">
            <el-form :model="form" ref="editRoleForm" label-width="100px">
              <el-form-item label="角色名">
                <el-input v-model="form.name"></el-input>
              </el-form-item>
              <el-form-item label="禁用">
                <el-switch v-model="form.delivery"></el-switch>
              </el-form-item>
              <el-form-item>
                <div style="text-align: right;">
                  <el-button  type="primary" @click="onSubmit('editRoleForm')">
                    保存
                  </el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
        <el-tab-pane
          label="用户"
          name="account"
        >
          <div v-if="tabIndex==='account'">
            <add-account :roleId="roleId"></add-account>
          </div>
        </el-tab-pane>
        <el-tab-pane
          label="权限"
          name="premissions"
        >
          <div v-if="tabIndex==='premissions'">
            <setting-premission :roleId="roleId"></setting-premission>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import AddAccount from './addAccount';
import SettingPremission from './settingPremission';
import {
  roleDetail,
  getDeptTreeOfM,
  editRole,
} from '../../../../api/roleManage';
import udTree from '../../../../utils/udTreeUtil';
import checkPermit from '../../../../utils/udFrameWorkUtil';
// import { roleDetail , getDeptTree , addRole} from '@/api/roleManage';
export default {
  name: 'RoleDetail',
  props: {
    deptId: {
      type: Number,
      default: 0,
    },
    roleId: {
      type: Number,
      default: 0,
    },
    rolePath: {
      type: String,
      default: '/',
    },
    pathName: {
      type: String,
      default: '/',
    },
    depatName: {
      type: String,
      default: '/',
    },
  },
  components: {
    'add-account': AddAccount,
    'setting-premission': SettingPremission,
  },
  data() {
    return {
      ascriptTree: this.pathName,
      tabIndex: 'edit',
      currentDeptId: 0,
      isSave:false,
      // 编辑角色
      form: {
        name: '',
        delivery: false,
        depart: 0,
      },
      selectDepatList: [],
      cascaderProps: { multiple: true, checkStrictly: true },
      options: [],
      cascaderPropsmn: {
        value: 'id',
        label: 'name',
        children: 'children',
        multiple: true,
        emitPath: false,
      },
    };
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  methods: {
    // 获取部门树 - 一次性
    getDeptList(params) {},

    // 获取详情 roleId
    getDetail(prma) {
      //console.log('获取详情', prma);
      const vm = this;
      roleDetail(prma)
        .then((data) => {
          //console.log(data);
          if (data.code == 0) {
            vm.form.name = data.result.name;
            vm.form.delivery = data.result.disabled;
            //vm.form.depart = data.result.depatName;
            vm.currentDeptId = data.result.deptId;
          }
        })
        .catch((error) => {
          console.log('获取详情' + error);
        });
    },
    saveEditRole: function (prma) {
      const vm = this;
      editRole(prma)
        .then((data) => {
          //console.log(data);
          vm.isSave = false;
          if (data.code == 0) {
            vm.$message({
              message: '保存成功',
              type: 'success',
            });
          } else {
            vm.$message.error('保存失败：' + data.msg);
          }
        })
        .catch((error) => {
          vm.isSave = false;
          vm.$message.error('保存失败：' + error);
        });
    },
    // 数据格式转化 - 平铺展示所有部门，每一个部门id为key ，部门的path为value
    getParent(res, list) {
      list.forEach((item) => {
        const { id, children, path } = item;
        if (!res[id]) {
          res.id = path;
        }
        if (Array.isArray(children) && children.length > 0) {
          this.getParent(res.children);
        }
      });

      return res;
    },
    checkPermits: function (key, path) {
      return checkPermit.checkPermit(key, path);
    },
    // 切换tab
    handleClick() {
      // 考虑编辑页面是否更改数据，若更改数据未保存需要提示用户是否保存当前数据 - watch
    },
    handleChange(value) {
      const vm = this;
      if (value) {
        vm.selectDepatList = value;
      } else {
        vm.selectDepatList = [];
      }
    },
    // 保存数据
    onSubmit(formName) {
      const vm = this;
      vm.isSave = true;
      vm.$refs[formName].validate((valid) => {
        if (valid) {
          //console.log('提交数据', vm.form);
          // var ids = vm.selectDepatList.join(',');
          // if (vm.selectDepatList.length == 0) {
          //   ids = vm.form.depart;
          // }
          var prma = {
            id: vm.roleId,
            disabled: vm.form.delivery,
            name: vm.form.name,
          };
          console.log(prma);
          vm.saveEditRole(prma);
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    // 返回
    goBack() {
      const vm = this;
      const info = {
        id:vm.roleId,
        deptId:vm.deptId
      }
      this.$emit('setId', info);
    },
    //zmnz_部门树
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
            //console.log(roleList);
            if (roleList) {
              vm.options = roleList.topLeaveList;
              vm.form.depart = vm.currentDeptId;
            }
          }
        })
        .catch((error) => {
          vm.$message.error('获取部门列表失败，请联系管理员\n' + error);
        });
    },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {
    // 请求部门数据
    //this.getDeptList({ spreadTag: false, deptId: '' });
    // 获得父级给出的列表ID 请求详情数据
    //this.getDetail();
    const vm = this;
    vm.getDeptTreeList();
    var prma = {
      roleId: vm.roleId,
    };
    vm.getDetail(prma);
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
<style lang="scss" scoped>
@import './index.scss';
.okbutton{
  height: 100px;
  display: flex;
  justify-content: center;
  align-items: flex-end;
}
</style>
