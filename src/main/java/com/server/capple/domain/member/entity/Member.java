package com.server.capple.domain.member.entity;

import com.server.capple.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//@SQLRestriction("deleted_at is null")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String sub;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String profileImage;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public void updateRole(Role role) {this.role = role;}

    public void resignMember() {
        this.nickname = "알 수 없음";
        this.email = "Resign Member";
        this.sub = "Resign Member";
        delete();
    }
}
