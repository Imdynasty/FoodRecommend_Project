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

        return list;
    }

    public List<Long> femaleCount() {
        List<Long> list = new ArrayList<>();

        list.add(repository.countByFemale("%korea%"));
        list.add(repository.countByFemale("%america%"));
        list.add(repository.countByFemale("%japan%"));

        return list;
    }

}
