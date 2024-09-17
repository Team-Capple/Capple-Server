package com.server.capple.dummy;


import com.server.capple.domain.board.repository.BoardHeartRedisRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DummyService {
    private final BoardHeartRedisRepository boardHeartRedisRepository;

    @PersistenceContext
    private EntityManager em;

    //멤버, 게시글, 게시글 좋아요 생성
    @Transactional
    public Object generateDummy(int memberCount, int boardCount) {
        generateDummyData(memberCount,boardCount);
        em.flush();
        boardHeartRedisRepository.generateDummyBoardLikes(memberCount, boardCount);
        return null;
    }

    @Transactional
    public void generateDummyData(int memberCount, int boardCount) {
        em.createNativeQuery("call generate_dummy_data(:memberCount, :boardCount)")
                .setParameter("memberCount", memberCount)
                .setParameter("boardCount", boardCount)
                .executeUpdate();
    }



}
