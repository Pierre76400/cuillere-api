package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.AvisCreationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.stream.IntStream;

public class PerfTest {
	@Test
	void test(){
		callGetRestaurant();
	}

	//
	@Test
	void testParrallelGetRestaurant(){
		IntStream.range(0, 1000000).parallel().forEach(x->callGetRestaurant());
	}


	@Test
	void testCreerAvis(){
		callPostAvis();
	}

	private static void callGetRestaurant() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
			= "http://localhost:8080/restaurants";
		ResponseEntity<String> response
			= restTemplate.getForEntity(fooResourceUrl , String.class);
		System.out.println(" " + Thread.currentThread().getName());
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	private static void callPostAvis() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
			= "http://localhost:8080/avis";

		AvisCreationDto avisCreation=new AvisCreationDto();
		avisCreation.setAuteur("Paul");
		avisCreation.setNote(4l);
		avisCreation.setIdRestaurant(1l);
		avisCreation.setCommentaire("Pas mal");

		HttpEntity<AvisCreationDto> request = new HttpEntity<>(avisCreation);

		ResponseEntity<Long> response
			= restTemplate.postForEntity(fooResourceUrl ,  request, Long.class);
		System.out.println(" " + Thread.currentThread().getName());
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
}
