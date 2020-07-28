package com.jdiaz.users.service.models.service;

import java.util.List;
import java.util.Optional;

import com.commons.jdiaz.users.models.entity.User;

public interface UserServiceInterface {

	public List<User> findAll();
	
	public User save(User user);

	public Optional<User> findById(Long id);

	public Optional<User> findByEmail(String email);

	public Optional<User> findByUsername(String username);

}
