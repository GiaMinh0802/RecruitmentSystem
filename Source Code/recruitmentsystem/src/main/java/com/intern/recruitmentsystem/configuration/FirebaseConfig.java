package com.fpt.recruitmentsystem.configuration;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

	private final ResourceLoader resourceLoader;

	public FirebaseConfig(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void initialize() {
		try {
			Resource resource = resourceLoader.getResource("classpath:serviceAccountKey.json");
			try (InputStream serviceAccount = resource.getInputStream()) {
				FirebaseOptions options = FirebaseOptions.builder()
						.setCredentials(GoogleCredentials.fromStream(serviceAccount))
						.setStorageBucket("recruitment-cv-375eb.appspot.com")
						.build();
				FirebaseApp.initializeApp(options);
			} catch (IOException e) {
				System.err.println("Không thể tìm thấy tệp serviceAccountKey.json.");
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.err.println("Không thể khởi tạo Firebase.");
			e.printStackTrace();
		}
	}

}

