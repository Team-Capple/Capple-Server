package com.server.capple.config.apns.service;

import com.server.capple.domain.member.entity.Member;

import java.util.List;

public interface ApnsService {
    <T> Boolean sendApns(T request, String ... deviceTokens);
    <T> Boolean sendApns(T request, List<String> deviceTokenList);
    <T> Boolean sendApnsToMembers(T request, Long ... memberIds);
    <T> Boolean sendApnsToMembers(T request, List<Long> memberIdList);
    <T> Boolean sendApnsToAllMembers(T request);
    <T> Boolean sendApnsToAllMembersExceptOne(T request, Long memberId);
}
