<template>
    <!--    <div>-->
    <!--        v-if="srcAllHint===false"-->
    <div class="sys-src-all">
        <div class="container" v-if="isNews==1">
            <div class="left">
                <div class="navHeader">
                    <el-button-group style="display: flex; padding-bottom: .5rem;padding-right: 10px;">
                        <el-button style="flex-grow: 1" @click="addSectionClick" icon="el-icon-plus">添加分组
                        </el-button>
                        <el-button style="flex-grow: 1" :type="editGroup?'primary':'normal'"
                                   @click="sectionMaintainClick" icon="el-icon-edit">编辑分组
                        </el-button>
                    </el-button-group>
                </div>
                <div class="nav-footer-wrapper">
                    <div class="navfloter">
                        <template v-for="(item, index) in srcList">
                            <div :key="item.id" @click="butItemSection(item)"
                                 :class="{navTtem:true,active: item.id ===  statusType}">
                                <i style="padding-top:9px; padding-right:.5rem; color: darkgray;"
                                   class="el-icon-s-tools"></i>
                                {{ item.name }}
                            </div>
                        </template>
                    </div>
                </div>
            </div>
            <div class="right">
                <div v-if="srcAllHint===false">
                    <div class="rightView" v-if="isNewsT==false">
                        <div style="display: flex;flex-direction: row;padding: 20px">
                            <el-input
                                    placeholder="请输入资源名称"
                                    v-model="inputSrcName"
                                    style="width: 220px;margin-right: 20px"
                                    clearable>
                            </el-input>
                            <el-button type="primary" @click="searchSrcName">查询</el-button>
                            <div style="margin-left: auto">
                                <el-button style="width: 100px" @click="addContent">添 加</el-button>
                                <el-button style="width: 100px" @click="moveContent">移 动</el-button>
                            </div>
                        </div>
                        <div class="tableView">
                            <el-table
                                    ref="multipleTable"
                                    :data="tableData"
                                    tooltip-effect="dark"
                                    style="width: 100%"
                                    @selection-change="selectChange">
                                <el-table-column
                                        type="selection"
                                >
                                </el-table-column>
                                <el-table-column
                                        prop="ord"
                                        label="排序号"
                                        width="100">
                                </el-table-column>
                                <el-table-column
                                        prop="name"
                                        label="资源名称"
                                        width="160">
                                </el-table-column>
                                <el-table-column
                                        prop="code"
                                        label="资源编码"
                                        width="200">
                                </el-table-column>
                                <el-table-column
                                        prop="des"
                                        label="描述">
                                    <template slot-scope="scope">
                                        <el-popover trigger="hover" placement="top">
                                            <p>描述: {{ scope.row.des }}</p>
                                            <div slot="reference">
                                                <span>{{ scope.row.des }}</span></div>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="deprecated"
                                        label="是否过期"
                                        width="120">
                                    <template slot-scope="scope">
                                        <span>{{scope.row.deprecated=='1'? '是':'否'}}</span>
                                    </template>
                                </el-table-column>
                                <el-table-column
                                        prop="operation"
                                        label="操作"
                                        width="100"
                                        show-overflow-tooltip>
                                    <template slot-scope="scope">
                                        <el-button @click="detailClick(scope.row)" type="text">详情</el-button>
                                        <el-button type="text" @click="deleteClick(scope.row)">删除</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </div>
                    </div>

                    <div v-if="isNewsT==true" v-bind:class="butStatus?'selectInputStyle':'rightView'">
                        <div style="display: flex;flex-direction: column;padding: 20px">
                            <div style="display: flex;justify-content: space-between;padding-bottom: 1rem;">
                                <el-button style="width: 80px" type="primary" @click="backPage">返 回</el-button>
                                <div style="margin-left: auto">
                                    <el-button style="width: 100px" @click="editClick">{{butStatus ?'编 辑':'取 消'}}
                                    </el-button>
                                    <el-button style="width: 100px" @click="saveClick">保 存</el-button>
                                </div>
                            </div>
                            <div style="width:100%;">
                                <el-form :model="formDetail" style="margin-left: 20%">
                                    <el-form-item label="资源名称:" :label-width="formLabelWidth">
                                        <el-input v-model="formDetail.name" :disabled="readOnly"></el-input>
                                    </el-form-item>
                                    <el-form-item label="资源编号:" :label-width="formLabelWidth">
                                        <el-input v-model="formDetail.code" :disabled="readOnly"></el-input>
                                    </el-form-item>
                                    <el-form-item label="资源序号:"  :label-width="formLabelWidth">
                                        <el-input-number v-model="formDetail.ord" :min="0"  style="width:100%;"
                                                         :disabled="readOnly">
                                        </el-input-number>
                                    </el-form-item>
                                    <el-form-item label="资源描述:" :label-width="formLabelWidth">
                                        <el-input type="textarea" v-model="formDetail.des"
                                                  :disabled="readOnly"></el-input>
                                    </el-form-item>
                                    <el-form-item label="是否过期:" :label-width="formLabelWidth">
                                        <el-select style="width: 100%;" v-model="formDetail.deprecated" placeholder="请选择是否过期"
                                                   :disabled="readOnly">
                                            <el-option label="是" value='1'></el-option>
                                            <el-option label="否" value='2'></el-option>
                                        </el-select>
                                    </el-form-item>
                                    <el-form-item label="创建时间:" :label-width="formLabelWidth">
                                        <span>{{formDetail.createTime}}</span>
                                    </el-form-item>
                                    <el-form-item label="更新时间:" :label-width="formLabelWidth">
                                        <span>{{formDetail.lastUpdate}}</span>
                                    </el-form-item>
                                    <el-form-item label="更新人:" :label-width="formLabelWidth">
                                        <span>{{formDetail.updateBy}}</span>
                                    </el-form-item>
                                </el-form>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else>
                    <group-src :contentSrc="groupContent" v-on:childHint="childByHint"
                               v-on:sectionListRule="listRule" v-on:groupListRef="refOn" ></group-src>
                </div>
            </div>
        </div>
        <div class="srcSection1" v-else-if="isNews==2">
            <div style="padding:20px">
                <span style="font-size: 18pt">资源分组维护</span>
            </div>
            <div class="srcButon">
                <el-button @click="addGroup">添加分组
                    <i class="el-icon-circle-plus-outline el-icon--right"></i>
                </el-button>
            </div>
        </div>
        <div class="srcSection1" v-else-if="isNews==3">
            <div class="srcButon">
                <el-button @click="addGroup">资源分组维护
                </el-button>
            </div>
        </div>

        <el-dialog title="添加资源分组" :visible.sync="dialogFormVisible" width="500px">
            <div class="addDict">
                <el-form :model="form" :rules="groupRules" ref="form">
                    <el-form-item label="分组名称" :label-width="formLabelWidth" prop="name">
                        <el-input class="inputWidth" v-model="form.name" style="width: 100%;" placeholder='请输入分组名称'></el-input>
                    </el-form-item>
                    <el-form-item label="排序号" :label-width="formLabelWidth" prop="num">
                        <el-input-number v-model="form.num" style="width: 100%;" :min="0"></el-input-number>
                    </el-form-item>
                    <div style="float:right;" class="dialog-footer">
                        <el-button @click="dialogFormVisible = false">取 消</el-button>
                        <el-button type="primary" @click="addSave('form')">保 存</el-button>
                    </div>
                </el-form>
            </div>
        </el-dialog>
        <el-dialog title="添加资源内容" :visible.sync="dialogFormVisibleContent" width="500px">
            <div class="addDict">
                <el-form :model="formContent" :rules="contentRules" ref="formContent">
                    <el-form-item label="资源名：" :label-width="formLabelWidth" prop="name">
                        <el-input class="inputWidth" v-model="formContent.name" placeholder='请输入资源名称'></el-input>
                    </el-form-item>
                    <el-form-item label="资源编码：" :label-width="formLabelWidth" prop="code">
                        <el-input class="inputWidth" v-model="formContent.code" placeholder='请输入资源编码'></el-input>
                    </el-form-item>
                    <el-form-item label="资源排序：" :label-width="formLabelWidth">
                        <el-input-number class="inputWidth" v-model="formContent.ord" :min="0" style="width: 100%;">
                        </el-input-number>
                    </el-form-item>
                    <el-form-item label="资源描述：" :label-width="formLabelWidth">
                        <el-input type="textarea" class="inputWidth" v-model="formContent.src"
                                  placeholder='请输入资源描述'></el-input>
                    </el-form-item>
                    <div style="float:right;" class="dialog-footer">
                        <el-button @click="dialogFormVisibleContent = false">取 消</el-button>
                        <el-button type="primary" @click="addContentSave('formContent')">保 存</el-button>
                    </div>
                </el-form>
            </div>
        </el-dialog>
        <el-dialog
                title="提示"
                :visible.sync="centerDialogVisible"
                width="500px"
                center>
            <span>{{message}}</span>
            <span slot="footer" class="dialog-footer">
    <el-button @click="centerDialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="messageClick">确 定</el-button>
  </span></el-dialog>
        <el-dialog title="用户移动" :visible.sync="dialogFormMove" width="500px">
            <el-form :model="form">
                <el-form-item label="移动到分组" :label-width="formLabelWidth">
                    <el-select v-model="moveSection" placeholder="请选择移动分组">
                        <div v-for="item in srcList">
                            <el-option :label="item.name" :value="item.id"></el-option>
                        </div>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormMove = false">取 消</el-button>
                <el-button type="primary" @click="moveClick">确 定</el-button>
            </div>
        </el-dialog>
    </div>
    <!--        <div v-else>-->
    <!--            <group-src v-on:childHint="childByHint"></group-src>-->
    <!--        </div>-->
    <!--    </div>-->
