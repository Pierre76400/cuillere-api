package fr.softeam.cuillereapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class CuillereApiApplicationDocker {

	public static void main(String[] args) {
		SpringApplication.run(CuillereApiApplicationDocker.class, args);
	}

}
