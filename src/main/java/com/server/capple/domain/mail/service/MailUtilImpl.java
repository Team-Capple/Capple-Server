package com.server.capple.domain.mail.service;

import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MailErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailUtilImpl implements MailUtil {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    @Value("${mail.white-list-cert-code}")
    private String whiteListCertCode;

    @Async
    @Override
    public CompletableFuture<String> sendMailAddressCertificationMail(String receiver, Boolean isWhiteList) {
        String certCode = generateCertCode();
        if (isWhiteList)  {
            certCode = whiteListCertCode;
            return CompletableFuture.completedFuture(certCode);
        }
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(receiver);
            mimeMessageHelper.setSubject("[Qapple] 회원가입 인증코드 안내");
            mimeMessageHelper.setText(setCertMailContext(certCode), true);
            javaMailSender.send(mimeMessage);
            return CompletableFuture.completedFuture(certCode);
        } catch (MessagingException e) {
            log.error(MailErrorCode.MULTI_PART_CRAETION_FAILED.getMessage());
            throw new RestApiException(MailErrorCode.MULTI_PART_CRAETION_FAILED);
        }
    }

    private String generateCertCode() {
        final String candidateChars = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Integer certCodeLength = 5;
        String certCode = "";
        for (int i = 0; i < certCodeLength; i++) {
            Long idx = (long) (Math.random() * candidateChars.length());
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
