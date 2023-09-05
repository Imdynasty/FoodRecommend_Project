package com.foocmend.repositories;

import com.foocmend.entities.Member;
import com.foocmend.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {

    Member findByEmail(String email);
    List<Member> findAll();
    Member findEmailByNicknameAndMobile(String nickname,String mobile);

    @Query(value = "select count(*) from member where favoriteFoods like :food and gender='male'", nativeQuery = true)
    long countByMale(@Param("food") String food);

    @Query(value = "select count(*) from member where favoriteFoods like :food and gender='female'", nativeQuery = true)
    long countByFemale(@Param("food") String food);

    default boolean exists(String email) {
        return exists(QMember.member.email.eq(email));
    }

    default boolean existsNickname(String nickname) {
        return exists(QMember.member.nickname.eq(nickname));
    }



}
