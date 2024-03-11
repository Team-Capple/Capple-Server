package com.server.capple.config.security.auth;

import com.server.capple.domain.member.entity.Member;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final Member member;
    public CustomUserDetails(Member member) {
        this.member = member;
    }
    @Bean
    public final Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //TODO Authority 관련 설정 추가 필요
        return null;
    }

    @Override
    public String getPassword() {
        return member.getSub();
    }

    @Override
    public String getUsername() {
        return member.getSub();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
