package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.AvisCreationDto;
import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.repository.AvisRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.AvisService;
import fr.softeam.cuillereapi.service.KafkaAvisService;
import io.micrometer.observation.annotation.Observed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//FIXME il faudrait placer ce controler sous la ressource restaurant (cela n'a pas trop de sens de la mettre à la racine)
@RestController
@Observed(name = "avisControler")
public class AvisControler {

	private AvisService avisService;
	private final AvisRepository avisRepository;
	private final RestaurantRepository restaurantRepository;

	private final KafkaAvisService kafkaAvisService;


	AvisControler(AvisService avisService,AvisRepository avisRepository,
				  RestaurantRepository restaurantRepository,KafkaAvisService kafkaAvisService) {
		this.avisService=avisService;
		this.avisRepository = avisRepository;
		this.restaurantRepository = restaurantRepository;
		this.kafkaAvisService=kafkaAvisService;
	}

	@GetMapping("/avis")
	List<AvisDto> all() {
		List<AvisDto> aviss = new ArrayList<>();
		avisRepository.findAll().iterator().forEachRemaining(avis -> aviss.add(ConvertUtil.avisEntityToDto(avis)));
		return aviss;
	}

	@GetMapping("/avis/_count")
	long count() {
		return avisRepository.count();
	}
	@PostMapping("/avis")
	public ResponseEntity<Long> ajouterAvis(@RequestBody AvisCreationDto avisCreationDto) {
		return new ResponseEntity<>(avisService.creerAvis(avisCreationDto), HttpStatus.CREATED);
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

	@PostMapping("/avis/kafka")
	//Rajouter un message spécificique (avec tt les infos sur l'avis) sur un topic dédié
	public void sendKafkaMessage(@RequestBody AvisCreationDto avisCreationDto) {
		kafkaAvisService.sendMessage(avisCreationDto);
	}

	@KafkaListener(topics = "cuillere-avis")
	public void listenGroupFoo(AvisCreationDto message) {
		System.out.println("Received Message in group foo: " + message.getAuteur());
		avisService.creerAvis(message);
	}
	//FIXME test consumer
	//FIXME menage kafka
}