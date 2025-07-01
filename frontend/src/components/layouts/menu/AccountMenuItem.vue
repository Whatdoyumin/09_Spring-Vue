<!-- 계정 메뉴 그룹 컴포넌트 -->
<script setup>
import { useAuthStore } from '@/stores/auth'; // 인증 스토어 가져오기
import { computed } from 'vue';

const props = defineProps({ username: String });
const auth = useAuthStore();

//const avatar = `/api/member/${props.username}/avatar`;
const avatar = computed(() => auth.avatarUrl);
</script>

<template>
  <li class="nav-item">
    <router-link class="nav-link" to="/auth/profile">
      <img :src="avatar" class="avatar avatar-sm" />
      {{ username }}
    </router-link>
  </li>
</template>
