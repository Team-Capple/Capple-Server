package com.server.capple.domain.mail.service;

import com.server.capple.config.security.jwt.service.JwtService;
import com.server.capple.domain.mail.repository.MailRedisRepository;
import com.server.capple.domain.mail.vo.MailDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MailUtil mailUtil;
    private final MailRedisRepository mailRedisRepository;
    private final JwtService jwtService;

    @Override
    public Boolean snedMailAddressCerticationMail(String email) {
        String certCode = mailUtil.snedMailAddressCerticationMail(email);
        String emailJwt = jwtService.createJwtFromEmail(email);
        return mailRedisRepository.save(emailJwt, certCode);
    }

    @Override
    public Boolean checkMailDomain(String emailAddress) {
        String domainAddress = emailAddress.split("@")[1];
        return MailDomain.isExistDomain(domainAddress);
    }
}
