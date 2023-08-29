package com.foocmend.services;

import com.foocmend.repositories.ChartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartMapper chartMapper;

    public List<Long> getMapper() {
        return chartMapper.categoryCount();
    }

}
