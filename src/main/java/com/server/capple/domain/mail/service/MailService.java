package com.server.capple.domain.mail.service;

public interface MailService {
    Boolean sendMailAddressCertificationMail(String receiver, Boolean isWhiteList);
    Boolean saveMailWhitelist(String email, Long whitelistDurationMinutes);
    Boolean checkWhiteList(String emailAddress);
    Boolean checkMailDomain(String emailAddress);
    Boolean checkEmailCertificationCode(String email, String certificationCode);
}
