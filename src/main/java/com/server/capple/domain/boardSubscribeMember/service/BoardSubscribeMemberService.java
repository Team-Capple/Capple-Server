package com.server.capple.domain.boardSubscribeMember.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.member.entity.Member;

import java.util.List;

public interface BoardSubscribeMemberService {
    void createBoardSubscribeMember(Member member, Board board);
    List<Member> findBoardSubscribeMembers(Long boardId);
    void deleteBoardSubscribeMemberByBoardId(Long boardId);
}
