package com.server.capple.dummy;


import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import com.server.capple.domain.board.repository.BoardRepository;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.server.capple.domain.member.entity.Role.ROLE_ACADEMIER;

@Service
@RequiredArgsConstructor
public class DummyService {
    private final BoardRepository boardRepository;
    private final BoardHeartRedisRepository boardHeartRedisRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Object generateDummyBoards(int num) {
        boardRepository.generateDummyBoards(num);
        em.flush();
        boardHeartRedisRepository.generateDummyBoardLikes(num);
        return null;
    }

    @Transactional
    public Object generateDummyMembers() {
        List<Member> members = new ArrayList<>();

        for (long i = 1; i <= 10; i++) {
            Member member = Member.builder()
                    .id(i)
                    .nickname("User" + i)
                    .email("user" + i + "@example.com")
                    .sub("sub" + i)
                    .role(ROLE_ACADEMIER)
                    .build();

            members.add(member);
        }

        memberRepository.saveAll(members);
        return null;
    }
}
