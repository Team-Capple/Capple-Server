package com.server.capple.domain.member.entity;

import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long Id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;


    private String profileImage;
}
