import axios from 'axios';
import { defineStore } from 'pinia';
import { computed, ref } from 'vue';

const initState = {
  token: '',
  user: {
    username: '',
    email: '',
    roles: [],
  },
  avatarTimestamp: Date.now(), // (1) 아바타 이미지 경로에 추가할 쿼리스트링 값 (타임스탬프)
};

export const useAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });

  // 로그인 여부
  const isLogin = computed(() => !!state.vaslue.user.username);

  // 로그인 사용자 ID
  const username = computed(() => state.value.user.username);

  // 로그인 사용자 email
  const email = computed(() => state.value.user.email);

  // (2) 로그인 여부에 따라 아바타 이미지 다운로드 주소 변경
  const avatarUrl = computed(() =>
    state.value.user.username
      ? `/api/member/${state.value.user.username}/avatar?t=${state.value.avatarTimestamp}`
      : null
  );

  /* 액션 메서드 작성 영역 */

  // (3) 아바타 업데이트 액션 추가
  const updateAvatar = () => {
    state.value.avatarTimestamp = Date.now();
    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  // 로그인 액션
  const login = async (member) => {
    //state.value.token = 'test token';
    // state.value.user = {
    //   username: member.username,
    //   email: member.username + '@test.com',
    // };

    // api 호출
    const { data } = await axios.post('/api/auth/login', member);
    state.value = { ...data };

    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  const logout = () => {
    localStorage.clear();

    state.value = { ...initState };
  };

  const getToken = () => state.value.token;

  const load = () => {
    const auth = localStorage.getItem('auth');

    if (auth != null) {
      state.value = JSON.parse(auth);
      console.log(state.value);
    }
  };

  const changeProfile = (member) => {
    state.value.user.email = member.email;
    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  // 스토어 초기화 시 자동 실행
  load();

  return {
    state,
    username,
    email,
    isLogin,
    changeProfile,
    login,
    logout,
    getToken,
    // (4) avatar 관련 구문 return 추가
    avatarUrl,
    updateAvatar,
  };
});
