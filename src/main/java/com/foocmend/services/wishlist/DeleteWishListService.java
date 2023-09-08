package com.foocmend.services.wishlist;

import com.foocmend.commons.Utils;
import com.foocmend.controllers.member.wishList;
import com.foocmend.entities.QWishList;
import com.foocmend.entities.Restaurant;
import com.foocmend.entities.WishList;
import com.foocmend.repositories.RestaurantRepository;
import com.foocmend.repositories.WishListRepository;
import com.foocmend.services.restaurant.RestaurantNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteWishListService {
    private final WishListRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final Utils utils;

    public void delete(Long id) {
        int uid = utils.getBrowserId();
        QWishList wishList = QWishList.wishList;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(wishList.uid.eq(uid))
                .and(wishList.restaurant.id.eq(id));

        WishList wish = repository.findOne(builder).orElse(null);
        if (wish == null) return;

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(RestaurantNotFoundException::new);


        repository.delete(wish);

        restaurant.setWishCnt(repository.getCount(id));

        repository.flush();

    }
}
