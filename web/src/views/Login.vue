<template>
    <div id="loginPage" class="login">
        <header class="baseheader">
            <div class="left">
                <img class="logo" src="../assets/images/login/logo.png"/>
                <p>XXXXXXX</p>
            </div>
        </header>
        <div class="content">
            <form v-if="loginStyle==false" class="form">
                <label>登录</label>
                <div class="ipt username">
                    <i></i>
                    <input
                            v-model="form.username"
                            type="text"
                            placeholder="请输入用户名"
                            autocomplete="off"
                    />
                </div>
                <div class="ipt password">
                    <i></i>
                    <input
                            type="password"
                            v-model="form.password"
                            placeholder="请输入密码"
                            autocomplete="off"
                    />
                </div>
                <el-row justify="center" type="flex" style="margin-bottom: 10px;">
                    <el-button
                            type="primary"
                            style="width: 360px;height: 40px;"
                            @click="login"
                    >
                        登录
                    </el-button>
                </el-row>
            </form>
            <form v-if="loginStyle==true" class="form">
                <label>登录</label>
                <div class="ipt username">
                    <i></i>
                    <input
                            v-model="form.userIphone"
                            type="text"
                            placeholder="请输入用户名"
                            autocomplete="off"
                    />
                </div>
                <div class="ipt password">
                    <i></i>
                    <input
                            type="password"
                            v-model="form.code"
                            placeholder="请输入密码"
                            autocomplete="off"
                    />
                    <el-button @click="getMsmCodeL"
                            style="width: 200px;color: orange; border:1px solid orange;background-color: white;"
                            :disabled="!show1">
                        <span v-show="show1" style="cursor:pointer">获取验证码</span>
                        <span v-show="!show1" class="count">{{count}} s</span>
                    </el-button>
                </div>
                <el-row justify="center" type="flex" style="margin-bottom: 10px;">
                    <el-button
                            type="primary"
                            style="width: 360px;height: 40px;"
                            @click="loginCode">
                        登录
                    </el-button>
                </el-row>
            </form>

        </div>
        <footer class="basefooter">
            <div class="browser">
                <el-button type="text" @click="imageChangeClick" :style="{color:textColor==true? '#5A5D61':'#4B9FFF'}"
                           style="margin-right: 50px">图片验证码登录
                </el-button>
                <el-button type="text" @click="smsChangeClick" :style="{color:textColor==false? '#5A5D61':'#4B9FFF'}">
                    短信验证码登录
                </el-button>

            </div>
            <!--            <div class="browsers">-->
            <!--                <div class="browser">-->
            <!--                    <img src="../assets/images/login/ic_chrome.png" alt="Chrome浏览器"/>-->
            <!--                    <span>Chrome浏览器</span>-->
            <!--                </div>-->
            <!--                <div class="browser">-->
            <!--                    <img src="../assets/images/login/ic_360.png" alt="360极速浏览器"/>-->
            <!--                    <span>360极速浏览器</span>-->
            <!--                </div>-->
            <!--            </div>-->
            <div class="copyright">
                <p>copyright&copy;2021中铁工程装备集团有限公司</p>
            </div>
        </footer>

        <el-dialog width="345px" append-to-body :visible.sync="dialogVisible"
                   :close-on-click-modal="false">
            <slideVerify
                    ref="dialogopen"
                    :l="42"
                    :r="10"
                    :w="catcha.w"
                    :h="catcha.h"
                    :blocky="catcha.blocky"
                    :imgurl="catcha.imgurl"
                    :miniimgurl="catcha.miniimgurl"
                    :slider-text="catcha.text"
                    @success="onSuccess"
                    @fail="onFail"
                    @refresh="onRefresh"
            />
        </el-dialog>
    </div>
