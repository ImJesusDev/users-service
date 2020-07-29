package com.jdiaz.users.service.controllers;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import com.jdiaz.users.service.models.service.UserServiceInterface;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import com.commons.jdiaz.users.models.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RefreshScope
public class UserController {

	@Autowired
	private UserServiceInterface userService;

	/* Get all users */
	@GetMapping("/users")
	public ResponseEntity<?> getUsers() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	/* Update users photo */
	@PutMapping("/users/{id}/update-photo")
	public ResponseEntity<?> updateUserPhoto(@PathVariable Long id, @RequestParam MultipartFile photo)
			throws IOException {
		User updatedUser = userService.updateUserPhoto(id, photo);
		if (!(updatedUser instanceof User)) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}

	/* Update users profile */
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
		if (result.hasErrors()) {
			return validate(result);
		}
		user.setId(id);
		User updatedUser = userService.save(user);
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}
	
	/* Update users last connection */
	@PutMapping("/update-last-connection/{id}")
	public ResponseEntity<?> updateUserLastConnection(@PathVariable Long id) {
		User updatedUser = userService.updateUserLastConnection(id);
		if (!(updatedUser instanceof User)) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}

	/* Register a new user */
	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			return validate(result);
		}
		try {
			User newUser = userService.save(user);
			return new ResponseEntity<User>(newUser, HttpStatus.CREATED);

		} catch (DataIntegrityViolationException e) {
			Map<String, Object> response = new HashMap<String, Object>();
			String cause = e.getRootCause().getMessage();

			if (cause.contains("duplicate key") && cause.contains("email")) {
				response.put("error", "Email already exists");
			}
			if (cause.contains("duplicate key") && cause.contains("username")) {
				response.put("error", "Username already exists");
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

		}
	}

	/* Get user by id */
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		Optional<User> optionalUser = userService.findById(id);
		if (optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/* Get user by username */
	@GetMapping("/users/search-username")
	public ResponseEntity<?> searchUsername(@RequestParam String username) {
		Optional<User> optionalUser = userService.findByUsername(username);
		if (optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	/* Function to validate @RequestBody */
	private ResponseEntity<?> validate(BindingResult result) {
		Map<String, Object> errors = new HashMap<>();
		result.getFieldErrors().forEach(error -> {
			errors.put(error.getField(), "The field " + error.getField() + " " + error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errors);

	}

}
