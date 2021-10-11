<!-- 添加角色下权限 -->
<template>
  <div class="premissWarp">
    <div class="oprate">
      <el-button type="primary" @click="onSubmit">保存</el-button>
    </div>

    <div class="select-depart">
      <el-divider content-position="left">作用范围</el-divider>
      <div class="accountList">
        <el-tag v-for="item in departList" :key="item.id" class="accitem">
          {{ item.name }}
        </el-tag>
      </div>
    </div>

    <div class="premission">
      <el-divider content-position="left">权限</el-divider>

      <div class="check-warp">
        <div
          class="accountList"
          v-for="perItem in permissList"
          :key="perItem.name"
        >
          <span>{{ perItem.name }}</span>
          <el-checkbox-group
            v-model="checkedCities"
            @change="handleCheckedCitiesChange"
          >
            <el-checkbox
              v-for="(btn, index) in perItem.btns"
              :label="btn"
              :key="index"
            >
              {{ btn }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      departList: [
        {
          id: 1,
          name: 'A部门',
        },
        {
          id: 2,
          name: 'B部门',
        },
        {
          id: 4,
          name: 'C部门/c1部门',
        },
      ],
      checkedCities: ['上海', '北京'],

      permissList: [
        {
          name: '系统管理/用户管理',
          btns: ['权限A', '权限B', '权限C'],
        },
        {
          name: '系统管理/角色管理',
          btns: ['权限E', '权限F', '权限G'],
        },
      ],
    };
  },
  // 监听属性 类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  methods: {
    // 请求该角色下的部门list
    getDepart() {
      // this.departList=[]
    },

    // 请求该角色下的部门list
    getPermiss() {
      // this.permissList=[]
    },

    handleCheckedCitiesChange(value) {
      console.log(value);
      // let checkedCount = value.length;
      // this.isIndeterminate =
      //   checkedCount > 0 && checkedCount < this.cities.length;
    },
  },
  //  创建之前
  beforeCreate() {},
  // 创建完成（可以访问当前this实例）
  created() {},
  //  挂载之前
  beforeMount() {},
  // 挂载完成（可以访问DOM元素）
  mounted() {
    this.getDepart();
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
.premissWarp {
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

.select-depart {
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

.check-warp {
  // border: 1px solid;
  padding: 0px 10px;

  .accountList {
    margin-bottom: 20px;
    .el-checkbox-group {
      margin: 10px 10px 0;

      .el-checkbox {
        margin-bottom: 10px;
      }
    }
  }
}

//@import url(); 引入公共css类
</style>
