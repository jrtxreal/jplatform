<template>
    <div class="groupAll">
        <div class="srcSection">
<!--            <el-row :gutter="10">-->
<!--                <el-col :span="4">-->
<!--                    <div class="leftViewG">-->
<!--                        <div style="padding: 10px;font-size: 13pt"><span>资源分组维护</span></div>-->
<!--                        <div style="padding: 10px 10px 30px 10px">-->
<!--                            <el-button style="width: 100%;font-size: 12pt" @click="addSectionClick">资源分组添加<i-->
<!--                                    class="el-icon-circle-plus-outline"></i></el-button>-->
<!--                        </div>-->
<!--                        <div style="height: 700px;overflow: auto">-->
<!--                            <div v-for='(item,index) in srcList' style="padding:0px 10px 0px 10px;">-->
<!--                                <el-button style="width: 100%;border: 0px;display: flex;justify-content: flex-start "-->
<!--                                           :type="(item.id ==statusType)?'primary':'normal'"-->
<!--                                           icon="el-icon-document" @click="butItemSectionG(item)">{{item.name}}-->
<!--                                </el-button>-->
<!--                            </div>-->
<!--                        </div>-->
<!--                    </div>-->
<!--                </el-col>-->
<!--                <el-col :span="20">-->
                    <div v-bind:class="butStatus?'selectInputStyleG':'rightViewG'">
                        <div style="display: flex;flex-direction: column;padding: 20px">
                            <el-button style="width: 80px" type="primary" @click="backPageG">返 回</el-button>
                            <div style="margin-left: auto">
                                <el-button style="width: 100px" @click="editClick">{{butStatus ?'编 辑':'取 消'}}
                                </el-button>
                                <el-button v-if="butStatus==false" style="width: 100px" @click="saveClick">保 存
                                </el-button>
                                <el-button style="width: 100px" @click="deleteClick">删 除</el-button>
                            </div>
                            <div style="width: 60%;position:absolute;top: 30%;left:30% ;min-width: 1200px">
                                <el-form :model="formDetailG" style="width: 50%">
                                    <el-form-item label="分组名称:" :label-width="formLabelWidth">
                                        <el-input v-model="formDetailG.name" :disabled="readOnlyG"></el-input>
                                    </el-form-item>
                                    <!--                                    <el-form-item label="是否排他:" :label-width="formLabelWidth">-->
                                    <!--                                        <el-select v-model="formDetailG.past" placeholder="请选择是否过期"-->
                                    <!--                                                   :disabled="readOnlyG">-->
                                    <!--                                            <el-option label="是" value="1"></el-option>-->
                                    <!--                                            <el-option label="否" value="2"></el-option>-->
                                    <!--                                        </el-select>-->
                                    <!--                                    </el-form-item>-->
                                    <el-form-item label="排序号:" :label-width="formLabelWidth">
                                        <el-input type="number" v-model="formDetailG.ord"
                                                  :disabled="readOnlyG"></el-input>
                                    </el-form-item>
                                    <el-form-item label="创建时间:" :label-width="formLabelWidth">
                                        <span>{{formDetailG.createTime}}</span>
                                    </el-form-item>
                                    <el-form-item label="更新时间:" :label-width="formLabelWidth">
                                        <span>{{formDetailG.lastUpdate}}</span>
                                    </el-form-item>
                                    <el-form-item label="更新人:" :label-width="formLabelWidth">
                                        <span>{{formDetailG.updateBy}}</span>
                                    </el-form-item>
                                </el-form>
                            </div>
                        </div>

                    </div>
<!--                </el-col>-->
<!--            </el-row>-->
        </div>
        <!--        <div class="srcSection1" v-else-if="isNews==2">-->
        <!--            <div style="padding:20px">-->
        <!--                <span style="font-size: 18pt">资源分组维护</span>-->
        <!--            </div>-->
        <!--            <div class="srcButon">-->
        <!--                <el-button @click="dialogFormVisibleG = true">添加分组<i-->
        <!--                        class="el-icon-circle-plus-outline el-icon&#45;&#45;right"></i></el-button>-->
        <!--            </div>-->
        <!--        </div>-->
        <!--        <div class="srcSection1" v-else>-->
        <!--            <div class="srcButon">-->
        <!--                <el-button @click="srcManageClickG">资源分组维护<i class="el-icon-d-arrow-right el-icon&#45;&#45;right"></i>-->
        <!--                </el-button>-->
        <!--            </div>-->
        <!--        </div>-->
        <el-dialog
                title="提示"
                :visible.sync="centerDialogVisibleG"
                width="400px"
                center>
            <span>{{message}}</span>
            <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisibleG = false">取 消</el-button>
    <el-button type="primary" @click="messageClickG">确 定</el-button>
  </span>
        </el-dialog>
<!--        <el-dialog title="添加资源分组" :visible.sync="dialogFormVisibleG" width="40%">-->
<!--            <div class="addDict">-->
<!--                <el-form :model="form">-->
<!--                    <el-form-item label="分组名称" :label-width="formLabelWidth">-->
<!--                        <el-input class="inputWidth" v-model="form.name"></el-input>-->
<!--                    </el-form-item>-->
<!--                    &lt;!&ndash;                <el-form-item label="是否排他" :label-width="formLabelWidth">&ndash;&gt;-->
<!--                    &lt;!&ndash;                    <el-input class="inputWidth" v-model="form.past"></el-input>&ndash;&gt;-->
<!--                    &lt;!&ndash;                </el-form-item>&ndash;&gt;-->
<!--                    <el-form-item label="排序号" :label-width="formLabelWidth">-->
<!--                        <el-input-number v-model="form.num" :min="0"></el-input-number>-->
<!--                    </el-form-item>-->
<!--                    <div style="float:right;" class="dialog-footer">-->
<!--                        <el-button @click="dialogFormVisibleG = false">取 消</el-button>-->
<!--                        <el-button type="primary" @click="addSave">保 存</el-button>-->
<!--                    </div>-->
<!--                </el-form>-->
<!--            </div>-->
<!--        </el-dialog>-->

    </div>

</template>

<script>
    import GroupSrc_ from "./GroupSrc_"

    export default GroupSrc_
</script>

<style type="scss">
    .groupAll {
        /*background: #EFF3F6;*/
        /*padding: 20px;*/
    }
        .leftViewG {
            background: #FFFFFF;
            /*height: 800px;*/

        }

        .rightViewG {
            width: 100%;
            background-color: #FFFFFF;
            /*height: 800px;*/

        }

        .selectInputStyleG {
            width: 100%;
            background-color: #FFFFFF;
            /*height: 800px;*/
        }

        .selectInputStyleG input.el-input__inner {
            border: 0px solid #DCDFE6;
            background-color: #FFFFFF !important;

        }

        .addDict {
            .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label::before, .el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label::before {
                content: '';
                margin-right: 0;
            }

            .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label::before, .el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label::after {
                content: '*';
                color: #f56c6c;
                margin-left: 4px;
            }

    }


</style>
