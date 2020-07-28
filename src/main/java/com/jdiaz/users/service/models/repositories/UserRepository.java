package com.jdiaz.users.service.models.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.commons.jdiaz.users.models.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByUsername(String username);

}
