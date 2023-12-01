package fr.softeam.cuillereapi.repository;


import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.model.Restaurant;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = CuillereApiApplication.class)
class RestaurantRepositoryTests {
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Test
	void findByNom() {
		assertEquals(1,restaurantRepository.findByNom("Le coq de la maison blanche").size());
		assertEquals(0,restaurantRepository.findByNom("Le COQ de la maison blanche").size());
	}

}
