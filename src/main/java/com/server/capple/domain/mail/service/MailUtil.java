package com.server.capple.domain.mail.service;

import java.util.concurrent.CompletableFuture;

public interface MailUtil {
    public static Boolean emailAddressFormVerification(String emailAddress) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return emailAddress.matches(emailRegex);
    }

    CompletableFuture<String> sendMailAddressCertificationMail(String receiver, Boolean isWhiteList);
}
