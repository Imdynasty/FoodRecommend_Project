package com.foocmend;

import com.foocmend.repositories.MemberRepository;
import com.foocmend.repositories.SiteVisitRepository;
import com.foocmend.services.ChartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectApplicationTests {

	@Autowired
	private MemberRepository repository;

	@Autowired
	private SiteVisitRepository visitRepository;

	@Autowired
	private ChartService service;

	@Test
	void contextLoads() throws Exception {

		long result = visitRepository.count();

		System.out.println(result);

	}

}
