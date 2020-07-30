package com.jdiaz.users.service.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.commons.jdiaz.users.models.entity.User;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repository;

	@Test
	public void findByEmailTest() {

		Optional<User> user = repository.findByEmail("test@mail.com");
		assertEquals(user.get().getUsername(), "test");
		assertEquals(user.get().getEmail(), "test@mail.com");

	}

	@Test
	public void findByUsernameTest() {

		Optional<User> user = repository.findByUsername("test");
		assertEquals(user.get().getUsername(), "test");
		assertEquals(user.get().getEmail(), "test@mail.com");

	}

}
