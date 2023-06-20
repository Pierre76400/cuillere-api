package fr.softeam.cuillereapi.repository;


import fr.softeam.cuillereapi.model.Restaurant;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RestaurantRepositoryTests {
	@Autowired
	private RestaurantRepository restaurantRepository;

	//FIXME mettre l'annotation pour que cette méthode soit chargé à chaque fois
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
	void findByNom() {
		setup();

		assertEquals(1,restaurantRepository.findByNom("La carotte").size());
		assertEquals(0,restaurantRepository.findByNom("La Carotte").size());
	}

}
