<template>
  <div class="sysSetting">
    <el-row>
      <el-card class="box-card" shadow="hover">
        <div slot="header" class="clearfix">
          <span>登陆设置</span>
        </div>
        <div class="sys_login_setting">
          <el-row style="padding: 6px 0px">
            <el-col :xs="15" :sm="10" :md="6" :lg="5" :xl="3">
              <span>连续输入错误锁定账号：</span>
            </el-col>
            <el-col :xs="9" :sm="14" :md="18" :lg="19" :xl="21">
              <el-switch v-model="isLocking" @change="loginLockEvent" style="width:50px"></el-switch>
            </el-col>
          </el-row>
          <el-row style="padding: 6px 0px;display: none;">
            <el-col :xs="15" :sm="10" :md="6" :lg="5" :xl="3">
              <span>上次登录信息提示：</span>
            </el-col>
            <el-col :xs="9" :sm="14" :md="18" :lg="19" :xl="21">
              <div class="tipsCol">
                <el-switch v-model="isTips" @change="loginTipsEvent"></el-switch>
                <el-image class="tipsImage" :src="src">
                  <div slot="error">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-row>
    <el-row>
      <el-card class="box-card" shadow="hover">
        <div slot="header" class="clearfix">
          <span>密码策略</span>
          <!-- <el-button style="padding: 3px 0" type="text">操作按钮</el-button> -->
        </div>
        <div class="sys_paw_setting">
          <el-row style="padding: 6px 0px;display: none;">
            <el-col :xs="15" :sm="10" :md="6" :lg="5" :xl="3">
              <span>初始密码强制修改：</span>
            </el-col>
            <el-col :xs="9" :sm="14" :md="18" :lg="19" :xl="21">
              <el-switch v-model="isPawEnforcement" @change="pawEnforcementEvent"></el-switch>
            </el-col>
          </el-row>
          <el-row style="padding: 6px 0px">
            <el-col :xs="15" :sm="10" :md="6" :lg="5" :xl="3">
              <span>密码强度限制：</span>
            </el-col>
            <el-col :xs="9" :sm="14" :md="18" :lg="19" :xl="21">
              <div class="tipsCol">
                <el-switch v-model="isPawLimit" @change="pawLimitEvent"></el-switch>
                <div class="pawLimit_check">
                  <div class="pawLimit">
                    <el-checkbox v-model="min_flg" label="最小长度" @change="minLengthCheckboxEvent" :disabled='!isPawLimit'>
                    </el-checkbox>
                    <el-checkbox label="包含数字" v-model="cd" @change="cdCheckboxEvent" :disabled='!isPawLimit'></el-checkbox>
                    <el-checkbox label="包含小写字母" v-model="cll" @change="cllCheckboxEvent" :disabled='!isPawLimit'></el-checkbox>
                    <el-checkbox label="包含大写字母" v-model="cul" @change="culCheckboxEvent" :disabled='!isPawLimit'></el-checkbox>
                    <el-checkbox label="包含符号" v-model="cc" @change="ccCheckboxEvent" :disabled='!isPawLimit'></el-checkbox>
                    <el-checkbox label="不包含用户名" v-model="ncu" @change="ncuCheckboxEvent" :disabled='!isPawLimit'></el-checkbox>
                  </div>
                  <div class="pawLimitRight">
                      <el-input-number style="width:120px" v-model="pawNums" :disabled='!min_flg' @change="minLengthEvent" controls-position="right"></el-input-number>
                      <span v-html="'\u00a0\u00a0'"></span><span>位</span>
                  </div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-row>
    <el-row style="display: none;">
      <el-card class="box-card" shadow="hover">
        <div slot="header" class="clearfix">
          <span>账号设置</span>
        </div>
        <div class="sys_account_setting">
          <el-row>
            <el-col :xs="15" :sm="10" :md="6" :lg="5" :xl="3">
              <div class="account_key">
                <span>批量导入用户的默认密码：</span>
              </div>
            </el-col>
            <el-col :xs="9" :sm="14" :md="18" :lg="19" :xl="21">
              <el-input
                v-model="defaultPwa"
                show-password
                style="width: 160px"
                @change="accountCheckboxEvent"
              ></el-input>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import {getSettingInfo,saveSettingInfo} from '../../../../api/setting';

