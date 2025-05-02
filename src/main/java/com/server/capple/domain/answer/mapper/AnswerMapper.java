package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.dao.AnswerRDBDao.AnswerInfoInterface;
import com.server.capple.domain.answer.dao.AnswerRDBDao.MemberAnswerInfoDBDto;
import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.AcademyGeneration;
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

    public AnswerInfo toAnswerInfo(AnswerInfoInterface answerInfoDto, Long memberId) {
        return AnswerInfo.builder()
                .answerId(answerInfoDto.getAnswer().getId())
                .writerId(answerInfoDto.getWriterId())
                .profileImage(answerInfoDto.getWriterProfileImage())
                .nickname(answerInfoDto.getWriterNickname())
                .writerGeneration(answerInfoDto.getWriterAcademyGeneration().orElse(AcademyGeneration.UNKNOWN).getGeneration())
                .content(answerInfoDto.getAnswer().getContent())
                .isMine(answerInfoDto.getWriterId().equals(memberId))
                .isReported(answerInfoDto.getIsReported())
                .isLiked(answerInfoDto.getIsLiked())
                .writeAt(answerInfoDto.getAnswer().getCreatedAt().toString())
                .commentCount(answerInfoDto.getAnswer().getCommentCount())
                .heartCount(answerInfoDto.getAnswer().getHeartCount())
                .build();
    }

    public MemberAnswerInfo toMemberAnswerInfo(MemberAnswerInfoDBDto memberAnswer) {
        return MemberAnswerInfo.builder()
                .questionId(memberAnswer.getAnswer().getQuestion().getId())
                .answerId(memberAnswer.getAnswer().getId())
                .writerId(memberAnswer.getAnswer().getMember().getId())
                .nickname(memberAnswer.getAnswer().getMember().getNickname())
                .profileImage(memberAnswer.getAnswer().getMember().getProfileImage())
                .writerGeneration(memberAnswer.getWriterAcademyGeneration().orElse(AcademyGeneration.UNKNOWN).getGeneration())
                .content(memberAnswer.getAnswer().getContent())
                .heartCount(memberAnswer.getAnswer().getHeartCount())
                .commentCount(memberAnswer.getAnswer().getCommentCount())
                .writeAt(memberAnswer.getAnswer().getCreatedAt().toString())
                .isLiked(memberAnswer.getIsLiked())
                .build();
    }
}
