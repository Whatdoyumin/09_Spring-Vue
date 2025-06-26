<!-- 메뉴 그룹 컴포넌트 -->
<script setup>
import MenuItem from './MenuItem.vue';

const props = defineProps({
  menus: { Type: Array, required: true },
});
</script>

<template>
  <ul class="navbar-nav">
    <MenuItem v-for="menu in menus" :menu="menu" :key="menu.url" />
  </ul>
</template>

<!-- 
  <MenuItem v-for="menu in menus" :menu="menu" :key="menu.url" />
  - v-for : 반복문, :key 속성 작성 필수!
  - :menu="menu" : 자식 컴포넌트에 전달된 속성 이름(props)
  - :key="menu.url" : 자식 컴포넌트에 전달된 속성 이름
-->
