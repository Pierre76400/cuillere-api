package fr.softeam.cuillereapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CuillereApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuillereApiApplication.class, args);
	}

}
