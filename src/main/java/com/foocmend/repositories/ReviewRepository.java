package com.foocmend.repositories;

import com.foocmend.controllers.admin.Review;
import com.foocmend.entities.EntityReview;
import com.foocmend.entities.Member;
import com.foocmend.entities.QEntityReview;
import com.foocmend.entities.QMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ReviewRepository extends JpaRepository<EntityReview, String>, QuerydslPredicateExecutor<EntityReview> {

    EntityReview findByNickname(String nickname);
    List<EntityReview> findAll();


    default boolean exists(String nickname) {
        return exists(QEntityReview.entityReview.nickname.eq(nickname));
    }

    default boolean existsNickname(String nickname) {
        return exists(QEntityReview.entityReview.nickname.eq(nickname));
    }


}
