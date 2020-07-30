package com.jdiaz.users.service.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.BindingResult;
import com.commons.jdiaz.users.models.entity.User;
import com.jdiaz.users.service.business.UserBusinessInterface;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = { "Users API" })
@RestController
@RefreshScope
public class UserController {

	@Autowired
	private UserBusinessInterface userBusiness;

	@ApiOperation(value = "Get all users", notes = "Get a List of all users along with the roles", response = User.class)
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok(userBusiness.findAll());
	}

	@ApiOperation(value = "Update photo", notes = "Update user's profile photo", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found.")})
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/users/{id}/update-photo")
	public ResponseEntity<User> updateUserPhoto(@PathVariable Long id, @RequestParam MultipartFile photo)
			throws IOException {
		User updatedUser = userBusiness.updateUserPhoto(id, photo);
		if (!(updatedUser instanceof User)) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update profile", notes = "Update user's profile information", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found.")})
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult result, @PathVariable Long id) {
		if (result.hasErrors()) {
			return validate(result);
		}
		user.setId(id);
		User updatedUser = userBusiness.save(user);
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Update last connection", notes = "Update user's last connection time", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found.")})
	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/update-last-connection/{id}")
	public ResponseEntity<User> updateUserLastConnection(@PathVariable Long id) {
		User updatedUser = userBusiness.updateUserLastConnection(id);
		if (!(updatedUser instanceof User)) {
			return ResponseEntity.notFound().build();
		}
		return new ResponseEntity<User>(updatedUser, HttpStatus.CREATED);
	}

	@ApiOperation(value = "Register user", notes = "Register a new user", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Validation errors.")})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/register-user")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result) {
		if (result.hasErrors()) {
			return validate(result);
		}
		try {
			User newUser = userBusiness.save(user);
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

	@ApiOperation(value = "Get user", notes = "Get the details of an user", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found.")})
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		Optional<User> optionalUser = userBusiness.findById(id);
		if (optionalUser.isPresent()) {
			return ResponseEntity.ok(optionalUser.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@ApiOperation(value = "Search user by username", notes = "Search user by username", response = User.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found.")})
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/users/search-username")
	public ResponseEntity<User> searchUsername(@RequestParam String username) {
		Optional<User> optionalUser = userBusiness.findByUsername(username);
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
