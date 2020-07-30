package com.jdiaz.users.service.business;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.commons.jdiaz.users.models.entity.User;

public interface UserBusinessInterface {

	public List<User> findAll();

	public User save(User user);

	public User updateUserPhoto(Long id, MultipartFile photo) throws IOException;

	public Optional<User> findById(Long id);

	public Optional<User> findByEmail(String email);

	public Optional<User> findByUsername(String username);

	public User updateUserLastConnection(Long id);

}
