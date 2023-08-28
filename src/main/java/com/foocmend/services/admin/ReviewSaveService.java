package com.foocmend.services.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foocmend.entities.EntityReview;
import com.foocmend.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewSaveService {

    private final ReviewRepository repository;
/*
    public <T> void save(String code, T t) {

        EntityReview configs = repository.findById(code).orElseGet(EntityReview::new);

        ObjectMapper om = new ObjectMapper();
        String value = null;
        try {
            value = om.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        configs.setCode(code);
        configs.setValue(value);

        repository.saveAndFlush(configs);
    }

 */
}
