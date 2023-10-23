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


	void setup(){
		Restaurant laPuce=new Restaurant();
		laPuce.setAdresse("12 rue Ernest Renan");
		laPuce.setNom("La Puce");
		laPuce.setVegetarien("Non");

		Restaurant leRipailleur=new Restaurant();
		leRipailleur.setAdresse("3 rue Emile Cordon");
		leRipailleur.setNom("Le Ripailleur");
		leRipailleur.setVegetarien("Non");

		Restaurant laCarotte=new Restaurant();
		laCarotte.setAdresse("3 rue des prés");
		laCarotte.setNom("La carotte");
		laCarotte.setVegetarien("Oui");

		restaurantRepository.save(laPuce);
		restaurantRepository.save(leRipailleur);
		restaurantRepository.save(laCarotte);

	}

	@Test
	void getDetailsById() {
		//setup();

		assertEquals("Le coq de la maison blanche",restaurantCustomRepository.getDetailsById(1l).getNom());
	}

}
