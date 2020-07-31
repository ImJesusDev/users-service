package com.jdiaz.users.service.business;

import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.commons.jdiaz.users.models.entity.Role;
import com.commons.jdiaz.users.models.entity.User;
import com.jdiaz.users.service.helpers.AwsS3Helper;
import com.jdiaz.users.service.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserBusinessTest {

	@Mock
	private UserRepository repository;

	@Mock
	private AwsS3Helper awsS3HelperMock;

	@InjectMocks
	private UserBusinessImplementation userBusiness;

	private List<Role> roles = Arrays.asList(new Role((long) 1, "ROLE_USER"));

	private User user = new User((long) 1, "Jesus", "Diaz", "jesus@mail.com", "jesusdiaz", "nophoto", "pass", true,
			new Date(), new Date(), roles);

	private Optional<User> optionalUser = Optional.of(user);

	private MockMultipartFile photo = new MockMultipartFile("photo", "photo.png", MediaType.IMAGE_PNG_VALUE,
			"<<png data>>".getBytes());

	@Test
	public void getAllUsersTest() {

		when(repository.findAll()).thenReturn(Arrays.asList(user));

		List<User> users = userBusiness.findAll();

		assertEquals(users.get(0).getFirstName(), user.getFirstName());

	}

	@Test
	public void saveUserTest() {

		when(repository.save(any(User.class))).thenReturn(user);

		User newUser = userBusiness.save(user);

		assertEquals(newUser.getFirstName(), user.getFirstName());

	}

	@Test
	public void findByIdTest() {

		when(repository.findById((long) 1)).thenReturn(optionalUser);

		Optional<User> foundOptionalUser = userBusiness.findById((long) 1);

		assertEquals(foundOptionalUser.get().getFirstName(), user.getFirstName());

	}

	@Test
	public void findByIdEmail() {

		when(repository.findByEmail("jesus@mail.com")).thenReturn(optionalUser);

		Optional<User> foundOptionalUser = userBusiness.findByEmail("jesus@mail.com");

		assertEquals(foundOptionalUser.get().getEmail(), user.getEmail());

	}

	@Test
	public void findByIdUsername() {

		when(repository.findByUsername("jesusdiaz")).thenReturn(optionalUser);

		Optional<User> foundOptionalUser = userBusiness.findByUsername("jesusdiaz");

		assertEquals(foundOptionalUser.get().getUsername(), user.getUsername());

	}

	@Test
	public void updateUserLastConnectionTest() {

		when(repository.findById((long) 1)).thenReturn(optionalUser);
		when(repository.save(any(User.class))).thenReturn(user);

		User updatedUser = userBusiness.updateUserLastConnection((long) 1);

		assertEquals(updatedUser.getFirstName(), user.getFirstName());

	}

	@Test
	public void updateUserPhoto() throws IOException {

		when(repository.findById((long) 1)).thenReturn(optionalUser);
		when(repository.save(any(User.class))).thenReturn(user);
		when(awsS3HelperMock.uploadMultipartFile(any(MockMultipartFile.class), any(String.class)))
				.thenReturn("https://aws.s3.com/photo.png");

		User updatedUser = userBusiness.updateUserPhoto((long) 1, photo);

		assertEquals(updatedUser.getFirstName(), user.getFirstName());

	}

}
