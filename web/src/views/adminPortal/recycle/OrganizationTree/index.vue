<!-- 
  * @author: dxyuan
  * @description: 该组件是一个选择人员的弹框，可从组织架构选择  只有是员工才可以被勾选
  *
  * props  父组件传递的参数
  * @param {String} defaultSelectedKeys
  * defaultSelectedKeys = '1,2,3,4' 
  * @param {String} defaultSelectedMember
  * defaultSelectedMember = [{id:"1",name:"dxyuan"}] 
  * @param {Boolean} visiable 该弹框是否展示
  * @param {Function} callBackResult()
  * 例：callBackResult(ids,memberRes)  接受两个参数，一个是id的集合  一个是会员详细信息的集合
  *
 -->
<template>
  <div class="organization">
    <el-dialog
      :visible="visiable"
      class="memberDialog"
      @close="submit('cancle')"
    >
      <div v-if="!isSearch">
        <div class="ant-breadcrumb">
          <span
            v-for="(path, index) in organPath"
            :key="index"
            @click="() => toPath('back', path, index)"
            class="ant-breadcrumb-link"
          >
            <a href="javascript:;" class="breadcrumb-folder" :title="path.name">
              <span class="breadcrumb-groupName">
                {{ path.name }}
              </span>
              <span class="ant-breadcrumb-separator">/</span>
            </a>
          </span>
        </div>
        <el-table
          ref="multipleTable"
          :data="orgTable"
          tooltip-effect="dark"
          style="width: 100%"
          @row-click="row => toPath('forward', row)"
          highlight-current-row
          :row-class-name="tableRowClassName"
          row-key="id"
        >
          <el-table-column align="left" width="55px">
            <template slot-scope="scope">
              <el-checkbox
                v-if="checkBoxIsshow(scope.row)"
                :checked="selectKeys.indexOf(scope.row.id) != -1"
                @change="
                  (status, event) => handleChange(scope.row, status, event)
                "
              ></el-checkbox>
            </template>
          </el-table-column>
          <el-table-column
            prop="name"
            label="名称"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="account"
            label="账号"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </div>

      <SearchTeam
        v-if="isSearch"
        :list="searchList"
        :selectKeys="selectKeys"
        :searchText="input1"
        @back="clearFn"
        @selectFn="handleChange"
        @toPathFn="toPath"
      ></SearchTeam>

      <div slot="title" class="dialog-title">
        <span>选择用户</span>
        <el-input
          size="medium"
          placeholder="搜索"
          v-model="input1"
          style="width:200px"
          suffix-icon="el-icon-search"
          :clearable="true"
        ></el-input>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button
          type="primary"
          @click="submit"
          :disabled="!!!selectKeys.length"
        >
          确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrgList, searchName } from '@/api/recycle';
import SearchTeam from './search.vue';

