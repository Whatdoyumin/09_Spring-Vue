<!-- 기본 메뉴 아이템 컴포넌트 -->

<script setup>
// defineProps() : 부모 -> 자식 컴포넌트로 전달된 속성을 정의
// 속성 이름을 동적으로 설정
// 예: defineProps({ menu: { Type: Object, required: true } })
const props = defineProps({
  menu: { Type: Object, required: true },
});
</script>

<template>
  <li class="nav-item">
    <router-link class="nav-link" :to="menu.url">
      <i :class="menu.icon"></i>
      {{ menu.title }}
    </router-link>
  </li>
</template>
