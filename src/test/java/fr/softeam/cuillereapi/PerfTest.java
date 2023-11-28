package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.AvisCreationDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class PerfTest {
	@Test
	void test(){
		callGetRestaurant(1);
	}

	//
	@Test
	void testParrallelGetRestaurant(){
		AtomicInteger cpt= new AtomicInteger();
		IntStream.range(0, 10_000_000).parallel().forEach(x->callGetRestaurant(cpt.getAndIncrement()));
	}


	@Test
	void testCreerAvis(){
		callPostAvis(1);
	}

	@Test
	void testParrallelCreerAvis(){
		AtomicInteger cpt= new AtomicInteger();
		IntStream.range(0, 100).parallel().forEach(x->callPostAvis(cpt.getAndIncrement()));
	}

	private static void callGetRestaurant(int x) {
		RestTemplate restTemplate = new RestTemplate();
		//FIXME à la place trouver une facon de récupérer dynmiquement l'id
		String fooResourceUrl = "http://localhost:8080/restaurants/160956";
		ResponseEntity<String> response
			= restTemplate.getForEntity(fooResourceUrl , String.class);
		System.out.println(x+" " + Thread.currentThread().getName());
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	private static void callPostAvis(int x) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
			= "http://localhost:8080/avis";

		AvisCreationDto avisCreation=new AvisCreationDto();
		avisCreation.setAuteur("Paul");
		avisCreation.setNote(4l);

		//FIXME à la place trouver une facon de récupérer dynmiquement l'id
		avisCreation.setIdRestaurant(160956l);
		avisCreation.setCommentaire("Pas mal");

		HttpEntity<AvisCreationDto> request = new HttpEntity<>(avisCreation);

		//FixME supprimer system.out
		ResponseEntity<Long> response
			= restTemplate.postForEntity(fooResourceUrl ,  request, Long.class);
		System.out.println(x+" " + Thread.currentThread().getName());
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}
}
