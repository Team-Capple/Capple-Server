package com.server.capple.config.security;

import com.server.capple.config.security.jwt.filter.JwtFilter;
import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtService jwtService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    csrf disable
        http
            .csrf((auth) -> auth.disable());
//    Form login disable
        http
            .formLogin((auth) -> auth.disable());
//    http basic disable
        http
            .httpBasic((auth) -> auth.disable());
//    경로별 인가 //TODO Role 분리 필요
        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/api-docs/**").permitAll()
                .requestMatchers("/members/sign-in","/members/sign-up", "/members/local-sign-in", "/token/**", "/members/check-nickname", "/members/generate-email-jwt").permitAll()
                .requestMatchers("/admin/**").hasRole(Role.ROLE_ADMIN.getName())
                .requestMatchers("/answers","/answers/**").authenticated()
                .requestMatchers("/members","/members/**").authenticated()
                .requestMatchers("/tags","/tags/**").authenticated()
                .requestMatchers("/questions","/questions/**").authenticated()
                .anyRequest().denyAll());
        http
            .addFilterBefore(new JwtFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
//    세션 설정
        http
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