export default {
  name: 'sysSetting',
  data() {
    return {
      isLocking: false,
      isTips: false,
      src: 'https://cube.elemecdn.com/6/94/4d3ea53c084bad6931a56d5158a48jpeg.jpeg',
      isPawEnforcement: false,
      isPawLimit: false,
      defaultPwa: '123456',
      pawNums:6,
      checkList:[],
      settingList:[],
      min_length:false,
      min_flg:false,
      cd:false,
      cll:false,
      cul:false,
      cc:false,
      ncu:false
    };
  },
  methods:{
    getSetting:function(){
      const vm = this;
      getSettingInfo().then((data=>{
        //console.log(data);
        if(data.code == 0){
          vm.settingList = data.result;
          let item1 = data.result[0].val;
          let item2 = data.result[1].val;
          let item3 = data.result[2].val;

          let loginInfo = JSON.parse(item1);
          let pawInfo = JSON.parse(item2);
          let accountInfo = JSON.parse(item3)

          vm.isLocking = loginInfo.swpla;
          vm.isTips = loginInfo.lli;
          vm.isPawEnforcement = pawInfo.opfu;
          vm.isPawLimit = pawInfo.psr.flg;
          vm.pawNums = pawInfo.psr.min_length;
          vm.min_flg = pawInfo.psr.min_flg;
          vm.cd = pawInfo.psr.cd;
          vm.cll = pawInfo.psr.cll;
          vm.cul = pawInfo.psr.cul;
          vm.cc = pawInfo.psr.cc;
          vm.ncu = pawInfo.psr.ncu;
          vm.defaultPwa = accountInfo.dp;

        }
      })).catch((error=>{
        vm.$message.error('获取信息失败，请联系管理员\n' + error);
      }))
    },
    editSetting:function(prma){
      const vm = this;
      saveSettingInfo(prma).then((data=>{
        if(data.code == 0){
          // vm.$message({
          //     message: '修改成功',
          //     type: 'success',
          //   });
        }else{
          vm.isLocking = !vm.isLocking;
          //vm.$message.error('修改失败：' + data.msg);
        }
      })).catch((error=>{
        vm.isLocking = !vm.isLocking;
        //vm.$message.error('修改失败，请联系管理员\n' + error);
      }))
    },
    checkboxEv:function(val){
    },
    loginLockEvent:function(val){
      const vm = this;
      var item = {"swpla":val,"lli":vm.isTips};
      var prma = {
        id:1,
        key:'login',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    loginTipsEvent:function(val){
      const vm = this;
      var item = {"swpla":vm.isLocking,"lli":val};
      var prma = {
        id:1,
        key:'login',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    pawEnforcementEvent:function(val){
      const vm = this;
      var item = {"opfu":val,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};
      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    pawLimitEvent:function(val){
      const vm = this;
      if(!val){
        vm.isPawLimit = false;
        vm.min_flg = false;
        vm.cd = false;
        vm.cll = false;
        vm.cul = false;
        vm.cc = false;
        vm.ncu = false;
      }
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    minLengthEvent:function(val){
      //console.log(val);
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    minLengthCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    cdCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    cllCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    culCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    ccCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    ncuCheckboxEvent:function(val){
      const vm = this;
      var item = {"opfu":vm.isPawEnforcement,"psr":{"flg":vm.isPawLimit,"min_length":vm.pawNums,"min_flg":vm.min_flg,"cd":vm.cd,"cll":vm.cll,"cul":vm.cul,"cc":vm.cc,"ncu":vm.ncu}};

      var prma = {
        id:2,
        key:'password',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
    accountCheckboxEvent:function(val){
      const vm = this;
      var item = {"dp":vm.defaultPwa};

      var prma = {
        id:3,
        key:'account',
        val:JSON.stringify(item)
      }
      vm.editSetting(prma);
    },
  },
  created(){
    const vm = this;
    vm.getSetting();
  }
};
</script>

<style scoped>
.sysSetting {
  /* background-color: antiquewhite; */
  padding: 16px;
}
.tipsImage {
  width: 200px;
  height: 200px;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: '';
}
.clearfix:after {
  clear: both;
}

.box-card {
  margin-bottom: 16px;
}
.tipsCol {
  display: flex;
  flex-direction: column;
  height: 230px;
  justify-content: space-between;
}
.account_key {
  display: flex;
  height: 40px;
  justify-content: flex-start;
  align-items: center;
}
.pawLimit{
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  height: 100%;
  /* background-color: brown; */
}
.pawLimit_check{
  height: 200px;
  /* background-color: antiquewhite; */
  display: flex;
  align-items: flex-start;
  flex-direction: row;
}
.pawLimitRight{
  /* background-color: aquamarine; */
}
</style>
