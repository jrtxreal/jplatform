export default {
    SUC: 0,  // 正常
    AUC: 1,  // 认证失败或token已失效
    AUZ: 2,  // 权限不足
    BIZ: 3,  // 业务逻辑错误，显示给用户看
    DEV: 4,  // 测试异常，显示给开发人员
    SYS: 5,  // 未知异常
    no_data: -1,  // 没有获取到任何数据
    no_code: -2   // 没有获取到状态
}
