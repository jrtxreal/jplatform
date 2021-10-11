export default {
    ok(vm,msg) {
        vm.$message({
            message:msg,
            type: 'success'
        });
    },
    err(vm,msg) {
        vm.$message({
            message:msg,
            type: 'error'
        });
    },
}
