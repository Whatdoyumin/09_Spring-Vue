//  애플리케이션 전역 설정
export default {
  title: '우리는 모두 친구', // 메인 타이틀
  subtitle: 'KB Fullstack 학습(Vue+Spring)', // 서브 타이틀

  // 메인 메뉴 구성 정보
  menus: [
    {
      title: '게시판',
      url: '/board/list',
      icon: 'fa-solid fa-paste',
    },
    {
      title: '여행',
      url: '/travel/list',
      icon: 'fa-solid fa-plane-departure',
    },
    {
      title: '갤러리',
      url: '/gallery/list',
      icon: 'fa-regular fa-images',
    },
  ],

  // 인증 관련 메뉴 정보
  accountMenus: {
    login: {
      url: '/auth/login',
      title: '로그인',
      icon: 'fa-solid fa-right-to-bracket',
    },
    join: {
      url: '/auth/join',
      title: '회원가입',
      icon: 'fa-solid fa-user-plus',
    },
  },
};

/* 전역 설정 파일 활용 이유유
  - 중앙집중식 관리: 메뉴 구성 및 설정을 한 곳에서 관리
  - 유지보수성: 메뉴 변경 시 설정 파일만 수정
  - 재사용성: 여러 컴포넌트에서 동일한 설정 활용
  */
