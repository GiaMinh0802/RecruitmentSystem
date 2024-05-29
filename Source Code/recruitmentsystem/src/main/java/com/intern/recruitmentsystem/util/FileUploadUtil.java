package com.fpt.recruitmentsystem.util;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

@Component
public class FileUploadUtil {
	
    private String publicUrl = "https://storage.googleapis.com/recruitment-cv-375eb.appspot.com/";
	public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
		String fileCode = RandomStringUtils.randomAlphanumeric(8);
		String fileNameUpload = fileCode + "-" + fileName;
		Bucket bucket = StorageClient.getInstance().bucket();

		bucket.create(fileNameUpload, multipartFile.getInputStream(), multipartFile.getContentType());
        
		return publicUrl + fileNameUpload;
	}
	public String updateAvatar(String oldFileName ,String newFileName, MultipartFile multipartFile) throws IOException {
		
		String fileCode = RandomStringUtils.randomAlphanumeric(8);
		String fileNameUpload = fileCode + "-" + newFileName;
		Bucket bucket = StorageClient.getInstance().bucket();
		Blob blob = bucket.get(oldFileName);
		if(blob == null) {
			
			bucket.create(fileNameUpload, multipartFile.getInputStream(), multipartFile.getContentType());
			return publicUrl + fileNameUpload;
		}

		blob.delete();
		bucket.create(fileNameUpload, multipartFile.getInputStream(), multipartFile.getContentType());
		return publicUrl + fileNameUpload;
        
	}
}
