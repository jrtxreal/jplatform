<template>
<div class="left-wrapper">
    <div class="top">
        <logo-cmpt></logo-cmpt>
    </div>
    <div class="nav" style="scrollbar-width:thin;scrollbar-color:#2a333c #2a333c;">
        <nav-cmpt></nav-cmpt>
    </div>
</div>
</template>

<script>
    import LogoCmpt from "./LogoCmpt";
    import NavCmpt from "./NavCmpt";
    export default {
        name: "leftWrapper",
        components: {NavCmpt, LogoCmpt}
    }
</script>

<style lang="scss">
@import "../../../../styles/adminPortal/la";
</style>
