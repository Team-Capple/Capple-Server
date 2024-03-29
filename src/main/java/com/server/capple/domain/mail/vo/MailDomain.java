package com.server.capple.domain.mail.vo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MailDomain {
    POSTECH("postech.ac.kr"),
    ;

    private final String domain;

    public static Boolean isExistDomain(String domain) {
        for (MailDomain mailDomain : MailDomain.values()) {
            if (mailDomain.domain.equals(domain)) {
                return true;
            }
        }
        return false;
    }
}