package com.foocmend.services.restaurant;

import com.foocmend.commons.CommonException;
import org.springframework.http.HttpStatus;

public class RestaurantNotFoundException extends CommonException {
    public RestaurantNotFoundException() {
        super(bundleValidation.getString("restaurant.notFound"), HttpStatus.BAD_REQUEST);
    }
}
