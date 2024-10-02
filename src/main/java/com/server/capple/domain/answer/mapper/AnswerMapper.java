package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public Answer toAnswerEntity(AnswerRequest request, Member member, Question question) {
        return Answer.builder()
                .member(member)
                .question(question)
                .content(request.getAnswer())
                .build();
    }

    public AnswerInfo toAnswerInfo(AnswerInfoInterface answerInfoDto, Long memberId, Boolean isLiked) {
        return AnswerInfo.builder()
                .answerId(answerInfoDto.getAnswer().getId())
                .writerId(answerInfoDto.getWriterId())
                .profileImage(answerInfoDto.getWriterProfileImage())
                .nickname(answerInfoDto.getWriterNickname())
                .content(answerInfoDto.getAnswer().getContent())
                .isMine(answerInfoDto.getWriterId().equals(memberId))
                .isReported(answerInfoDto.getIsReported())
                .isLiked(isLiked)
                .writeAt(answerInfoDto.getAnswer().getCreatedAt().toString())
                .build();
    }

    public MemberAnswerInfo toMemberAnswerInfo(Answer answer, int heartCount, Boolean isLiked) {
        return MemberAnswerInfo.builder()
                .questionId(answer.getQuestion().getId())
                .answerId(answer.getId())
                .writerId(answer.getMember().getId())
                .nickname(answer.getMember().getNickname())
                .profileImage(answer.getMember().getProfileImage())
                .content(answer.getContent())
                .heartCount(heartCount)
                .writeAt(answer.getCreatedAt().toString())
                .isLiked(isLiked)
                .build();
    }
}
