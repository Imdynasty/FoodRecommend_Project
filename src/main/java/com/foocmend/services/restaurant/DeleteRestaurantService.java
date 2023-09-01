package com.foocmend.services.restaurant;

import com.foocmend.commons.Utils;
import com.foocmend.controllers.admin.RestaurantForm;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteRestaurantService {
    private final RestaurantRepository repository;
    private final Utils utils;
    public void delete(RestaurantForm form) {
        List<Integer> chkNos = form.getChkNo();
        List<Restaurant> items = new ArrayList<>();
        for (int chkNo : chkNos) {
            Long id = Long.valueOf(utils.getParam("id_" + chkNo));
            Restaurant item = repository.findById(id).orElse(null);
            if (item == null) {
                continue;
            }
            items.add(item);
        }

        repository.deleteAll(items);
        repository.flush();
    }
}
