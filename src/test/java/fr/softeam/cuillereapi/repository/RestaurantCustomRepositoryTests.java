package fr.softeam.cuillereapi.repository;


import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.model.Restaurant;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CuillereApiApplication.class)
class RestaurantCustomRepositoryTests {
	@Autowired
	private RestaurantCustomRepository restaurantCustomRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Test
	void getDetailsById() {
		assertEquals("Le coq de la maison blanche",restaurantCustomRepository.getDetailsById(1l).getNom());
	}

}
