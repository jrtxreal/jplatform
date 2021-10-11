<!--  -->
<template>
  <div class="recycle-page">
    <div class="search-header">
      <el-form
        :inline="true"
        :model="form"
        class="demo-form-inline"
        ref="recycleForm"
      >
        <el-form-item label="文件名" prop="condition">
          <el-input
            v-model="form.condition"
            placeholder="请输入文件名"
          ></el-input>
        </el-form-item>
        <el-form-item label="文件位置" prop="type">
          <el-select v-model="form.type">
            <el-option
              v-for="location in fileLocatin"
              :key="location.id"
              :label="location.name"
              :value="location.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="文件类型" prop="docType">
          <el-select v-model="form.docType">
            <el-option
              v-for="type in fileType"
              :key="type.id"
              :label="type.name"
              :value="type.name"
            ></el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="删除人">
          <el-input
            v-model="form.memberName"
            placeholder="请选择删除人"
            suffix-icon="el-icon-arrow-down"
            @focus="arouseOrgTree(true)"
          ></el-input>
        </el-form-item>
        <el-form-item label="删除时间">
          <el-date-picker
            v-model="form.delTime"
            type="datetimerange"
            range-separator="~"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item> -->
        <el-form-item class="btn">
          <el-button type="primary" @click="onSubmit">
            检索
          </el-button>
        </el-form-item>
        <el-form-item class="btn">
          <el-button @click="onReset('recycleForm')">清空条件</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="oprate-permission">
      <el-button @click="oprate('recycle')" :disabled="isDisable">
        恢复
      </el-button>
      <el-button @click="oprate('redel')" :disabled="isDisable">删除</el-button>
    </div>
    <div class="list-content">
      <Table
        :tableData="list"
        :pageable="pages"
        @callBackOprate="itemOprate"
        @callBackSelect="handleSelect"
      ></Table>
    </div>

    <!-- <OrganizationTree
      v-if="orgTreeVisiable"
      :visiable="orgTreeVisiable"
      :defaultSelectedKeys="form.delUser"
      :defaultSelectedMember="form.selectMsg"
      @callBackResult="getSelectedMember"
    ></OrganizationTree> -->
  </div>
</template>

<script>
// import OrganizationTree from './OrganizationTree/index.vue';
import Table from './table.vue';
import { fileLocatin, fileType } from './initData';
import map from "../../../map";
import { recycleFileList, realCleanBin, recoverFile } from '@/api/recycle';

export default {
  name:map.sys_recycle_query,
  components: {
    // OrganizationTree,
    Table,
  },
  data() {
    return {
      fileLocatin,
      fileType,

      form: {
        condition: '',
        type: 0,
        docType: '所有类型',
      },
      fileIds: [],

      // 列表
      list: [],
      pages: {},

      // 角色树
      // orgTreeVisiable: false,
    };
  },
  // 监听属性 类似于data概念
  computed: {
    isDisable() {
      return this.fileIds.length === 0;
    },
  },
  // 监控data中的数据变化
  watch: {},
  methods: {
    getFileList() {
      const params = this.changeData(this.form);
      recycleFileList(params).then(res => {
        if (res.code === 0) {
          // console.log(res);
          this.list = res.result;
        }
      });
    },

    changeData(params) {
      const { condition, docType, type } = params;
      return {
        condition,
        docType: docType === '所有类型' ? '' : docType,
        type: type === 0 ? '' : type,
      };
    },

    onSubmit() {
      // 无需校验
      this.getFileList();
    },

    onReset(formName) {
      this.$refs[formName].resetFields();
      this.getFileList();
    },

    handleSelect(ids) {
      if (ids.length) {
        this.fileIds = ids.map(item => {
          return item.id;
        });
      }
    },

    oprate(type, data) {
      data = data || this.fileIds.join();

      let title = '',
        message = '',
        reqPromisse = null;

      if (type === 'recycle') {
        title = '是否将所选文件恢复至原位';
        message = '恢复';
        reqPromisse = recoverFile;
      } else {
        title = '此操作将永久删除所选文件, 是否继续?';
        message = '删除';
        reqPromisse = realCleanBin;
      }

      this.$confirm(title, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          reqPromisse({
            fileIds: data,
          }).then(res => {
            // console.log(res, 'type');
          });
          this.$message({
            type: 'success',
            message: `${message}成功`,
          });
        })
        .catch(() => {
          // console.log('0000000');
          this.$message({
            type: 'info',
            message: `已取消${message}`,
          });
        })
        .finally(() => {
          this.getFileList();
        });
    },

    itemOprate(row, type) {
      // console.log(row, type);
      this.oprate(type, row.id);
    },

    // 展示组织架构 - 选择删除人
    // arouseOrgTree(status) {
    //   this.orgTreeVisiable = status;
    // },

    // 返回已选择成员信息
    // getSelectedMember(ids, gathers, type) {
    //   console.log(ids, gathers, type);

    //   this.form.memberName =
    //     gathers.length > 0
    //       ? gathers
    //           .map(item => {
    //             return item.name;
    //           })
    //           .join(',')
    //       : '';
    //   this.form.delUser = ids || '';
    //   this.form.selectMsg = gathers || [];
    //   // this.form.delUser = ids || '';

    //   this.arouseOrgTree(false);
    // },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {},
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素，但mounted不能保证所有的子组件都挂载成功）
  mounted() {
    this.getFileList();
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
@import './index.scss';
//@import url(); 引入公共css类
</style>
