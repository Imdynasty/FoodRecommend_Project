package com.foocmend.repositories;

import com.foocmend.entities.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long>, QuerydslPredicateExecutor<SearchHistory> {
}
