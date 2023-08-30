package com.foocmend.services;

import com.foocmend.repositories.ChartMapper;
import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final MemberRepository repository;
    List<Long> list = new ArrayList<>();

    public List<Long> getMapper() {
        list.add(repository.countByFavoriteFoodsLike("%korea%"));
        list.add(repository.countByFavoriteFoodsLike("%america%"));
        list.add(repository.countByFavoriteFoodsLike("%japan%"));

        return list;
    }

}
