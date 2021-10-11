<template>
  <div class="dictKeyEdit">
    <div class="keyForm">
      <div>
        <el-form
          label-width="120px"
          :model="formData"
          :rules="rules"
          ref="formData"
          class="keyFormC"
        >
          <el-form-item label="键：" prop="key">
            <el-input
              v-model="formData.key"
              style="width: 100%"
              :disabled="isEdit"
            ></el-input>
          </el-form-item>
          <el-form-item label="值：" prop="val">
            <el-input
              v-model="formData.val"
              style="width: 100%"
              :disabled="isEdit"
            ></el-input>
          </el-form-item>
          <!-- <el-form-item label="字典描述：" prop="dictMark">
            <el-input
              v-model="formData.dictMark"
              style="width: 260px"
              type="textarea"
              :rows="3"
              placeholder="请输入内容"
            ></el-input>
          </el-form-item> -->
          <el-form-item label="是否过期：" prop="deprecated">
            <el-select
              v-model="formData.deprecated"
              placeholder="请选择"
              style="width: 100%"
              :disabled="isEdit"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="序号：">
            <el-input-number style="width: 100%" v-model="formData.ord" :min="0"></el-input-number>
          </el-form-item>
          <!-- <el-form-item label="创建时间：">
            <div style="color: #666666">
              {{ formData.createT }}
            </div>
          </el-form-item> -->
          <el-form-item label="更新时间：">
            <div style="color: #666666">
              {{ formData.lastUpdate }}
            </div>
          </el-form-item>
          <el-form-item label="更新人：">
            <div style="color: #666666">
              {{ formData.updateBy }}
            </div>
          </el-form-item>
          <el-form-item>
            <el-button style="float: right;" type="primary" @click="submitForm('formData')" :loading='isSave'>保存</el-button>
          </el-form-item>

        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
import { editDict } from '../../../../api/dict';

export default {
  name: 'dictKeyEdit',
  data() {
    return {
      formData: this.keyInfo,
      isEdit: false,
      rules: {
        key: [{ required: true, message: '请输入键', trigger: 'blur' }],
        val: [{ required: true, message: '请输入值', trigger: 'blur' }],
        // dictMark: [{ required: false, message: '请选择排序', trigger: 'blur' }],
        deprecated: [
          // { required: true, message: '请选择是否过期', trigger: ['change'] },
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
      isSave:false
    };
  },
  props: ['keyInfo'],
  methods: {
    submitForm: function (formData) {
      const vm = this;
      vm.isSave = true;
      vm.$refs[formData].validate((valid) => {
        if (valid) {
          var item = {
            key: vm.formData.key,
            val: vm.formData.val,
            deprecated: vm.formData.deprecated,
            dicGrpId: vm.keyInfo.sectionId,
            ord:vm.formData.ord,
            id: vm.formData.id,
          };
          vm.saveDict(item);
        } else {
          return false;
        }
      });
    },
    //编辑保存
    saveDict: function (prma) {
      //console.log(prma);
      const vm = this;
      editDict(prma)
        .then((data) => {
          vm.isSave = false;
          if (data.code == 0) {
            vm.$message({
              message: '保存成功',
              type: 'success',
            });
            //向父组件发出通知，返回
            vm.$emit('editOK', '1');
          }else{
            vm.isSave = false;
            vm.$message.error('保存失败，失败原因：' + data.msg);
          }
        })
        .catch((error) => {
          vm.$message.error('保存失败，请联系管理员\n' + error);
        });
    },
  },
};
</script>

<style>
.dictKeyEdit {
  width: 100%;
}
.saveHeader {
  width: 95%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.keyForm {
  width: 100%;
  min-height: 600px;
  max-height: 1200px;
  display: flex;
  justify-content: center;
  align-items: center;
  /* background-color: azure; */
}
.keyFormC {
  width: 400px;
  height: 100%;
  /* background-color: blueviolet; */
}
</style>
