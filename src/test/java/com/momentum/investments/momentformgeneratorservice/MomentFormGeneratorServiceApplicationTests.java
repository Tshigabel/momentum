package com.momentum.investments.momentformgeneratorservice;

import com.amazonaws.services.s3.AmazonS3;
import com.momentum.investments.momentformgeneratorservice.config.InitFileStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.profiles.active=mock",
})
class MomentFormGeneratorServiceApplicationTests {


	@MockBean
	AmazonS3 amazonS3;

	@MockBean
	InitFileStore initFileStore;

	@Test
	void contextLoads() {
	}

}
