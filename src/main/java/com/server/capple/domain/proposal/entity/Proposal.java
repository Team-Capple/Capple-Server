//package com.server.capple.domain.proposal.entity;
//
//import com.server.capple.domain.member.entity.Member;
//import com.server.capple.global.common.BaseEntity;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.SQLRestriction;
//
//@Getter
//@Builder
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@SQLRestriction("deleted_at is null")
//public class Proposal extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long Id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    @Column(nullable = false)
//    private String content;
//}

// 추후 수정예정(건의함) - 삭제될 수도 있음