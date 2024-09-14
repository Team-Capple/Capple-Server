package com.server.capple.domain.boardSubscribeMember.reposiotry;

import com.server.capple.domain.boardSubscribeMember.entity.BoardSubscribeMember;
import com.server.capple.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardSubscribeMemberRepository extends JpaRepository<BoardSubscribeMember, Long> {
    Boolean existsByMemberIdAndBoardId(Long memberId, Long boardId);
    List<BoardSubscribeMember> findBoardSubscribeMembersByBoardId(Long boardId);
    void deleteBoardSubscribeMemberByBoardId(Long boardId);
}
