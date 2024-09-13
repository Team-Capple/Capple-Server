package com.server.capple.domain.boardSubscribeMember.service;

import com.server.capple.domain.board.entity.Board;
import com.server.capple.domain.boardSubscribeMember.entity.BoardSubscribeMember;
import com.server.capple.domain.boardSubscribeMember.reposiotry.BoardSubscribeMemberRepository;
import com.server.capple.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardSubscribeMemberServiceImpl implements BoardSubscribeMemberService {
    private final BoardSubscribeMemberRepository boardSubscribeMemberRepository;
    @Override
    public void createBoardSubscribeMember(Member member, Board board) {
        if(boardSubscribeMemberRepository.existsByMemberIdAndBoardId(member.getId(), board.getId())) {
            return;
        }
        boardSubscribeMemberRepository.save(BoardSubscribeMember.builder().member(member).board(board).build());
    }

    @Override
    public List<Member> findBoardSubscribeMembers(Long boardId) {
        return boardSubscribeMemberRepository.findBoardSubscribeMembersByBoardId(boardId).stream().map(BoardSubscribeMember::getMember).toList();
    }

    @Override
    public void deleteBoardSubscribeMemberByBoardId(Long boardId) {
        boardSubscribeMemberRepository.deleteBoardSubscribeMemberByBoardId(boardId);
    }
}
