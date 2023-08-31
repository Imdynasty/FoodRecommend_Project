package com.foocmend.repositories;

import com.foocmend.entities.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDateTime;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long>, QuerydslPredicateExecutor<SiteVisit> {

    @Query(value = "insert into siteVisit(number, visitDate) values (1, now())", nativeQuery = true)
    SiteVisit add();

}
