<template>
  <div class="section">
    <div class="sectionHeader">
      <el-page-header @back="goBack" :content="sectionInfo.name">
      </el-page-header>
    </div>
    <div class="sectionDetails">
      <div class="detailsHeader">
        <div>
          <el-button @click="detailsEdit">{{ buttonName }}</el-button>
          <el-button
                  type="primary"
                  @click="detailsSave"
                  :disabled="allowEdit"
                  :loading='isSave'
          >
            保存
          </el-button>
          <el-button type="danger" @click="dialogVisible = true">
            删除
          </el-button>
        </div>
      </div>
      <div class="detailsbody">
        <div class="detailsbodyFrom">
          <el-form
            label-width="100px"
            :model="formData"
            :rules="rules"
            ref="formData"
          >
            <el-form-item label="分组名称：" prop="name">
              <el-input
                v-model="formData.name"
                style="width: 260px"
                clearable
                :disabled="allowEdit"
              ></el-input>
            </el-form-item>
            <el-form-item label="是否排他：" prop="exclusive">
              <el-select
                v-model="formData.exclusive"
                placeholder="请选择"
                style="width: 260px"
                :disabled="allowEdit"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="排序号：" prop="ord">
              <el-input-number
                v-model="formData.ord"
                :min="0"
                :max="10"
                label="排序等级"
                style="width: 260px"
                :disabled="allowEdit"
              ></el-input-number>
            </el-form-item>
            <el-form-item label="创建时间：">
              <div style="color: #666666">
                {{ formData.createTime }}
              </div>
            </el-form-item>
            <el-form-item label="修改时间：">
              <div style="color: #666666">
                {{ formData.lastUpdate }}
              </div>
            </el-form-item>
            <el-form-item label="修改人：">
              <div style="color: #666666">
                {{ formData.updateBy }}
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
    <el-dialog title="提示" :visible.sync="dialogVisible" width="400px">
      <span>是否删除此字典分组？（此字典分组下的所有字典都会被删除）</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="detailsDelete">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { deleteDictSection, editDictSection } from '../../../../api/dict';
import udStoreUtil from "../../../../utils/udStoreUtil";

export default {
  name: 'dictSection',
  data() {
    return {
      options: [
        {
          value: true,
          label: '是',
        },
        {
          value: false,
          label: '否',
        },
      ],
      rules: {
        name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }],
        exclusive: [
          { required: true, message: '请选择是否排序', trigger: 'change' },
        ],
        ord: [{ required: true, message: '请选择排序', trigger: 'blur' }],
      },
      buttonName: '编辑',
      allowEdit: true,
      dialogVisible: false,
      isSave:false
    };
  },
  props: ['sectionInfo'],

  methods: {
    goBack: function () {
      const vm = this;
      vm.refresh=!vm.refresh;
      vm.editMode = false;
    },
    detailsEdit: function () {
      const vm = this;
      if (vm.buttonName == '编辑') {
        vm.buttonName = '取消';
        vm.allowEdit = false;
      } else {
        vm.buttonName = '编辑';
        vm.allowEdit = true;
      }
    },
    detailsSave: function () {
      const vm = this;
      vm.isSave = true;
      var prma = {
        name: vm.formData.name,
        exclusive: vm.formData.exclusive,
        id: vm.formData.id,
        ord:vm.formData.ord,
      };
      //console.log(prma);
      editDictSection(prma)
        .then((data) => {
          vm.isSave = false;
          if (data.code == 0) {
            if (vm.buttonName == '编辑') {
              vm.buttonName = '取消';
              vm.allowEdit = false;
            } else {
              vm.buttonName = '编辑';
              vm.allowEdit = true;
            }
          }
        })
        .catch((error) => {
          vm.isSave = false;
          console.log(error);
        });
    },
    //删除分组
    detailsDelete: function () {
      const vm = this;
      vm.dialogVisible = false;

      var prma = {
        dicGrpId: vm.sectionInfo.id,
      };
      deleteDictSection(prma)
        .then((data) => {
          //console.log(data);
          if (data.code == 0) {
            vm.$message({
              message: '删除成功',
              type: 'success',
            });
            //vm.$emit('dictDetailsSection', '2');//回调事件
            vm.refresh = !vm.refresh;
          }else{
            vm.$message.error('删除失败，失败原因：' + data.msg);
          }
        })
        .catch((error) => {
          vm.$message.error('删除失败，请联系管理员\n' + error);
        });
    },
  },
  created: function () {},
  computed: {
    formData: function () {
      return this.sectionInfo;
    },
    editMode:{
      get(){
        return udStoreUtil.getStore("dic.goList.editMode")
      },
      set(val){
        udStoreUtil.setStore("dic.goList.editMode",val)
      }
    },
    refresh:{
      get(){
        return udStoreUtil.getStore("dic.goList.refresh")
      },
      set(val){
        return udStoreUtil.setStore("dic.goList.refresh",val)
      },
    },
  },
};
</script>

<style scoped>
.section {
  /* width: 100%; */
  height: 100%;
  min-width: 800px;
  /* background-color: chocolate; */
}
.sectionHeader {
  padding: 1rem 2rem;
  border-bottom: 1px solid #eeeeee;
}
.sectionDetails {
  max-height: 1200px;
  width: 100%;
  /* background-color:darkseagreen */
}
.detailsHeader {
  width: 100%;
  height: 60px;
  min-width: 800px;
  /* background-color: cadetblue; */
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  align-items: center;
}
.button1 {
  width: 200px;
  /* background-color: chocolate; */
}
.button2 {
  width: 100px;
  /* background-color: cornflowerblue; */
  display: flex;
  justify-content: center;
  align-items: center;
}
.detailsbody {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  /* background-color: floralwhite; */
}
.detailsbodyFrom {
  height: 100%;
  width: 400px;
}
</style>
