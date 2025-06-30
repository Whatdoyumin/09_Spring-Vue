<!-- 로그아웃 메뉴 아이템 컴포넌트 -->
<script setup>
// useRouter() : 라우터 인스턴스를 반환하는 함수
// 라우터 인스턴스를 사용하여 페이지 이동 및 라우팅 처리
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const store = useAuthStore();
const router = useRouter();

const logout = (e) => {
  // 로그아웃
  store.logout();
  router.push('/'); // 로그아웃 시 홈 페이지로 이동
};
</script>

<template>
  <a href="#" class="nav-link" @click.prevent="logout">
    <i class="fa-solid fa-right-from-bracket"></i>
    로그아웃
  </a>
</template>
