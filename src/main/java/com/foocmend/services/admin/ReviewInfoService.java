package com.foocmend.services.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foocmend.entities.EntityReview;
import com.foocmend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewInfoService {

    private final ReviewRepository repository;
/*
    public <T> T get(String code, Class<T> clazz) {
        return get(code, clazz, null);
    }

    public <T> T get(String code, TypeReference<T> type) {
        return get(code, null, type);
    }

    public <T> T get(String code, Class<T> clazz, TypeReference<T> typeReference) {
        try {
            EntityReview configs = repository.findById(code).orElse(null);
            if (configs == null || configs.getValue() == null || configs.getValue().isBlank()) {
                return null;
            }

            String value = configs.getValue();

            ObjectMapper om = new ObjectMapper();
            T data = null;
            try {

                if (clazz == null) data = om.readValue(value, typeReference);
                else data = om.readValue(value, clazz);

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return data;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;
        }
    }

 */
}
