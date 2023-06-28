package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.AvisRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//FIXME il faudrait placer ce controler sous la ressource restaurant (cela n'a pas trop de sens de la mettre à la racine)
@RestController
public class AvisControler {

	private final AvisRepository avisRepository;

	AvisControler(AvisRepository avisRepository) {
		this.avisRepository = avisRepository;
	}

	@GetMapping("/avis")
	List<Avis> all() {
		List<Avis> list = new ArrayList<>();
		avisRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
}