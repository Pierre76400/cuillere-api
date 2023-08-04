package fr.softeam.cuillereapi.repository;


import fr.softeam.cuillereapi.CuillereApiApplication;
import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CuillereApiApplication.class)
class PlatRepositoryTests {
	@Autowired
	private PlatRepository platRepository;

	@Autowired
	private CategoriePlatRepository categoriePlatRepository;

	//FIXME mettre l'annotation pour que cette méthode soit chargé à chaque fois
	void setup(){
		CategoriePlat cpPrincipal=new CategoriePlat();
		cpPrincipal.setCode("CP");
		cpPrincipal.setLibelle("Plat principal");
		categoriePlatRepository.save(cpPrincipal);

		CategoriePlat cpEntree=new CategoriePlat();
		cpEntree.setCode("EN");
		cpEntree.setLibelle("Entrée");
		categoriePlatRepository.save(cpEntree);

		Plat boeuf=new Plat();
		boeuf.setLibelle("Boeuf bourguignon");
		boeuf.setCategoriePlat(cpPrincipal);
		boeuf.setPrix(10.5d);

		Plat steak=new Plat();
		steak.setLibelle("Steak frite");
		steak.setCategoriePlat(cpPrincipal);
		steak.setPrix(12.5d);

		Plat oeuf=new Plat();
		oeuf.setLibelle("Oeuf mayo");
		oeuf.setCategoriePlat(cpEntree);
		oeuf.setPrix(2.5d);

		platRepository.save(boeuf);
		platRepository.save(steak);
		platRepository.save(oeuf);

	}

	@Test
	void findAll() {
		setup();

		assertEquals(3,platRepository.findAll().size());
	}

}