</template>
<script>
    import SysSrc_ from "./SysSrc_"

    export default SysSrc_
</script>
<style lang="scss">
    .sys-src-all {
        height: calc(100% - 20px);
        width: calc(100% - 20px);
        margin-top: 10px;
        margin-left: 10px;
        display: flex;

        .container {
            height: 100%;
            width: 100%;
            display: flex;

            .left {
                width: 230px;
                border-right: 2px dashed darkgray;
                display: flex;
                flex-direction: column;
                align-items: stretch;

                .navHeader {
                    .el-button {
                        padding: 12px 15px;
                    }
                }

                .nav-footer-wrapper {
                    padding-right: 2px;
                    flex-grow: 1;
                    overflow: scroll;
                    scrollbar-width: thin;

                    .navfloter {
                        border: 1px solid rgba(0, 0, 0, 0.12);

                        .active {
                            background-color: #e0f2f1 !important;
                        }

                        .navTtem {
                            box-sizing: border-box;
                            display: flex;
                            height: 48px;
                            font-size: 14px;
                            line-height: 32px;
                            padding: 8px 16px;
                            border-bottom: 1px solid rgba(0, 0, 0, 0.12);
                            transition: color 0.3s, background-color 0.3s;
                            color: #606266;
                        }

                        .navTtem:last-child {
                            border-bottom: none;
                        }

                        .navTtem:hover {
                            background-color: #e4e4e4;
                        }
                    }
                }
            }

            .right {
                scrollbar-width: thin;
                flex-grow: 1;

                .rightView {
                    /*min-width: 800px;*/
                    background-color: #FFFFFF;

                }

                .tableView {
                    height: 100%;
                    min-width: 800px;
                    .el-table__body-wrapper{
                        scrollbar-width:thin;
                    }
                }
            }
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

        .srcSection1 {
            height: 600px;
            width: 100%;
            background: #ffffff;

        }

        .srcButon {
            display: flex;
            height: 100%;
            justify-content: center;
            align-items: center;
        }

        .srcButon button {
        }
    }
</style>
