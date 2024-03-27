package com.server.capple.domain.mail.service;

public interface MailService {
    Boolean snedMailAddressCerticationMail(String receiver);
    Boolean checkMailDomain(String emailAddress);
}
