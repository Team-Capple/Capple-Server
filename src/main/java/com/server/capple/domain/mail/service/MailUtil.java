package com.server.capple.domain.mail.service;

public interface MailUtil {
    public static Boolean emailAddressFormVerification(String emailAddress) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return emailAddress.matches(emailRegex);
    }

    String snedMailAddressCerticationMail(String receiver);
}
