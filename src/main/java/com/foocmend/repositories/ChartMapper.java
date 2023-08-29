package com.foocmend.repositories;

import com.foocmend.entities.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChartMapper {
    Long categoryCount();

}
