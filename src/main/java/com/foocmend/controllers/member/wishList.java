package com.foocmend.controllers.member;

import com.foocmend.commons.rests.JSONData;
import com.foocmend.services.wishlist.DeleteWishListService;
import com.foocmend.services.wishlist.SaveWishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wish")
@RequiredArgsConstructor
public class wishList {
    private final SaveWishListService saveService;
    private final DeleteWishListService deleteService;

    @ResponseBody
    @GetMapping("/save")
    public JSONData<Object> save(Long id) {
        JSONData<Object> data = new JSONData<>();
        try {
            saveService.save(id);
            data.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            data.setSuccess(false);
        }

        return data;
    }

    @ResponseBody
    @GetMapping("/delete")
    public JSONData<Object> delete(Long id) {
        JSONData<Object> data = new JSONData<>();
        try {
            deleteService.delete(id);
            data.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            data.setSuccess(false);
        }

        return data;
    }
}
