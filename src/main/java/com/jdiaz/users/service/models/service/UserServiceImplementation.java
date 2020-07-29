package com.jdiaz.users.service.models.service;


import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.commons.jdiaz.users.models.entity.User;
import com.jdiaz.users.service.components.AWS;
import com.jdiaz.users.service.models.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserServiceInterface {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AWS awsComponent;

	/* Get all users */
	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	/* Save new user */
	@Override
	@Transactional()
	public User save(User user) {
		return userRepository.save(user);
	}

	/* Get user by id */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	/* Get user by email */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/* Get user by Username */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/* Update user last connection */
	@Override
	@Transactional()
	public User updateUserLastConnection(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setLastConnection(new Date());
			User updatedUser = userRepository.save(user);
			return updatedUser;
		} else {
			return null;
		}

	}

	/* Update user profile photo */
	@Override
	@Transactional()
	public User updateUserPhoto(Long id, MultipartFile photo) throws IOException {

		/* Find the user */
		Optional<User> optionalUser = this.findById(id);
		/* If the user is found and the photo is not empty */
		if (optionalUser.isPresent() && !photo.isEmpty()) {
			/* Set the folder */
			String folder = "users/" + id + "/";
			/* Upload file and get URL */
			String url = awsComponent.uploadMultipartFile(photo, folder);
			/* Get the User model */
			User dbUser = optionalUser.get();
			/* Update the photo URL */
			dbUser.setPhotoUrl(url);
			User updatedUser = userRepository.save(dbUser);
			return updatedUser;

		} else {
			return null;
		}

	}

}
