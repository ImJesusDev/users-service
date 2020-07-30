package com.jdiaz.users.service.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class AwsS3Helper {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${config.aws.s3.bucket-name}")
	private String bucket;

	public String uploadMultipartFile(MultipartFile multipartFile, String folder) throws IOException {
		Date currentDate = new Date();
		String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String fileName = currentDate.getTime() + "." + extension;
		/* Create and empty file to store the MultipartFile data */
		File file = new File("src/main/resources/targetFile." + extension);
		/* Write the MultipartFile into the temporal file */
		try (OutputStream os = new FileOutputStream(file)) {
			os.write(multipartFile.getBytes());
		}

		/* Put Object */
		amazonS3.putObject(
				new PutObjectRequest(bucket, folder + fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));

		/* Get Object URL */
		String url = ((AmazonS3Client) amazonS3).getResourceUrl(bucket, folder+ fileName);
		/* Delete temporal file */
		file.delete();

		return url;
	}

}
