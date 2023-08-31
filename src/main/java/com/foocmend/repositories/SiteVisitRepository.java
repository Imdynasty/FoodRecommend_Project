package com.foocmend.repositories;

import com.foocmend.entities.SiteVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface SiteVisitRepository extends JpaRepository<SiteVisit, Long>, QuerydslPredicateExecutor<SiteVisit> {

    @Modifying
    @Transactional
    @Query(value = "insert into sitevisit (number, visitDate) " +
            "values (1, date_format(now(), '%y%m%d'))", nativeQuery = true)
    void add();

    @Query(value = "select visitDate from sitevisit " +
            "where date_format(visitDate, '%y%m%d') = " +
            "date_format(now(), '%y%m%d')", nativeQuery = true)
    String select();

    @Modifying
    @Transactional
    @Query(value = "update sitevisit set number=number+1 " +
            "where date_format(visitDate, '%y%m%d') = " +
            "date_format(now(), '%y%m%d')", nativeQuery = true)
    void update();

    @Query(value = "select number from sitevisit " +
            "where date_format(visitDate, '%y%m%d') = " +
            "date_format(now(), '%y%m%d')", nativeQuery = true)
    long count();

}
