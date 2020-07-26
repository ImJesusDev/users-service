package com.jdiaz.users.service.models.repositories;

import org.springframework.data.repository.CrudRepository;

import com.jdiaz.users.commons.models.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByEmail(String email);
	
	public User findByUsername(String username);

}
