package com.jdiaz.users.service.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commons.jdiaz.users.models.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByEmail(String email);
	
	public Optional<User> findByUsername(String username);

}
