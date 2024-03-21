package com.server.capple.domain.member.entity;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_VISITOR("VISITOR")
    ,ROLE_ADMIN("ADMIN")
    ,ROLE_ACADEMIER("ACADEMIER")
    ,ROLE_DEVELOPER("DEVELOPER")
    ;
    private final String name;
    Role(String name) {
        this.name = name;
    }
}
