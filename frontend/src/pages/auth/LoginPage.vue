<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const isLoggedIn = ref(false);

// 로그인 입력 상태 반응형 데이터 (객체)
const loginForm = ref({
  username: '',
  password: '',
});

const userInfo = ref({});

// 로그인 함수
const handleLogin = async () => {
  try {
    const response = await axios.post('/api/auth/login', loginForm.value);
    // console.log('response.data : ', response.data);

    // 구조 분해 할당
    const { token, user } = response.data;
    console.log(user);

    // 로컬 스토리지에 토큰 저장
    localStorage.setItem('authToken', token);

    // 로컬 스토리지에 사용자 정보 저장
    localStorage.setItem('userInfo', JSON.stringify(user));

    // 로그인 상태 업데이트
    isLoggedIn.value = true;
    userInfo.value = user;

    // 로그인 폼에 입력된 값 초기화 (v-model)
    loginForm.value = {
      username: '',
      password: '',
    };
  } catch (e) {
    console.error(e);
  }
};

const handleLogout = () => {
  // 로컬스토리지 저장된 내용 비우기
  //localStorage.clear();

  localStorage.removeItem('authToken');
  localStorage.removeItem('userInfo');

  // 로그인 상태 false로 변경
  isLoggedIn.value = false;
};

// // 로그인 상태 확인 함수
const checkLoginStatus = () => {
  const token = localStorage.getItem('authToken');
  const savedUserInfo = localStorage.getItem('userInfo');

  if (token && savedUserInfo) {
    isLoggedIn.value = true;
    userInfo.value = JSON.parse(savedUserInfo);
  } else {
    isLoggedIn.value = false;
    userInfo.value = {};
  }
};

// // 컴포넌트 마운트 시 로그인 상태 확인
onMounted(() => {
  checkLoginStatus();
});
</script>

<template>
  <div class="container py-5">
    <div class="row justify-content-center">
      <div class="col-md-6 col-lg-5">
        <div class="card shadow-sm border-0">
          <div class="card-body p-4">
            <!-- 미로그인 시 화면 -->
            <div v-if="!isLoggedIn">
              <h5 class="text-center text-secondary mb-3">로그인</h5>
              <form @submit.prevent="handleLogin">
                <div class="mb-3">
                  <label for="username" class="form-label">아이디</label>
                  <input
                    type="text"
                    id="username"
                    class="form-control"
                    v-model="loginForm.username"
                    placeholder="아이디를 입력하세요"
                  />
                </div>
                <div class="mb-4">
                  <label for="password" class="form-label">비밀번호</label>
                  <input
                    type="password"
                    id="password"
                    class="form-control"
                    v-model="loginForm.password"
                    placeholder="비밀번호를 입력하세요"
                  />
                </div>
                <div class="d-grid">
                  <button type="submit" class="btn btn-primary">로그인</button>
                </div>
              </form>
            </div>

            <!-- 로그인 시 화면 -->
            <div v-else>
              <h5 class="text-center text-success mb-3">
                반가워요, <strong>{{ userInfo.username }}</strong
                >님!
              </h5>
              <ul class="list-group mb-4">
                <li class="list-group-item">
                  <strong>이메일:</strong> {{ userInfo.email }}
                </li>
                <li class="list-group-item">
                  <strong>권한:</strong> {{ userInfo.roles }}
                </li>
              </ul>
              <form @submit.prevent="handleLogout">
                <div class="d-grid">
                  <button type="submit" class="btn btn-outline-danger">
                    로그아웃
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
