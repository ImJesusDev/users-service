package com.jdiaz.users.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jdiaz.users.service.models.service.UserServiceInterface;

@RestController
@RefreshScope
public class UserController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserServiceInterface userService;
	
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userService.findAll());
	}
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(userService.findById(id).getRoles());
	}
	
	@GetMapping("/users/search-username")
	public ResponseEntity<?> searchUsername(@RequestParam String username) {
		return ResponseEntity.ok(userService.findByUsername(username));
	}
	
	@GetMapping("/encrypt-password")
	public ResponseEntity<?> encryptPassword(@RequestParam String password) {
		String property = env.getProperty("config.environment.name");
        return ResponseEntity.ok(property);
	}

}
