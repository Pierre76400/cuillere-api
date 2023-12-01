package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.AvisCreationDto;
import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.repository.AvisRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.AvisService;
import fr.softeam.cuillereapi.service.DatabaseService;
import fr.softeam.cuillereapi.service.KafkaAvisService;
import io.micrometer.observation.annotation.Observed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Observed(name = "avisControler")
@Transactional
@CrossOrigin("*")
public class AvisControler {

	Logger logger = LoggerFactory.getLogger(AvisControler.class);
	private AvisService avisService;
	private final AvisRepository avisRepository;

	private final KafkaAvisService kafkaAvisService;

	AvisControler(AvisService avisService,AvisRepository avisRepository,KafkaAvisService kafkaAvisService) {
		this.avisService=avisService;
		this.avisRepository = avisRepository;
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
}