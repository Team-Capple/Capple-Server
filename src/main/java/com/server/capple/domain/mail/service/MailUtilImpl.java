package com.server.capple.domain.mail.service;

import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MailErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class MailUtilImpl implements MailUtil {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${mail.white-list-cert-code}")
    private String whiteListCertCode;

    @Override
    public String sendMailAddressCertificationMail(String receiver, Boolean isWhiteList) {
        String certCode = generateCertCode();
        if(isWhiteList) certCode = whiteListCertCode;
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setSubject("[Capple] 회원가입 인증코드 안내");
            mimeMessageHelper.setText(setCertMailContext(certCode), true);
            javaMailSender.send(mimeMessage);
            return certCode;
        } catch (MessagingException e) {
            throw new RestApiException(MailErrorCode.MULTI_PART_CRAETION_FAILED);
        }
    }

    private String generateCertCode() {
        final String candidateChars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Integer certCodeLength = 5;
        String certCode = "";
        for (int i = 0; i < certCodeLength; i++) {
            Long idx = Math.round(Math.random() * candidateChars.length());
            certCode += candidateChars.charAt(idx.intValue());
        }
        return certCode;
    }

    private String setCertMailContext(String certCode) {
        Context context = new Context();
        context.setVariable("certCode", certCode);
        return templateEngine.process("certCodeMail", context);
    }
}
