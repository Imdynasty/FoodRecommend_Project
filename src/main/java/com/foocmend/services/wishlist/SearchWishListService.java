package com.foocmend.services.wishlist;

import com.foocmend.commons.Utils;
import com.foocmend.entities.QWishList;
import com.foocmend.entities.WishList;
import com.foocmend.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class SearchWishListService {
    private final WishListRepository repository;
    private final Utils utils;

    public List<WishList> getMine() {
        QWishList wishList = QWishList.wishList;
        int uid = utils.getBrowserId();

        List<WishList> items = (List<WishList>)repository.findAll(wishList.uid.eq(uid), Sort.by(desc("createdDt")));

        return items;
    }

    public List<Long> getMyIds() {
        List<WishList> items = getMine();
        if (items == null || items.isEmpty()) return null;

        return items.stream().map(i -> i.getRestaurant().getId()).toList();
    }
}
