package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.AvisCreationDto;
import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.repository.AvisRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Observed(name = "avisService")
public class AvisService {

	private RestaurantRepository restaurantRepository;

	private AvisRepository avisRepository;

	public AvisService(RestaurantRepository restaurantRepository, AvisRepository avisRepository) {
		this.restaurantRepository = restaurantRepository;
		this.avisRepository = avisRepository;
	}

	public long creerAvis(AvisCreationDto avisCreationDto){
		Avis entity=new Avis();
		entity.setAuteur(avisCreationDto.getAuteur());
		entity.setCommentaire(avisCreationDto.getCommentaire());
		entity.setNote(avisCreationDto.getNote());
		entity.setRestaurant(restaurantRepository.findById(avisCreationDto.getIdRestaurant()).get());
		entity.setDateCreation(LocalDate.now());

		avisRepository.save(entity);

		return entity.getId();
	}

	public List<AvisDto> getAvis(Long idRestaurant) {
		return avisRepository.findByRestaurantId(idRestaurant).stream().map(ConvertUtil::avisEntityToDto).toList();
	}

}
