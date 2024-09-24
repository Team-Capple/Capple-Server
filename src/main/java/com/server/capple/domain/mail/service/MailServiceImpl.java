package com.server.capple.domain.mail.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.mail.repository.MailRedisRepository;
import com.server.capple.domain.mail.repository.MailWhitelistRedisRepository;
import com.server.capple.domain.mail.vo.MailDomain;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MailErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MailUtil mailUtil;
    private final MailRedisRepository mailRedisRepository;
    private final MailWhitelistRedisRepository mailWhitelistRedisRepository;
    private final JwtService jwtService;

    @Override
    public Boolean sendMailAddressCertificationMail(String email, Boolean isWhiteList) {
        mailUtil.sendMailAddressCertificationMail(email, isWhiteList).thenAccept(certCode -> {
            String emailJwt = jwtService.createJwtFromEmail(email);
            mailRedisRepository.save(emailJwt, certCode);
            log.info("메일 발송 완료 : {}", email);
        });
        return true;
    }

    @Override
    public Boolean saveMailWhitelist(String email, Long whitelistDurationMinutes) {
        String emailJwt = jwtService.createJwtFromEmail(email);
        return mailWhitelistRedisRepository.save(emailJwt, whitelistDurationMinutes);
    }

    @Override
    public Boolean checkWhiteList(String emailAddress) {
        String emailJwt = jwtService.createJwtFromEmail(emailAddress);
        return mailWhitelistRedisRepository.existsByEmail(emailJwt);
    }

    @Override
    public Boolean checkMailDomain(String emailAddress) {
        String domainAddress = emailAddress.split("@")[1];
        return MailDomain.isExistDomain(domainAddress);
    }

    @Override
    public Boolean checkEmailCertificationCode(String emailJwt, String certificationCode) {
        String savedCertificationCode = mailRedisRepository.findByEmail(emailJwt);
        if (savedCertificationCode == null) {
            throw new RestApiException(MailErrorCode.CERTIFICATION_CODE_EXPIRED);
        }
        if (!certificationCode.equals(savedCertificationCode)) {
            throw new RestApiException(MailErrorCode.CERTIFICATION_CODE_NOT_MATCH);
        }
        mailRedisRepository.deleteByEmail(emailJwt);
        return true;
    }
}
