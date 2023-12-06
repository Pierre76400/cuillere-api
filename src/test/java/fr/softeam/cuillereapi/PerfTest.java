package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.AvisCreationDto;
import fr.softeam.cuillereapi.api.RechercheRestaurantDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Disabled("Faux tests unitaire pour les besoins de la démo")
class PerfTest {
	static Logger logger = LoggerFactory.getLogger(PerfTest.class);

	@Test
	void test(){
		long idRestau=getPremierIdRestaurant();
		callGetRestaurant(1,idRestau);
	}

	//
	@Test
	void testParrallelGetRestaurant(){
		long idRestau=getPremierIdRestaurant();
		AtomicInteger cpt= new AtomicInteger();
		IntStream.range(0, 10_000_000).parallel().forEach(x->callGetRestaurant(cpt.getAndIncrement(), idRestau));
	}


	@Test
	void testCreerAvis(){
		long idRestau=getPremierIdRestaurant();
		callPostAvis(1, idRestau);
	}

	@Test
	void testParrallelCreerAvis(){
		long idRestau=getPremierIdRestaurant();
		AtomicInteger cpt= new AtomicInteger();
		IntStream.range(0, 100).parallel().forEach(x->callPostAvis(cpt.getAndIncrement(),idRestau));
	}

	@Test
	void testRecupererPremierRestaurant(){
		logger.info("Premier id restaurant = "+getPremierIdRestaurant());
	}

	private static void callGetRestaurant(int cpt, long idRestau) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://localhost:8080/restaurants/"+idRestau;
		ResponseEntity<String> response
			= restTemplate.getForEntity(fooResourceUrl , String.class);
		logger.info("cpt "+cpt);
		Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
	}

	private static void callPostAvis(int cpt, long idRestau) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl
			= "http://localhost:8080/avis";

		AvisCreationDto avisCreation=new AvisCreationDto();
		avisCreation.setAuteur("Paul");
		avisCreation.setNote(4l);

		avisCreation.setIdRestaurant(idRestau);
		avisCreation.setCommentaire("Pas mal");

		HttpEntity<AvisCreationDto> request = new HttpEntity<>(avisCreation);

		ResponseEntity<Long> response
			= restTemplate.postForEntity(fooResourceUrl ,  request, Long.class);
		logger.info("cpt "+cpt);
		Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}

	private Long getPremierIdRestaurant(){
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "http://localhost:8080/restaurants/_search?nomRestaurant=assembleur";
		ResponseEntity<RechercheRestaurantDto> response
				= restTemplate.getForEntity(fooResourceUrl , RechercheRestaurantDto.class);

		Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
		return response.getBody().getRestaurants().get(0).getId();
	}
}
