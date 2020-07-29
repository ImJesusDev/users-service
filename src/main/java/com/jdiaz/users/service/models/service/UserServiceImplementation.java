package com.jdiaz.users.service.models.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.commons.jdiaz.users.models.entity.User;
import com.jdiaz.users.service.models.repositories.UserRepository;

@Service
public class UserServiceImplementation implements UserServiceInterface {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${config.aws.s3.bucket-name}")
	private String bucket;

	@Autowired
	private Environment env;

	/* Get all users */
	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return (List<User>) userRepository.findAll();
	}

	/* Save new user */
	@Override
	@Transactional()
	public User save(User user) {
		return userRepository.save(user);
	}

	/* Get user by id */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	/* Get user by email */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/* Get user by Username */
	@Override
	@Transactional(readOnly = true)
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/* Update user last connection */
	@Override
	@Transactional()
	public User updateUserLastConnection(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setLastConnection(new Date());
			User updatedUser = userRepository.save(user);
			return updatedUser;
		} else {
			return null;
		}

	}

	/* Update user profile photo */
	@Override
	@Transactional()
	public User updateUserPhoto(Long id, MultipartFile photo) throws IOException {
		/* Find the user */
		Optional<User> optionalUser = this.findById(id);
		/* If the user is found and the photo is not empty */
		if (optionalUser.isPresent() && !photo.isEmpty()) {
			/* Create and empty file to store the MultipartFile data */
			File file = new File("src/main/resources/targetFile.png");
			try (OutputStream os = new FileOutputStream(file)) {
				/* Write the MultipartFile into the temporal file */
				os.write(photo.getBytes());
				/* Build the file name */
				Date currentDate = new Date();
				String fileName = currentDate.getTime() + ".png";
				/* Destination folder */
				String folder = "users/" + id + "/";
				/* Put Object */
				amazonS3.putObject(new PutObjectRequest(bucket, folder + fileName, file)
						.withCannedAcl(CannedAccessControlList.PublicRead));
				/* Get Object URL */
				String url = ((AmazonS3Client) amazonS3).getResourceUrl(bucket, folder + fileName);
				/* Get the User model */
				User dbUser = optionalUser.get();
				/* Update the photo URL */
				dbUser.setPhotoUrl(url);
				User updatedUser = userRepository.save(dbUser);
				/* Delete temporal file */
				file.delete();
				return updatedUser;
			}

		} else {
			return null;
		}

	}

}