</template>
<script>
    import moment from 'moment';
    import map from '../map';
    import slideVerify from "./slideVerify";
    import udCryptUtil from "../utils/udCryptUtil";
    import udUuidUtil from "../utils/udUuidUtil";
    import setting from "../setting";
    import udStoreUtil from "../utils/udStoreUtil";
    import login from '../api/login'
    import udFrameWorkUtil from "../utils/udFrameWorkUtil";
    import udMsgUtil from "../utils/udMsgUtil";

    const TIME_COUNT = 120;
    export default {
        name: 'Login',
        components: {slideVerify},
        data() {
            return {
                dialogVisible: false,
                catcha: {
                    blocky: 0,
                    imgurl: '',
                    miniimgurl: '',
                    text: '向右滑动完成拼图',
                    h: 150,
                    w: 300,
                    sh: 60,
                    sw: 60,
                    modifyImg: '',
                },
                loginStyle: false,
                submitState: 0,
                kaptchaUrl: '',
                keyEvent: null,
                initForm: {
                    username: '',
                    password: '',
                    userIphone: '',
                    code: ''
                },
                state: null,
                form: {
                    userIphone: '',
                },
                textColor: false,//判断登录方式点击效果

                show1: true,
                count: '',
                timer: null,
            };
        },
        created() {
            const vm = this;
            udStoreUtil.resetStore();
            vm.reset();
            vm.state = udUuidUtil.getUUID();
            vm.kaptchaUrl =
                setting.serverPath +
                '/g/user/kaptcha?state=' +
                vm.state +
                '&time=' +
                moment().millisecond();
            vm.addWindowKeyEvent();
        },
        destroyed() {
            const vm = this;
            vm.removeWindowKeyEvent();
        },
        methods: {
            addWindowKeyEvent() {
                const vm = this;
                vm.keyEvent = ev => {
                    if (ev.key == 'Enter') {
                        vm.login();
                    }
                };
                vm.keyEvent = window.addEventListener('keydown', vm.keyEvent);
            },
            removeWindowKeyEvent() {
                const vm = this;
                window.removeEventListener('keydown', vm.keyEvent);
            },
            reset() {
                const vm = this;
                vm.form = JSON.parse(JSON.stringify(vm.initForm));
            },
            getMsmCodeL() {
                const vm = this
                if (vm.form.userIphone == '') {
                    vm.showMessage('电话号码不能为空','warning')
                } else {
                    vm.getMsgCode()
                    if (!vm.timer) {
                        vm.count = TIME_COUNT;
                        vm.show1 = false;
                        vm.timer = setInterval(() => {
                            if (vm.count > 0 && vm.count <= TIME_COUNT) {
                                vm.count--;
                            } else {
                                vm.show1 = true;
                                clearInterval(vm.timer);
                                vm.timer = null;
                            }
                        }, 1000)
                    }
                }
            },
            //图片验证码验证
            imageChangeClick: function () {
                const vm = this
                vm.textColor = false
                vm.loginStyle = false
            },
            //短信验证码验证
            smsChangeClick: function () {
                const vm = this
                vm.textColor = true
                vm.loginStyle = true


            },
            resetKaptcha() {
                const vm = this;
                vm.kaptchaUrl =
                    udUuidUtil.getStore('settings.serverAddress') +
                    '/g/user/kaptcha?state=' +
                    vm.state +
                    '&time=' +
                    moment().millisecond();
            },
            //图片验证
            login() {
                const vm = this;
                if (vm.form.username == '' || vm.form.password == '') {
                    vm.showMessage('用户名或密码不能为空', 'warning')
                } else {
                    vm.validateKaptcha();
                    vm.imageCode()
                }

            },
            //短信验证
            loginCode() {
                const vm = this;
                // vm.validateKaptcha();
                const obj = {};
                obj.username = vm.form.userIphone;
                obj.password = vm.form.code;
                obj.deviceType = 'BROWSER';
                obj.loginType = 'PHONE&SMS',
                    obj.state = vm.state;
                // e.offsetX + ',' + e.offsetY;
                // obj.xpos = left;
                const msg = JSON.stringify(obj);
                const public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhZRMfkhJqOSJ2J9Im9wis/l/OGlViSyujS9hy8fQ0CTr+v87xDpoQu8LWN91CIRZnCADI0UqOscYtmykDDzyLzK9XktmDnZRII8uPYJirmgSPr0LIO3sai+fUpV3d4rJ9NBXFl/juQX1uHeRsDaGKdQwut/crOUfyaXWlKdrF8UQyrO08gjTbJWTBI7dpp85NciJm0/uQOLg234m2mhNuyWZB/Ov+eqTxMQeqa+zXRYmJtfKHjI+FNoM2hJ9sgos2/nHfd9wfn5QoVDHxNZVtuCIvcCGlfluIWP0qkoJp56QCnOmJAI50FGnvk6lv8YKpLWvYYtVNB0kYsDFEQnHbwIDAQAB";
                const ec = udCryptUtil.rsaEncrypt(msg, public_key)
                vm.getLoginIn(ec)
            },
            validateKaptcha() {
                const vm = this;
                vm.dialogVisible = true;
                // vm.resetKaptcha();
            },
            imageCode() {
                let vm = this
                login.loginCode(vm.state).then(data => {
                    console.log(data);
                    const imgobj = data.result
                    vm.catcha.blocky = 'data:image/png;base64,' + imgobj.yHeight
                    vm.catcha.imgurl = 'data:image/png;base64,' + imgobj.backImage
                    vm.catcha.miniimgurl = "data:image/png;base64," + imgobj.slidingImage
                    vm.$nextTick(() => {
                        if (vm.$refs.dialogopen) {
                            vm.$refs.dialogopen.reset(imgobj.yHeight)
                        }
                    })
                }, err => {
                    vm.showMessage(err, 'warning')
                    console.log(err)
                })
            },
            //获取短信验证码
            getMsgCode: function () {
                const vm = this
                login.msgCode(vm.form.userIphone).then(data => {
                    console.log("短信验证码获取", data)

                }).catch(err => {
                    vm.showMessage(err.msg, 'warning')
                    vm.show1 = true;
                    clearInterval(vm.timer);
                    vm.timer = null;
                    console.log(err)
                })

            },
            onSuccess(left) {
                const vm = this;
                if (vm.submitState != 0) {
                    return;
                }
                vm.submitState = 0;
                const obj = {};
                obj.username = vm.form.username;
                obj.password = vm.form.password;
                obj.deviceType = 'BROWSER';
                obj.loginType = 'USERNAME&PASSWORD',
                    obj.state = vm.state;
                // e.offsetX + ',' + e.offsetY;
                obj.xpos = left;
                const msg = JSON.stringify(obj);
                const public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhZRMfkhJqOSJ2J9Im9wis/l/OGlViSyujS9hy8fQ0CTr+v87xDpoQu8LWN91CIRZnCADI0UqOscYtmykDDzyLzK9XktmDnZRII8uPYJirmgSPr0LIO3sai+fUpV3d4rJ9NBXFl/juQX1uHeRsDaGKdQwut/crOUfyaXWlKdrF8UQyrO08gjTbJWTBI7dpp85NciJm0/uQOLg234m2mhNuyWZB/Ov+eqTxMQeqa+zXRYmJtfKHjI+FNoM2hJ9sgos2/nHfd9wfn5QoVDHxNZVtuCIvcCGlfluIWP0qkoJp56QCnOmJAI50FGnvk6lv8YKpLWvYYtVNB0kYsDFEQnHbwIDAQAB";
                const ec = udCryptUtil.rsaEncrypt(msg, public_key)
                // 验证是否成功checkKaptchaImg是后台验证接口方法
                // console.log("222222222",ec)
                vm.getLoginIn(ec)
            },
            getLoginIn: function (ec) {
                const vm = this
                login.loginIn({ec: ec}).then(
                    data => {
                        if (data.result.code) {
                            vm.onRefresh()
                        } else {
                            vm.dialogVisible = false
                            if (data.code == 0) {
                                vm.submitState = 1;
                                // TODO 权限设置;
                                // vm.getMsgCode()
                                vm.initFrameWork(data);
                            } else {
                                vm.showMessage(data.msg, 'warning')
                            }
                        }
                    },
                    err => {
                        vm.dialogVisible = false
                        // vm.$message.error(err);
                        vm.showMessage(err, 'warning')
                        console.log("444444444", err.msg)
                        vm.submitState = 0;
                        // if (err.code == excType.DEV && err.msg == excMsg.KAPTCHA_ERROR) {
                        //     vm.resetKaptcha();
                        // } else if (err.code == excType.BIZ) {
                        //     vm.dialogVisible = false;
                        //     util.show.err(err);
                        // } else {
                        //     console.log(err.msg);
                        // }
                    },
                )
            },
            onFail() {
                console.log('fail')
            },
            // 刷新
            onRefresh() {
                this.imgurl = ''
                this.miniimgurl = ''
                this.imageCode()
            },
            showMessage(message, type) {
                this.$message({
                    message: message,
                    type: type
                });
            },
            /**
             * 转化存储当前登录用户数据
             * 1、menuList - 面包屑与左侧菜单
             * 2、roleList - 该用户所拥有的权限 - 菜单、操作权限
             * 3、token信息
             * 4、用户其他信息
             */
            initFrameWork(d) {
                const vm = this;
                udFrameWorkUtil.initFrameWork(d.result);
                if(!udFrameWorkUtil.checkPermit(map.sys_user_query,"all")){
                    udMsgUtil.err(vm,"权限不足");
                }else{
                    vm.$router.push({name: map.base});
                }
            },
        },
    };
