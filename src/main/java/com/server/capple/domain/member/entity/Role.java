package com.server.capple.domain.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_VISITOR("ROLE_VISITOR")
    ,ROLE_ADMIN("ROLE_ADMIN")
    ,ROLE_ACADEMIER("ROLE_ACADEMIER")
    ,ROLE_DEVELOPER("ROLE_DEVELOPER")
    ;
    private final String name;
    Role(String name) {
        this.name = name;
    }
}
