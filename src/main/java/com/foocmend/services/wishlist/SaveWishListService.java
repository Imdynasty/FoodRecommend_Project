package com.foocmend.services.wishlist;


import com.foocmend.commons.Utils;
import com.foocmend.entities.Restaurant;
import com.foocmend.entities.WishList;
import com.foocmend.repositories.RestaurantRepository;
import com.foocmend.repositories.WishListRepository;
import com.foocmend.services.restaurant.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveWishListService {
    private final WishListRepository repository;
    private final RestaurantRepository restaurantRepository;
    private final Utils utils;

    public void save(Long id) {
        int uid = utils.getBrowserId();

        if (repository.exists(uid, id)) {
            return;
        }

        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(RestaurantNotFoundException::new);

        WishList wish = WishList.builder()
                .uid(uid)
                .restaurant(restaurant)
                .build();

        repository.saveAndFlush(wish);

        restaurant.setWishCnt(repository.getCount(id));

        restaurantRepository.flush();

    }
}
