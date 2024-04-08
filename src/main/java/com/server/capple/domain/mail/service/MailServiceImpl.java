package com.server.capple.domain.mail.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.mail.repository.MailRedisRepository;
import com.server.capple.domain.mail.repository.MailWhitelistRedisRepository;
import com.server.capple.domain.mail.vo.MailDomain;
import com.server.capple.global.exception.RestApiException;
import com.server.capple.global.exception.errorCode.MailErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MailUtil mailUtil;
    private final MailRedisRepository mailRedisRepository;
    private final MailWhitelistRedisRepository mailWhitelistRedisRepository;
    private final JwtService jwtService;

    @Override
    public Boolean snedMailAddressCerticationMail(String email) {
        String certCode = mailUtil.snedMailAddressCerticationMail(email);
        String emailJwt = jwtService.createJwtFromEmail(email);
        return mailRedisRepository.save(emailJwt, certCode);
    }

    @Override
    public Boolean saveMailWhitelist(String email, Long whitelistDurationMinutes) {
        return mailWhitelistRedisRepository.save(email, whitelistDurationMinutes);
    }

    @Override
    public Boolean checkMailDomain(String emailAddress) {
        if (mailWhitelistRedisRepository.existsByEmail(emailAddress)) {
            return true;
        }
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
