package com.foocmend.services.restaurant;

import com.foocmend.controllers.admin.RestaurantForm;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.FileInfoRepository;
import com.foocmend.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 음식점 정보 저장 및 수정 처리
 *
 */
@Service
@RequiredArgsConstructor
public class SaveRestaurantService {
    private final RestaurantRepository repository;
    private final FileInfoRepository fileInfoRepository;
    public void save(RestaurantForm form) {
        String gid = form.getGid();
        Long id = form.getId();

        Restaurant restaurant = null;
        if (id != null) { // 정보 수정
            restaurant = repository.findById(id).orElseThrow(RestaurantNotFoundException::new);
        } else { // 정보 추가
            restaurant = new Restaurant();
        }
        restaurant.setGid(form.getGid());
        restaurant.setZipcode(form.getZipcode());
        restaurant.setZonecode(form.getZonecode());
        restaurant.setRoadAddress(form.getRoadAddress());
        restaurant.setAddress(form.getAddress());
        restaurant.setDescription(form.getDescription());
        restaurant.setType(form.getType());
        restaurant.setStoreName(form.getStoreName());
        restaurant.setXpos(form.getXpos());
        restaurant.setYpos(form.getYpos());
        restaurant.setHomepage(form.getHomepage());
        repository.saveAndFlush(restaurant);

        form.setId(restaurant.getId());

        fileInfoRepository.processDone(gid);
    }

}
