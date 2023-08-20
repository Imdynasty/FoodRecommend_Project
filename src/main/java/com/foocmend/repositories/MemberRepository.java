package com.foocmend.repositories;

import com.foocmend.entities.Member;
import com.foocmend.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {

    Member findByEmail(String email);

    default boolean exists(String email) {
        return exists(QMember.member.email.eq(email));
    }

    default boolean existsNickname(String nickname) {
        return exists(QMember.member.nickname.eq(nickname));
    }

}
