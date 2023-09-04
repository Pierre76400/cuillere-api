package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.AvisRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
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
	List<AvisDto> all() {
		List<AvisDto> aviss = new ArrayList<>();
		avisRepository.findAll().iterator().forEachRemaining(avis -> aviss.add(ConvertUtil.avisEntityToDto(avis)));
		return aviss;
	}

	@DeleteMapping("/avis/_obsoletes")
	public void supprimeAvisObsoletes() {
		LocalDate datePurge=LocalDate.now().minusYears(5);
		avisRepository.findByDateCreationLessThan(datePurge).iterator().forEachRemaining(avis -> avisRepository.delete(avis));
	}

	//FIXME le bulk ci dessous ne marche pas , faut il garder cette implémentation ou celle ci dessus ?
	@DeleteMapping("/avis/_obsoletesBULKQuiNeMarchePAs")
	@Transactional
	public void supprimeAvisObsoletesBulk() {
		LocalDate datePurge=LocalDate.now().minusYears(5);
		avisRepository.deleteByDateCreationLessThan(datePurge);
	}

	@DeleteMapping("/avis/_obsoletesBULK")
	@Transactional
	public void supprimeAvisObsoletesBulk2() {
		LocalDate datePurge=LocalDate.now().minusYears(5);
		avisRepository.deleteBulkByDateCreationLessThan(datePurge);
	}
}