</script>
<style  lang="scss">
    #loginPage {
        display: flex;
        min-width: 1200px;
        min-height: 100vh;
        flex-direction: column;
        .baseheader {
            height: 86px;
            background-color: #fff;
            display: flex;
            align-items: center;
            padding: 0 49px;
        }

        .baseheader > .left {
            display: flex;
            align-items: center;

            & > .logo {
                width: 55px;
                height: 46px;
                object-fit: contain;
                margin-right: 20px;
            }

            & > p {
                font-size: 28px;
                font-weight: 800;
                color: #1377ec;
                line-height: 36px;
            }
        }

        .content {
            background: url('../assets/images/login/ic_bj.png') no-repeat center center;
            background-image: image-set(
                            '../assets/images/login/ic_bj.png' 1x,
                            '../assets/images/login/ic_bj@2x.png' 2x
            );
            background-repeat: no-repeat;
            background-position: center center;
            background-size: cover;
            flex: 1;
            display: flex;
            justify-content: center;
            align-items: flex-end;
            scrollbar-width:thin;
        }

        .form {
            width: 550px;
            height: 400px;
            margin-bottom: -200px;
            background: white;
            box-shadow: 0px 6px 10px 0px rgba(239, 242, 251, 0.86);
            border-radius: 4px;

            & > label {
                display: block;
                height: 60px;
                border-radius: 4px;
                background: rgba(233, 237, 242, 1);
                line-height: 60px;
                padding-left: 50px;
                font-size: 20px;
                color: #3e89f9;
                margin-bottom: 50px;
            }
        }

        .form > .ipt {
            display: flex;
            flex-direction: row;
            width: 360px;
            margin: 0 auto 36px;
            position: relative;
        }

        .form > .ipt > input {
            width: 100%;
            height: 40px;
            border-radius: 4px;
            padding-left: 56px;
            line-height: 60px;
            font-size: 14px;
            outline: none;
        }

        .form > .ipt > i {
            display: block;
            width: 20px;
            height: 24px;
            background-size: contain;
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
            margin: auto 15px;
        }

        .form > .username > i {
            background: url('../assets/images/login/ic_accunt.png') no-repeat 0 0;
        }

        .form > .password > i {
            background: url('../assets/images/login/ic_key.png') no-repeat 0 0;
        }

        .form > .ipt:last-of-type {
            margin-bottom: 75px;
        }

        .form > .btn {
            display: block;
            width: 450px;
            height: 60px;
            border-radius: 4px;
            line-height: 60px;
            text-align: center;
            color: #fff;
            font-size: 22px;
            margin: 0 auto 20px;
            cursor: pointer;
            background-color: #3e89f9;

            &:hover {
                background-color: #67a4ff;
            }

            &:active {
                background-color: #2774e7;
            }

            &:disabled {
                background-color: #67a4ff;
            }
        }

        .basefooter {
            height: 440px;
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            align-items: center;

            & > .browsers {
                display: flex;
                align-items: center;
            }
        }

        .browser {
            display: flex;
            align-items: center;
            font-size: 18px;
            color: #999;
            margin-bottom: 33px;

            &:first-child {
                margin-right: 48px;
            }

            & > img {
                width: 34px;
                height: 34px;
                object-fit: contain;
                margin-right: 16px;
            }
        }

        .copyright {
            height: 90px;
            font-size: 14px;
            color: #666;
        }

        input {
            border: 1px solid #ddd;
            outline: none;
            color: #222;
        }

        input::placeholder {
            color: #999;
        }

        input:focus {
            border: 1px solid #3e89f9;
        }
    }
</style>


