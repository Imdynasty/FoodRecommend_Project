package com.foocmend.services;

import com.foocmend.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final MemberRepository repository;

    public List<Long> maleCount() {
        List<Long> list = new ArrayList<>();

        list.add(repository.countByMale("%korea%"));
        list.add(repository.countByMale("%america%"));
        list.add(repository.countByMale("%japan%"));
        list.add(repository.countByMale("%chinese%"));
        list.add(repository.countByMale("%snack%"));
        list.add(repository.countByMale("%cafe%"));
        list.add(repository.countByMale("%family_restaurant%"));
        list.add(repository.countByMale("%sashimi%"));
        list.add(repository.countByMale("%fast_food%"));
        list.add(repository.countByMale("%buffet%"));
        list.add(repository.countByMale("%pub%"));
        list.add(repository.countByMale("%etc%"));

        return list;
    }

    public List<Long> femaleCount() {
        List<Long> list = new ArrayList<>();

        list.add(repository.countByFemale("%korea%"));
        list.add(repository.countByFemale("%america%"));
        list.add(repository.countByFemale("%japan%"));
        list.add(repository.countByFemale("%chinese%"));
        list.add(repository.countByFemale("%snack%"));
        list.add(repository.countByFemale("%cafe%"));
        list.add(repository.countByFemale("%family_restaurant%"));
        list.add(repository.countByFemale("%sashimi%"));
        list.add(repository.countByFemale("%fast_food%"));
        list.add(repository.countByFemale("%buffet%"));
        list.add(repository.countByFemale("%pub%"));
        list.add(repository.countByFemale("%etc%"));

        return list;
    }

}
