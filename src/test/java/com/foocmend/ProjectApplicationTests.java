package com.foocmend;

import com.foocmend.commons.constants.Foods;
import com.foocmend.repositories.MemberRepository;
import com.foocmend.repositories.SiteVisitRepository;
import com.foocmend.services.ChartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

		long result = repository.countByBirth(30, 39);
		System.out.println(result);


	}

}
