<template>
  <div class="addDict">
    <el-form label-width="85px" label-position="left" :model="formData" :rules="rules" ref="formData">
      <el-form-item label="分组名称" prop="sectionName">
        <el-input v-model="formData.sectionName" style="width: 100%;"></el-input>
      </el-form-item>
      <el-form-item label="是否排他" prop="value">
        <el-select v-model="formData.value" placeholder="请选择" style="width: 100%;">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="排序号" prop="num">
        <el-input-number
          v-model="formData.num"
          :min="0"
          style="width: 100%;"
        ></el-input-number>
      </el-form-item>
      <el-form-item>
        <div class="from_action">
          <el-button type="primary" @click="submitForm('formData')" :loading='isSave'>
            保存
          </el-button>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addDictSection} from '../../../../api/dict'
import udStoreUtil from "../../../../utils/udStoreUtil";

export default {
  name: 'addDict',
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
      formData: {
        sectionName: '',
        value: '',
        num: 0,
      },
      rules: {
        sectionName: [
          { required: true, message: '请输入分组名称', trigger: 'blur' },
        ],
        value: [
          { required: true, message: '请选择是否排他', trigger: 'change' },
        ],
        num: [{ required: false, message: '请选择排序', trigger: 'blur' }],
      },
      isSave:false
    };
  },
  computed:{
    addDicVo:{
      get(){
        return udStoreUtil.getStore("dic.goAdd.dicGrpVo")
      },
      set(val){
        udStoreUtil.setStore("dic.goAdd.dicGrpVo",val)
      }
    }
  },
  created() {
    const vm = this;
    vm.formData.sectionName = vm.addDicVo.name;
    vm.formData.value = vm.addDicVo.exclusive;
    vm.formData.num = vm.addDicVo.ord;
  },
  methods: {
    submitForm: function (formData) {
      const vm = this;
      vm.isSave = true;
      vm.$refs[formData].validate((valid) => {
        if (valid) {
          //console.log(vm.formData);

          var prma = {
            name:vm.formData.sectionName,
            exclusive:vm.formData.value,
            ord:vm.formData.num
          }
          addDictSection(prma).then((data=>{
            //console.log(data);
            vm.isSave = false;
            vm.$emit('addDictSection', vm.formData);
          })).catch((error=>{
            vm.isSave = false;
            console.log(error)
          }))
        } else {
          return false;
        }
      });
    },
  },
};
</script>

<style lang="scss">

.addDict {
  .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label::before, .el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label::before{
    content: '';
    margin-right: 0;
  }
  .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label::before, .el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label::after{
    content: '*';
    color: #f56c6c;
    margin-left: 4px;
  }
  width: 100%;
  /* background-color: chartreuse; */
  display: flex;
  justify-content: center;
  .from_action {
    width: 100%;
    /* background-color: chartreuse; */
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
}

</style>