export default {
  props: {
    visiable: Boolean,
    defaultSelectedKeys: String,
    defaultSelectedMember: Array,
  },
  components: {
    SearchTeam,
  },
  data() {
    return {
      input1: '',
      searchList: [],
      isSearch: false,

      orgTable: [], // 组织架构

      // 选择的员工id - 组织和团队
      selectKeys: [], // 新旧均有 - ids
      selectMemberMsg: [], // 新均有 - 成员信息  -  只有新建团队的时的添加成员才需要返回所选的成员信息  用于展示成员信息名字等..
      organPath: [], // 组织的面包屑
      defaultOrganPath: [{ name: '组织', id: 'self-org', isDept: true }], // 组织的面包屑
    };
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {
    input1: {
      // 全局的请求
      handler(newVal) {
        // 请求列表
        this.reqSearchList(newVal);
      },
    },

    organPath: {
      handler(newVal) {
        const row = newVal[newVal.length - 1];
        const params = row.id === 'self-org' ? {} : { deptId: row.id };
        this.reqOrganiza(params);
      },
      deep: true,
    },
  },
  methods: {
    // row-class-name
    tableRowClassName({ row }) {
      if (row.isDept) {
        return 'curOption';
      }
    },

    // 初始化组织架构/团队路径
    initPath() {
      this.organPath = [].concat(this.defaultOrganPath);
    },

    // 设置已选择的成员-id
    setDefaultKeys() {
      this.selectKeys =
        this.defaultSelectedKeys && this.defaultSelectedKeys.length
          ? [].concat(this.defaultSelectedKeys.split(','))
          : [];

      this.selectMemberMsg =
        this.defaultSelectedMember && this.defaultSelectedMember.length
          ? [].concat(this.defaultSelectedMember)
          : [];
    },

    // 跳转到某级 同时更新path  step=forward是前进操作 ，back是后退
    toPath(step, row, index) {
      if (!row.isDept) {
        return;
      }
      if (step === 'forward') {
        this.organPath.push({
          ...row,
        });
      } else {
        //
        // console.log('后退', row);
        const len = (this.organPath && this.organPath.length) || 0;
        this.organPath.splice(index + 1, len);
      }
    },

    // 请求组织
    // 请求当前点击当前行 进入下一级
    reqOrganiza(params) {
      getOrgList(params)
        .then(res => {
          if (res.code === 0 && res.result) {
            this.orgTable = res.result.map(item => {
              return {
                name: item.name,
                account: item.userName || '',
                isDept: item.dept,
                id: item.id,
              };
            });
          }
        })
        .catch(err => {
          console.log('组织架构列表请求失败');
        });
    },

    // 选择具体的账号信息
    handleChange(row, status, event) {
      console.log('我是搜索的参数，能否成功获得', row, status, event);

      if (event) {
        event.stopPropagation();
        event.preventDefault();
      }
      // 为真则为勾选，添加到selectKeys中，否则删除该id
      if (status) {
        this.selectKeys.push(row.id);
        this.selectMemberMsg.push(row);
      } else {
        this.selectKeys = this.selectKeys.filter(item => item !== row.id);
        this.selectMemberMsg = this.selectMemberMsg.filter(
          item => item.id !== row.id,
        );
      }
    },

    // 确定 或 取消
    submit(type) {
      // 如果是取消不需要做校验，所以传递了type:cancle来区分
      if (type === 'cancle') {
        this.$emit(
          'callBackResult',
          this.defaultSelectedKeys,
          this.defaultSelectedMember,
          'cancle',
        );
      } else {
        const ids = this.selectKeys.join();

        // 只有 新建团队时的添加成员才需要返回MemeberMsg 用于展示成员信息
        this.$emit('callBackResult', ids, this.selectMemberMsg);

        this.selectKeys = [];
        this.selectMemberMsg = [];
      }
    },

    // 全局搜索
    reqSearchList(val) {
      if (!val) {
        this.isSearch = false;
      } else {
        this.isSearch = true;
        searchName({ condition: val })
          .then(res => {
            if (res.code === 0 && res.result) {
              if (Array.isArray(res.result) && res.result.length > 0) {
                this.searchList = res.resul.map(item => {
                  return {
                    ...item,
                    name: item.name,
                    id: item.id,
                    isDept: item.type !== 'user',
                  };
                });
              }
            }
          })
          .catch(err => {
            console.log('请求筛选的数据失败');
          });
      }
    },

    // 返回
    clearFn() {
      this.isSearch = false;
    },

    // 是否可展示可选
    checkBoxIsshow(row) {
      return !(row.isDept || this.selectKeys.indexOf(String(row.id)) === 0);
    },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {
    // this.reqOrganiza();
    this.initPath();
  },
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素）
  mounted() {
    this.setDefaultKeys();
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
::v-deep .curOption {
  cursor: pointer !important;
}

.memberDialog ::v-deep .el-dialog {
  width: 60%;
}

::v-deep .el-dialog__header {
  padding: 10px 20px !important;
}
::v-deep .el-dialog__body {
  padding: 20px !important;
}

.dialog-title {
  display: flex;
  align-items: center;
  padding-right: 40px;
  justify-content: space-between;
  font-size: 16px;
}

.ant-breadcrumb {
  height: 50px;
  line-height: 50px;
  display: flex;
  flex-flow: row wrap;
  padding-left: 10px;

  .ant-breadcrumb-link {
    .ant-breadcrumb-separator {
      margin: 0 8px;
      color: rgba(0, 0, 0, 0.45);
    }
  }
}

.ant-breadcrumb a:hover {
  color: #40a9ff;
}

.breadcrumb-folder {
  margin-left: 4px;
  display: flex;
  max-width: 500px;
  -webkit-box-align: center;
  align-items: center;
}
.ant-breadcrumb > span:last-child .ant-breadcrumb-separator {
  display: none;
}

.ant-breadcrumb > span:last-child,
.ant-breadcrumb > span:last-child a {
  color: rgba(0, 0, 0, 0.65);
}

.breadcrumb-groupName {
  margin-left: 4px;
  display: block;
  max-width: 100px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  word-break: keep-all;
}

::v-deep .el-table {
  height: 400px;
  overflow: auto;
}

::v-deep .el-table::before {
  height: 0px;
}
</style>
