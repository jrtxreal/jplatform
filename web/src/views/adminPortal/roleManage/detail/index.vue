<!-- 角色详情页面 -->
<template>
  <div class="role-detail">
    <el-button @click="goBack" size="small">返回</el-button>

    <div class="ascription">{{ ascriptTree }}</div>
    <div class="content">
      <el-tabs v-model="activeName" type="card" @tab-click="handleClick">
        <el-tab-pane label="编辑" name="edit" class="editWarp">
          <div class="oprate">
            <el-button type="primary" @click="onSubmit('editRoleForm')">
              保存
            </el-button>
          </div>
          <el-form :model="form" ref="editRoleForm" label-width="100px">
            <el-form-item label="角色名">
              <el-input v-model="form.roleName" disabled></el-input>
            </el-form-item>
            <el-form-item label="禁用">
              <el-switch v-model="form.delivery"></el-switch>
            </el-form-item>
            <el-form-item label="管理部门">
              <el-cascader
                v-model="form.depart"
                :options="options"
                :props="props"
                clearable
              ></el-cascader>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="用户" name="account">
          <add-account :roleId="roleId"></add-account>
        </el-tab-pane>
        <el-tab-pane label="权限" name="premissions">
          <setting-premission :roleId="roleId"></setting-premission>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import AddAccount from './addAccount';
import SettingPremission from './settingPremission';

export default {
  name: 'RoleDetail',
  props: {
    deptId: {
      type: String,
      default: '',
    },
    roleId: {
      type: String,
      default: '',
    },
  },
  components: {
    'add-account': AddAccount,
    'setting-premission': SettingPremission,
  },
  data() {
    return {
      ascriptTree: 'A部门/角色A',
      activeName: 'edit',

      // 编辑角色
      form: {
        name: '',
        region: '',
        date1: '',
      },
      props: { multiple: true },
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
  computed: {},
  // 监控data中的数据变化
  watch: {},
  methods: {
    // 获取详情 roleId
    getDetail() {
      console.log('获取详情');
      // getRoleDetail({ roleId: this.roleId })
      //   .then(res => {})
      //   .catch(err => {});
    },

    // 切换tab
    handleClick() {
      // 考虑编辑页面是否更改数据，若更改数据未保存需要提示用户是否保存当前数据 - watch
    },

    // 保存数据
    onSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          console.log(this.form);

          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },

    // 返回
    goBack() {
      this.$emit('setId', '');
    },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {
    // 获得父级给出的列表ID 请求详情数据
    this.getDetail();
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
</style>
