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

	//FIXME mettre l'annotation pour que cette méthode soit chargé à chaque fois
	void setup(){
		Restaurant laPuce= ModelHelper.createRestaurantLaPuce();
		restaurantRepository.save(laPuce);

		Restaurant leRipailleur=ModelHelper.createRestaurantLeRipailleur();
		restaurantRepository.save(leRipailleur);

		CategoriePlat cpPrincipal = getCategoriePlatPlatPrincipal("CP", "Plat principal");
		categoriePlatRepository.save(cpPrincipal);

		CategoriePlat cpEntree = getCategoriePlatPlatPrincipal("EN", "Entrée");
		categoriePlatRepository.save(cpEntree);

		Plat boeuf=createPlatBoeufBourguignon(leRipailleur, cpPrincipal);

		Plat steak = createPlatSteakFrite(laPuce, cpPrincipal);

		Plat oeuf = createPlatOeufMayo(leRipailleur, cpEntree);

		platRepository.save(boeuf);
		platRepository.save(steak);
		platRepository.save(oeuf);

	}


	@Test
	void findAll() {
		//setup();
		assertEquals(4,platRepository.findAll().size());
	}


	@Test
	void findByIdRestaurant() {
		//setup();

		assertNotEquals(0,platRepository.findByRestaurantId(1l).size());
	}

}
