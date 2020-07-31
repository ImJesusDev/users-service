package com.jdiaz.users.service.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.jdiaz.users.service.configurations.AWSConfiguration;
import com.jdiaz.users.service.helpers.AwsS3Helper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)

public class UserControllerIT {

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private AWSConfiguration awConfiguration;

	@MockBean
	private AwsS3Helper awsS3Helper;

	@Test
	public void contextLoads() throws JSONException {
		String response = this.restTemplate.getForObject("/users", String.class);
		JSONAssert.assertEquals("[{id: 1, email: test@mail.com}]", response, false);

	}

}
