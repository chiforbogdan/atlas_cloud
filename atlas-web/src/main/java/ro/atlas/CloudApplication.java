package ro.atlas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = {"ro.atlas.repository"})
@SpringBootApplication
public class CloudApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CloudApplication.class, args);
	}
}
