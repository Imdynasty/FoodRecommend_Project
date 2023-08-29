package com.foocmend.services;

import com.foocmend.entities.Member;
import com.foocmend.repositories.ChartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartMapper chartMapper;

    public Long getMapper() {
        return chartMapper.categoryCount();
    }

}
