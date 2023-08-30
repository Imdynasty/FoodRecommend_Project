package com.foocmend.repositories;

import com.foocmend.entities.Member;
import com.foocmend.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {

    Member findByEmail(String email);
    List<Member> findAll();
    Member findByEmailAndNickname(String email, String nickname);
    Member findByEmailAndNicknameAndMobile(String email, String nickname, String mobile);

    default boolean exists(String email) {
        return exists(QMember.member.email.eq(email));
    }

    default boolean existsNickname(String nickname) {
        return exists(QMember.member.nickname.eq(nickname));
    }

}
