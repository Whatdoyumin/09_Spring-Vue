package org.scoula.security.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.scoula.security.util.JwtProcessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  // Authorization 헤더명
  public static final String AUTHORIZATION_HEADER = "Authorization";

  // Bearer 토큰 접두사 (끝에 공백 포함)
  public static final String BEARER_PREFIX = "Bearer";    // 끝에 공백 있음

  // JWT 처리를 위한 유틸리티
  private final JwtProcessor jwtProcessor;

  // 사용자 인증에 필요한 정보를 DB에서 조회해서 담는 커스텀 UserDetailService
  private final UserDetailsService userDetailsService;

  private Authentication getAuthentication(String token) {
    String username = jwtProcessor.getUsername(token);

    UserDetails principal = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
  }

  /* [필터 메인 로직]

      토큰 추출 과정
        1. Authorization 헤더에서 값 조회
        2. "Bearer" 접두사 확인
        3. 접두사 제거 후 순수 토큰 추출
        4. 토큰 유효성 검사 및 사용자 정보 설정

        Bearer(보유자, 소지자) :
            - "이 토큰을 가진 사람에게 권한을 부여한다"는 의미
            - 토큰만 있으면 인증 완료!(토큰을 가지고 있는 것 자체가 인증의 증거)
            - 신분증과 같은 개념: 토큰 = 출입증, Bearer = 출입증 소지자
     */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    // Authorization 헤더에서 Bearer Token 추출
    String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

    if (bearerToken != null && bearerToken.startsWith(
        BEARER_PREFIX)) { // null이면 로그인 안한 사용자 -> Anonymous
      // Bearer 접두사 제거하여 순수 토큰 추출
      String token = bearerToken.substring(BEARER_PREFIX.length());

      // 토큰에서 사용자 정보 추출 및 Authentication 객체 구성 후 SecurityContext에 저장
      Authentication authentication = getAuthentication(token);

      // SecurityContext에 인증 정보 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);   // 필터의 목적
    }
    // 다음 필터로 요청 전달
    super.doFilter(request, response, filterChain);
  }
}
