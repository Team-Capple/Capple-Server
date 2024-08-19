package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AnswerMapper {
    public Answer toAnswerEntity(AnswerRequest request, Member member, Question question) {
        return Answer.builder()
                .member(member)
                .question(question)
                .content(request.getAnswer())
                .build();
    }

    public AnswerList toAnswerList(List<AnswerInfo> answerInfoList) {
        return AnswerList.builder()
                .answerInfos(answerInfoList)
                .build();
    }

    public AnswerInfo toAnswerInfo(Answer answer, Long memberId, Boolean isReported) {
        return AnswerInfo.builder()
                .answerId(answer.getId())
                .profileImage(answer.getMember().getProfileImage())
                .nickname(answer.getMember().getNickname())
                .content(answer.getContent())
                .isMyAnswer(answer.getMember().getId() == memberId)
                .isReported(isReported)
                .build();
    }

    public MemberAnswerInfo toMemberAnswerInfo(Answer answer, int heartCount) {
        return MemberAnswerInfo.builder()
                .questionId(answer.getQuestion().getId())
                .answerId(answer.getId())
                .nickname(answer.getMember().getNickname())
                .profileImage(answer.getMember().getProfileImage())
                .content(answer.getContent())
                .heartCount(heartCount)
                .writeAt(answer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public MemberAnswerList toMemberAnswerList(List<MemberAnswerInfo> memberAnswerInfos) {
        return new MemberAnswerList(memberAnswerInfos);
    }

}
