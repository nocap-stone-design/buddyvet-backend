package com.capstone.buddyvet.common.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capstone.buddyvet.common.enums.ErrorCode;
import com.capstone.buddyvet.common.exception.RestApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.server.local}")
	private String uploadFolder;

	@Value("${cloud.aws.s3.bucket.name}")
	private String bucket;

	@Value("${cloud.aws.s3.bucket.directory}")
	private String bucketDirectory;

	@Value("${cloud.aws.s3.url}")
	private String awsUrl;

	public List<String> uploadFiles(List<MultipartFile> files, String directory) {
		List<String> uploadPaths = new ArrayList<>();

		try {
			for (MultipartFile file : files) {
				File convertFile = convert(file)
					.orElseThrow(() -> new RestApiException(ErrorCode.FILE_CONVERT_ERROR));
				uploadPaths.add(upload(convertFile, directory));
			}
			return uploadPaths;
		} catch (IOException e) {
			throw new RestApiException(ErrorCode.FILE_UPLOAD_ERROR);
		}
	}

	public String uploadSingleFile(MultipartFile file, String directory) {
		try {
			File convertFile = convert(file)
				.orElseThrow(() -> new RestApiException(ErrorCode.FILE_CONVERT_ERROR));
			return upload(convertFile, directory);
		} catch (IOException e) {
			throw new RestApiException(ErrorCode.FILE_UPLOAD_ERROR);
		}
	}

	private Optional<File> convert(MultipartFile file) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

		String newFilename = UUID.randomUUID().toString() + extension;
		File convertFile = new File(uploadFolder, newFilename);
		InputStream fileStream = file.getInputStream();
		FileUtils.copyInputStreamToFile(fileStream, convertFile);

		try {
			file.transferTo(convertFile);
			return Optional.of(convertFile);
		} catch (IllegalStateException e) {
			throw new RestApiException(ErrorCode.FILE_CONVERT_ERROR);
		}
	}

	public String upload(File uploadFile, String dirName) {
		String fileName = bucketDirectory + "/" + dirName + "/" + uploadFile.getName();
		String uploadImageUrl = putS3(uploadFile, fileName);
		removeNewFile(uploadFile);
		return awsUrl + fileName;
	}

	private String putS3(File uploadFile, String fileName) {
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.debug("파일이 삭제되었습니다.");
		} else {
			log.info("파일이 삭제되지 못했습니다.");
		}
	}
}
