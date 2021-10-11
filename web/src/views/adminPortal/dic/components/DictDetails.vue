<template>
  <div class="dictDetails">
    <template v-if="!isEdit">
      <div class="detail_header">
        <el-row type="flex" justify="space-between">
          <div class="searchDiv">
            <el-input
              placeholder="请输入字典名称"
              prefix-icon="el-icon-search"
              v-model="search"
              clearable
              @clear="inputClerClick"
              style="margin-right: .5rem"
            ></el-input>
            <el-button @click="searchDict">
              查询
            </el-button>
          </div>
          <div class="addButton">
            <el-button type="primary" @click="goAdd">
              添加
            </el-button>
          </div>
        </el-row>
        <el-dialog title="添加字典" :visible.sync="addVisible" width="500px">
          <div class="addDicts">
            <el-form
              label-width="85px"
              :model="formData"
              :rules="rules"
              ref="formData"
            >
              <el-form-item label="键" prop="dictKey">
                <el-input
                  v-model="formData.dictKey"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item label="值" prop="dictValue">
                <el-input
                  v-model="formData.dictValue"
                  style="width: 100%"
                ></el-input>
              </el-form-item>
              <el-form-item label="序号" prop="dictValue">
                <el-input-number
                        :min="0"
                        v-model="formData.ord"
                        style="width: 100%"
                ></el-input-number>
              </el-form-item>
              <!-- <el-form-item label="字典描述" prop="dictMark">
                <el-input
                  v-model="formData.dictMark"
                  style="width: 260px"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入内容"
                ></el-input>
              </el-form-item> -->
              <el-form-item label="是否过期" prop="dictIsDate">
                <el-select
                  v-model="formData.dictIsDate"
                  placeholder="请选择"
                  style="width: 100%"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <div class="from_action">
                  <el-button type="primary" style="float: right;" @click="submitForm('formData')" :loading='isSave'>
                    保存
                  </el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </el-dialog>
      </div>
        <div v-if="dictList.length == 0" style="display: flex;justify-content: center;align-items:center;flex-grow: 1">
          <div style="width: 300px;">
            <el-empty description="暂无数据"></el-empty>
          </div>
        </div>
      <template v-else>
        <div class="dictTable">
          <el-table :data="dictList" stripe max-height="600">
            <el-table-column label="键" align="center">
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.key }}</span>
              </template>
            </el-table-column>
            <el-table-column label="值" align="center">
              <template slot-scope="scope">
                <span style="margin-left: 10px">{{ scope.row.val }}</span>
              </template>
            </el-table-column>
            <el-table-column label="是否过期" align="center">
              <template slot-scope="scope">
                <span style="margin-left: 10px">
                  <template v-if="scope.row.deprecated">是</template>
                  <template v-else>否</template>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)"
                  type="text"
                >
                  编辑
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  @click="handleDelete(scope.$index, scope.row)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </template>
      <el-dialog title="提示" :visible.sync="dialogVisible" width="20%">
        <span>⚠️ 是否删除此字典？</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="deleteDictClick()">确 定</el-button>
        </span>
      </el-dialog>
    </template>
    <template v-else>
      <div class="keyHeader">
        <el-page-header @back="goBack" :content="itemInfo.name+' _ '+editKeyInfo.key">
        </el-page-header>
      </div>
      <div class="keyBody">
        <dict-key-edit
          :keyInfo="editKeyInfo"
          @editOK="dictEditOk"
        ></dict-key-edit>
      </div>
    </template>
  </div>
</template>

<script>
import DictKeyEdit from './DictKeyEdit.vue';
import { getDict, addDict, deleteDict } from '../../../../api/dict';
import udStoreUtil from "../../../../utils/udStoreUtil";

