package fr.softeam.cuillereapi.repository;


import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.util.ModelHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static fr.softeam.cuillereapi.util.ModelHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(classes = CuillereApiApplication.class)
class PlatRepositoryTests {
	@Autowired
	private PlatRepository platRepository;

	@Autowired
	private CategoriePlatRepository categoriePlatRepository;
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Test
	void findAll() {
		assertEquals(4,platRepository.findAll().size());
	}


	@Test
	void findByIdRestaurant() {
		assertNotEquals(0,platRepository.findByRestaurantId(1l).size());
	}

}
