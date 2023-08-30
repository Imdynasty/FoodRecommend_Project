package com.foocmend;

import com.foocmend.repositories.MemberRepository;
import com.foocmend.services.ChartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class ProjectApplicationTests {

	@Autowired
	private MemberRepository repository;

	@Autowired
	private ChartService service;

	@Test
	void contextLoads() throws Exception {

		List<Long> list = new ArrayList<>();
		list.add(repository.countByFavoriteFoodsLike("%korea%"));
		list.add(repository.countByFavoriteFoodsLike("%america%"));
		list.add(repository.countByFavoriteFoodsLike("%japan%"));

		System.out.println(list);

	}

}
