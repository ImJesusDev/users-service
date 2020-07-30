package com.jdiaz.users.service.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.commons.jdiaz.users.models.entity.Role;
import com.commons.jdiaz.users.models.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jdiaz.users.service.business.UserBusinessInterface;

@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserBusinessInterface userBusiness;

	/* Create a list of roles to pass to the user model */
	private List<Role> roles = Arrays.asList(new Role((long) 1, "ROLE_USER"));
	/* Create the user model */
	private User user = new User((long) 1, "Jesus", "Diaz", "jesus@mail.com", "jesusdiaz", "nophoto", "pass", true, new Date(),
			new Date(), roles);

	/* Create an optional to match the business method return data */
	private Optional<User> optionalUser = Optional.of(user);

	@Test
	public void getAllUsersTest() throws Exception {

		// Mock the business method call
		when(userBusiness.findAll()).thenReturn(Arrays.asList(user));

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.get("/users").accept(MediaType.APPLICATION_JSON);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);

		// Assert the result
		int length = context.read("$.length()");
		assertThat(length).isEqualTo(1);

		List<Integer> ids = context.read("$..id");

		assertThat(ids).containsExactly(1, 1);

		String username = context.read("$.[0].username");

		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void getUserTest() throws Exception {

		// Mock the business method call
		when(userBusiness.findById((long) 1)).thenReturn(optionalUser);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.get("/users/1").accept(MediaType.APPLICATION_JSON);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void searchUsernameTest() throws Exception {

		// Mock the business method call
		when(userBusiness.findByUsername("jesusdiaz")).thenReturn(optionalUser);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.get("/users/search-username?username=jesusdiaz")
				.accept(MediaType.APPLICATION_JSON);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void updateUserLastConnectionTest() throws Exception {

		// Mock the business method call
		when(userBusiness.updateUserLastConnection((long) 1)).thenReturn(user);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.put("/update-last-connection/1")
				.accept(MediaType.APPLICATION_JSON);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void updateUserTest() throws Exception {

		// Mock the business method call
		when(userBusiness.save(any(User.class))).thenReturn(user);

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(user);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.put("/users/1").accept(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("UTF-8").content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();

		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void registerUserTest() throws Exception {

		// Mock the business method call
		when(userBusiness.save(any(User.class))).thenReturn(user);

		ObjectMapper mapper = new ObjectMapper();

		String jsonString = mapper.writeValueAsString(user);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.post("/register-user").accept(MediaType.APPLICATION_JSON_VALUE)
				.characterEncoding("UTF-8").content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE);

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();

		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

	@Test
	public void updateUserPhotoTest() throws Exception {
		// Mock the photo
		MockMultipartFile photo = new MockMultipartFile("photo", "photo.png", MediaType.IMAGE_PNG_VALUE,
				"<<png data>>".getBytes());

		// Mock the business method call
		when(userBusiness.updateUserPhoto((long) 1, photo)).thenReturn(user);

		// Build the request
		RequestBuilder request = MockMvcRequestBuilders.multipart("/users/1/update-photo").file(photo)
				.accept(MediaType.APPLICATION_JSON);

		// Override the default method of multipart to make a put request
		((MockHttpServletRequestBuilder) request).with(new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.setMethod("PUT");
				return request;
			}
		});

		// Get the response
		MvcResult mvcResult = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		DocumentContext context = JsonPath.parse(response);
		String username = context.read("$.username");
		// Assert the result
		assertThat(username).isEqualTo("jesusdiaz");

	}

}
