package com.foocmend.services.restaurant;

import com.foocmend.commons.ListData;
import com.foocmend.commons.Pagination;
import com.foocmend.commons.Utils;
import com.foocmend.controllers.admin.RestaurantForm;
import com.foocmend.controllers.restaurant.RestaurantSearchForm;
import com.foocmend.entities.Category;
import com.foocmend.entities.FileInfo;
import com.foocmend.entities.QRestaurant;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.CategoryRepository;
import com.foocmend.repositories.RestaurantRepository;
import com.foocmend.services.file.InfoFileService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchRestaurantService {
    private final JPAQueryFactory factory;
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository repository;
    private final InfoFileService infoFileService;
    private final HttpServletRequest request;

    /**
     * 음식점 정보 개별 조회
     *
     */
    public Restaurant get(Long id) {
        Restaurant item = repository.findById(id).orElseThrow(RestaurantNotFoundException::new);
        addFileInfo(item);

        return item;
    }

    public RestaurantForm getForm(Long id) {
        RestaurantForm form =  new ModelMapper().map(get(id), RestaurantForm.class);
        String gid = form.getGid();
        gid = gid == null || gid.isBlank() ? UUID.randomUUID().toString() : gid;
        form.setGid(gid);

        return form;
    }

    /**
     * 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Restaurant> getList(RestaurantSearchForm search) {
        QRestaurant restaurant = QRestaurant.restaurant;
        BooleanBuilder andBuilder = new BooleanBuilder();

        int page = Utils.getNumber(search.getPage(), 1);
        int limit = Utils.getNumber(search.getLimit(), 20);
        int offset = (page - 1) * limit;

        String sopt = search.getSopt();
        String skey = search.getSkey();
        String type = search.getType();

        /** 조건 및 키워드 검색 S */
        // 음식점 분류에 따른 목록 조회 */
        if (type != null && !type.isBlank()) {
            type = type.trim();
            andBuilder.and(restaurant.type.eq(type));
        }

        if (sopt != null && !sopt.isBlank() && skey != null && !skey.isBlank()) {
            sopt = sopt.trim();
            skey = skey.trim();
            if (sopt.equals("all")) { // 통합 검색
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(restaurant.address.contains(skey))
                        .or(restaurant.roadAddress.contains(skey))
                        .or(restaurant.storeName.contains(skey))
                        .or(restaurant.type.contains(skey))
                        .or(restaurant.description.contains(skey));
                andBuilder.and(orBuilder);
            } else if (sopt.equals("address")) {
                BooleanBuilder orBuilder = new BooleanBuilder();
                orBuilder.or(restaurant.address.contains(skey))
                        .or(restaurant.roadAddress.contains(skey));
                andBuilder.and(orBuilder);

            } else if (sopt.equals("storeName")) {
                andBuilder.and(restaurant.storeName.contains(skey));
            } else if (sopt.equals("type")) {
                andBuilder.and(restaurant.type.contains(skey));
            } else if (sopt.equals("description")) {
                andBuilder.and(restaurant.description.contains(skey));
            }
        }
        /** 조건 및 키워드 검색 E */

        /** 정렬 처리 S */
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        String sort = search.getSort();
        if (sort == null || sort.isBlank()) {
            sort = "createdDt_DESC";
        }

        List<String[]> sorts = Arrays.stream(sort.trim().split(","))
                .map(s -> s.split("_")).toList();
        PathBuilder pathBuilder = new PathBuilder(Restaurant.class, "restaurant");

        for (String[] _sort : sorts) {
            Order direction = Order.valueOf(_sort[1].toUpperCase()); // 정렬 방향
            orderSpecifiers.add(new OrderSpecifier(direction, pathBuilder.get(_sort[0])));
        }
        /** 정렬 처리 E */


        List<Restaurant> items = factory.selectFrom(restaurant)
                .offset(offset)
                .limit(limit)
                .where(andBuilder)
                .orderBy(orderSpecifiers.toArray(OrderSpecifier[]::new))
                .fetch();

        ListData<Restaurant> data = new ListData<>();
        data.setContent(items);

        int total = (int)repository.count(andBuilder);
        Pagination pagination = new Pagination(page, total, 10, limit, request);
        data.setPagination(pagination);

        return data;
    }

    /** 음식점 분류 목록 */
    public List<Category> getCategories() {
       return categoryRepository.findAll();
    }

    public void addFileInfo(Restaurant item) {
        if (item == null) return;

        String gid = item.getGid();

        List<FileInfo> mainImages = infoFileService.getListDone(gid, "main");
        List<FileInfo> listImages = infoFileService.getListDone(gid, "list");
        List<FileInfo> editorImages = infoFileService.getListDone(gid, "editor");

        item.setMainImages(mainImages);
        item.setListImages(listImages);
        item.setEditorImages(editorImages);
    }
}
