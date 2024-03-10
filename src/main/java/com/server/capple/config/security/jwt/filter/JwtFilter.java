package com.server.capple.config.security.jwt.filter;

import com.server.capple.config.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        토큰 추출
        String authorization = request.getHeader("Authorization");
//        토큰이 유형 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
//        토큰 분리
        String token = authorization.split(" ")[1];
//        토큰 만료 여부 확인
        if (jwtService.isExpired(token)) {
            filterChain.doFilter(request, response);
            return;
        }
//        토큰에서 tokenType 추출
        String tokenType = jwtService.getTokenType(token);
        if(tokenType == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if(!tokenType.equals("signUp")) {//TODO Authentication 관련 설정 추가 필요
            Authentication authentication = jwtService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
