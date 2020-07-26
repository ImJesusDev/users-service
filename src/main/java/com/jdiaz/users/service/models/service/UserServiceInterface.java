package com.jdiaz.users.service.models.service;

import java.util.List;

import com.jdiaz.users.commons.models.entity.User;;

public interface UserServiceInterface {

	public List<User> findAll();

	public User findById(Long id);

	public User findByEmail(String email);

	public User findByUsername(String username);

	public String encryptPassword(String password);
}
