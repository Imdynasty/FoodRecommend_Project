package com.foocmend.repositories;

import com.foocmend.entities.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartMapper {
    List<Member> categoryCount();

}
