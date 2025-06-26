<!-- 로그아웃 메뉴 아이템 컴포넌트 -->
<script setup>
// useRouter() : 라우터 인스턴스를 반환하는 함수
// 라우터 인스턴스를 사용하여 페이지 이동 및 라우팅 처리
import { useRouter } from 'vue-router';

const router = useRouter();

const logout = (e) => {
  // 실제 로그아웃 로직 구현 예정
  router.push('/'); // 로그아웃 시 홈 페이지로 이동
};
</script>

<template>
  <a href="#" class="nav-link" @click.prevent="logout">
    <i class="fa-solid fa-right-from-bracket"></i>
    로그아웃
  </a>
</template>
