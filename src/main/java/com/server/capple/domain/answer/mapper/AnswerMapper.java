package com.server.capple.domain.answer.mapper;

import com.server.capple.domain.answer.dto.AnswerRequest;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerList;
import com.server.capple.domain.answer.dto.AnswerResponse.MemberAnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerInfo;
import com.server.capple.domain.answer.dto.AnswerResponse.AnswerList;
import com.server.capple.domain.answer.entity.Answer;
import com.server.capple.domain.member.entity.Member;
import com.server.capple.domain.question.entity.Question;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AnswerMapper {
    public Answer toAnswerEntity(AnswerRequest request, Member member, Question question) {
        return Answer.builder()
                .member(member)
                .question(question)
                .tags(String.join(" ", request.getTags()) + " ")
                .content(request.getAnswer())
                .build();
    }

    public AnswerList toAnswerList(List<AnswerInfo> answerInfoList) {
        return AnswerList.builder()
                .answerInfos(answerInfoList)
                .build();
    }

    public AnswerInfo toAnswerInfo(Answer answer, Long memberId) {
        return AnswerInfo.builder()
                .profileImage(answer.getMember().getProfileImage())
                .nickname(answer.getMember().getNickname())
                .content(answer.getContent())
                .tags(answer.getTags())
                .isMyAnswer(answer.getMember().getId() == memberId)
                .build();
    }

    public MemberAnswerInfo toMemberAnswerInfo(Answer answer, int heartCount) {
        return MemberAnswerInfo.builder()
                .questionId(answer.getQuestion().getId())
                .nickname(answer.getMember().getNickname())
                .profileImage(answer.getMember().getProfileImage())
                .content(answer.getContent())
                .tags(answer.getTags())
                .heartCount(heartCount)
                .writeAt(answer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public MemberAnswerList toMemberAnswerList(List<MemberAnswerInfo> memberAnswerInfos) {
        return new MemberAnswerList(memberAnswerInfos);
    }

}
