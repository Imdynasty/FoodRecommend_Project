package com.foocmend.services.restaurant;

import com.foocmend.controllers.admin.RestaurantForm;
import com.foocmend.entities.Category;
import com.foocmend.entities.FileInfo;
import com.foocmend.entities.Restaurant;
import com.foocmend.repositories.CategoryRepository;
import com.foocmend.repositories.RestaurantRepository;
import com.foocmend.services.file.InfoFileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SearchRestaurantService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository repository;
    private final InfoFileService infoFileService;

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
