package com.jdiaz.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.commons.jdiaz.users.models.entity.Role;
import com.commons.jdiaz.users.models.entity.User;
import com.jdiaz.users.service.models.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserServiceTest {
	
	@Autowired
	private UserRepository userService;

	@Test
    public void givenUserRepository_whenSaveAndRetreiveEntity_thenOK() {
		
		User user = new User();
		user.setFirstName("Jesus");
		user.setLastName("Diaz");
		user.setEmail("jesus@mail.com");
		user.setUsername("jesusdiaz");
		user.setCreatedAt(new Date());
		user.setLastConnection(new Date());
		user.setPassword("123456");
		user.setEnabled(true);
		List<Role> roles = new ArrayList<>();
		Role role = new Role();
		role.setId((long) 1);
		role.setName("ROLE_USER");
		roles.add(role);
		user.setRoles(roles);
		User newUser = userService.save(user);
		Optional<User> foundUser = userService.findById(newUser.getId());

        assertEquals(foundUser.get().getId(), newUser.getId());
    }

}