export default {
  name: 'detail',
  components: { DictKeyEdit },
  data() {
    return {
      dictList: [],
      search: '',
      addVisible: false,
      currentRow: {},
      currentRowIndex: 0,
      formData: {
        dictKey: '',
        dictValue: '',
        dictMark: '',
        ord:0,
        dictIsDate: false,
      },
      rules: {
        dictKey: [{ required: true, message: '请输入键名称', trigger: 'blur' }],
        dictValue: [
          { required: true, message: '请输入值名称', trigger: 'blur' },
        ],
        // dictMark: [{ required: false, message: '请选择排序', trigger: 'blur' }],
        dictIsDate: [
          { required: true, message: '请选择是否过期', trigger: ['change'] },
        ],
      },
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
      dialogVisible: false,
      isEdit: false,
      editKeyInfo: {},
      isSave:false
    };
  },
  props: ['itemInfo'],
  computed: {
    refresh:{
      get(){
        return udStoreUtil.getStore("dic.goList.refresh")
      },
      set(val){
        udStoreUtil.setStore("dic.goList.refresh",val)
      }
    }
  },
  methods: {
    goAdd(){
      const vm = this;
      vm.formData.ord = vm.dictList.length +1;
      vm.addVisible = true;
    },
    submitForm: function (formData) {
      const vm = this;
      vm.isSave = true;
      vm.$refs[formData].validate((valid) => {
        if (valid) {
          var item = {
            key: vm.formData.dictKey,
            val: vm.formData.dictValue,
            ord:vm.formData.ord,
            deprecated: vm.formData.dictIsDate,
            dicGrpId: vm.itemInfo.id,
          };
          vm.addDictInfo(item);
        } else {
          return false;
        }
      });
    },
    handleEdit: function (index, row) {
      const vm = this;
      vm.editKeyInfo = row;
      vm.editKeyInfo.sectionId = vm.itemInfo.id;
      vm.isEdit = true;
    },
    handleDelete: function (index, row) {
      const vm = this;
      vm.dialogVisible = true;
      vm.currentRow = row;
      vm.currentRowIndex = index;
    },
    deleteDictClick: function () {
      const vm = this;
      var prma = {
        sysDicId: vm.currentRow.id,
      };
      vm.dictKeyDelete(prma);
    },
    dictKeyDelete: function (prma) {
      const vm = this;
      deleteDict(prma)
      .then((data) => {
          if (data.code == 0) {
            vm.$message({
              message: '删除成功',
              type: 'success',
            });
            var prma = {
              sysDicGrpId: this.itemInfo.id,
              name: ''
            };
            vm.getDictLists(prma);
            vm.dialogVisible = false;
          } else {
            vm.$message.error('删除失败，失败原因：' + data.msg);
          }
        })
        .catch((error) => {
          vm.$message.error('删除失败，请联系管理员\n' + error);
        });
    },
    goBack: function () {
      const vm = this;
      vm.isEdit = false;
    },
    addDictInfo: function (prma) {
      const vm = this;
      addDict(prma)
        .then((data) => {
          //console.log(data);
          vm.isSave = false;
          if (data.code == 0) {
            vm.$message({
              message: '添加成功',
              type: 'success',
            });
            vm.addVisible = false;
            vm.formData.dictKey = '';
            vm.formData.dictValue = '';
            var prma = {
              sysDicGrpId: vm.itemInfo.id,
              name: '',
            };
            //console.log(prma);
            vm.getDictLists(prma);
          } else {
            this.$message.error('添加失败，失败原因：' + data.msg);
          }
        })
        .catch((error) => {
          vm.isSave = false;
          this.$message.error('添加失败，请联系管理员\n' + error);
        });
    },
    getDictLists: function (prma) {
      const vm = this;
      getDict(prma)
        .then((data) => {
          //console.log(data);
          if (data.code == 0) {
            vm.dictList = data.result.sysDicList;
          } else {
            this.$message.error('字典查询错误，错误原因：' + data.msg);
          }
        })
        .catch((error) => {
          this.$message.error('字典查询错误，请联系管理员\n' + error);
        });
    },
    dictEditOk: function (val) {
      if (val == '1') {
        const vm = this;
        vm.isEdit = false;
      }
    },
    searchDict:function(){
      const vm = this;
      var prma = {
        sysDicGrpId: this.itemInfo.id,
        name: vm.search,
      };
      vm.getDictLists(prma);
    },
    //输入框清空事件
    inputClerClick:function(){
      const vm = this;
      var prma = {
        sysDicGrpId: this.itemInfo.id,
        name: ''
      };
      vm.getDictLists(prma);
    }
  },
  watch: {
    itemInfo: function (val) {
      const vm = this;
      var prma = {
        sysDicGrpId: val.id,
        name: '',
      };
      vm.getDictLists(prma);
    },
  },
};
</script>

<style lang="scss">

.dictDetails {
  display: flex;
  flex-direction: column;
  width: calc(100% - 2rem);
  height: 100%;
  margin-left: 1rem;
  .el-empty__description{
    display: flex;
    justify-content: center;
    font-size: 14px;
    color: #5e6d82;
    line-height: 1.5em;
  }
  .searchDiv {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    /* background-color: chocolate; */
  }
  .addButton {
    /* width: 100px; */
    /* background-color: coral; */
  }
  .addDicts {
    width: 100%;
    /* background-color: chartreuse; */
    display: flex;
    justify-content: center;
  }
  .detail_header {
    /*margin: 16px 0px 8px 0px;*/
    padding: 0px 16px .5rem 16px;
    /* background-color: beige; */
  }
  .dictTable {
    width: calc(100% - 2rem);
    margin-left: 1rem;
    border-left: 1px solid #eee;
    border-right: 1px solid #eee;
    border-top: 1px solid #eee;
  }
  .keyHeader {
    height: 40px;
    border-bottom: 1px solid #eeeeee;
    /* background-color: cornsilk; */
  }
  .keyBody {
    width: 100%;
  }
}

</style>
