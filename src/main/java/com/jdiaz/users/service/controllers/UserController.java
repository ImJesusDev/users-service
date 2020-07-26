package com.jdiaz.users.service.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdiaz.users.commons.models.entity.User;
import com.jdiaz.users.service.models.service.UserServiceInterface;

@RestController
@RefreshScope
public class UserController {

	@Autowired
	private UserServiceInterface userService;

	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userService.findAll());
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		Optional<User> optionalUser = userService.findById(id);
		if(optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/users/search-username")
	public ResponseEntity<?> searchUsername(@RequestParam String username) {
		Optional<User> optionalUser = userService.findByUsername(username);
		if(optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
