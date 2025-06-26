<!-- 네비게이션 바 레이아웃 컴포넌트 -->
<script setup>
import { reactive, computed } from 'vue';
import config from '@/config';
import MenuGroup from './menu/MenuGroup.vue';
import AccountMenuGroup from './menu/AccountMenuGroup.vue';

let state = reactive({ isNavShow: false });

// 반응형 네비게이션 클래스 계산
let navClass = computed(() =>
  state.isNavShow ? 'collapse navbar-collapse show' : 'collapse navbar-collapse'
);

const toggleNavShow = () => (state.isNavShow = !state.isNavShow);
</script>

<template>
  <nav class="navbar navbar-expand-sm bg-primary navbar-dark">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">
        <i class="fa-solid fa-house"></i>
        Scoula
      </router-link>

      <!-- 모바일 토글 버튼 -->
      <button class="navbar-toggler" type="button" @click="toggleNavShow">
        <span class="navbar-toggler-icon"></span>
      </button>

      <!-- 네비게이션 메뉴 -->
      <div :class="navClass" id="collapsibleNavbar">
        <!-- <MenuGroup :menus="config.menus" /> -->
        <MenuGroup v-bind:menus="config.menus" />
        <AccountMenuGroup />
      </div>
    </div>
  </nav>
</template>

<!-- 
  import { reactive, computed } from 'vue';

  - reactive() : 반응형 상태 관리(주로 객체에 사용)
  - ref() : 반응형 상태 관리(주로 단일 값에 사용)

    * 반응형 상태 관리 이유
    - 상태 변경 시 자동으로 업데이트 처리
    - 컴포넌트 간 상태 공유 용이

  - computed() : 반응형 데이터를 기반으로 계산된 값을 제공하는 기능
    * 핵심 개념
      의존하는 데이터가 변경될 때만 다시 계산
      결과값을 캐싱하여 성능 최적화
      읽기 전용 속성으로 사용


  -<router-link> : 라우터 링크 컴포넌트, <
    RouterView> 에 표시될 컴포넌트를 지정

    * 주요 속성
      to : 이동할 경로
      tag : 태그 이름
      exact : 정확한 경로 일치 여부
      active-class : 활성 상태 클래스
      exact-active-class : 정확한 경로 일치 클래스


    - v-bind : 속성 바인딩
      - 속성 이름을 동적으로 설정
      - 예: v-bind:menus="config.menus"
      - 속성 이름을 동적으로 설정

    - v-model : 양방향 바인딩
      - 입력 필드와 상태를 연결
      - 예: v-model="searchQuery"
      - 속성 이름을 동적으로 설정

-->
