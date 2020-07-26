package com.jdiaz.users.service.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdiaz.users.commons.models.entity.User;
import com.jdiaz.users.service.clients.AuthFeignClient;
import com.jdiaz.users.service.models.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserServiceInterface{
	
	@Autowired
	private AuthFeignClient authClient;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public String encryptPassword(String password) {
		return authClient.encryptPassword(password);
	}

}
