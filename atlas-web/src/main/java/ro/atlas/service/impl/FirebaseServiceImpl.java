package ro.atlas.service.impl;

import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import ro.atlas.properties.AtlasProperties;
import ro.atlas.service.FirebaseService;

@Component
public class FirebaseServiceImpl implements FirebaseService {

	private static final Logger LOG = LoggerFactory.getLogger(AtlasOwnerServiceImpl.class);
	private static final String FIREBASE_NOTIFICATION_TITLE = "ATLAS Commands are waiting for approval";

	@Autowired
	private AtlasProperties properties;

	@PostConstruct
	public void initialize() {
		try {
			FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials
					.fromStream(new ClassPathResource(properties.getFirebaseConfigurationFile()).getInputStream()))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			} else {
				FirebaseApp.getInstance();
			}

			LOG.info("Firebase app has been initialized");
		} catch (IOException e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Async
	@Override
	public void sendPushNotification(String firebaseToken) {
		LOG.info("Sending firebase notification to token: {} on thread {}", firebaseToken, Thread.currentThread().getName());

		Notification notification = new Notification(FIREBASE_NOTIFICATION_TITLE, new Date().toString());
		AndroidConfig androidConfig = AndroidConfig.builder()
				.setPriority(Priority.HIGH)
				.build();
		Message message = Message.builder()
				.setToken(firebaseToken)
				.setNotification(notification)
				.setAndroidConfig(androidConfig)
				.build();

		String response = null;
		try {
			response = FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {
			LOG.error("Firebase exception: {}", e.getMessage());
			e.printStackTrace();
		}

		LOG.info("Firebase response: {}", response);
	}
}
