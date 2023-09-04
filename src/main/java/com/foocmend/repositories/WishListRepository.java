package com.foocmend.repositories;

import com.foocmend.entities.QWishList;
import com.foocmend.entities.WishList;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface WishListRepository extends JpaRepository<WishList, Long>, QuerydslPredicateExecutor<WishList> {
    default boolean exists(int uid, Long id) {
        QWishList wishList = QWishList.wishList;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(wishList.uid.eq(uid))
                .and(wishList.restaurant.id.eq(id));

        return exists(builder);
    }

    default int getCount(Long id) {
        QWishList wishList = QWishList.wishList;
        return (int)count(wishList.restaurant.id.eq(id));
    }
}
