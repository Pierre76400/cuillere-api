package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantCustomRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

	private static final double REDUCTION_FETES_DES_MERES = 10.0;
	private RestaurantCustomRepository repository;
	private final RestaurantRepository restaurantRepository;

	public RestaurantService(RestaurantCustomRepository repository,
							 RestaurantRepository restaurantRepository) {
		this.repository = repository;
		this.restaurantRepository = restaurantRepository;
	}

	public RestaurantDetailDto getRestaurant(Long idRestaurant) {
		return restaurantEntityToDetailDto(repository.getDetailsById(idRestaurant));
	}

	private RestaurantDetailDto restaurantEntityToDetailDto(Restaurant r) {
		RestaurantDetailDto dto=new RestaurantDetailDto();
		dto.setNom(r.getNom());
		dto.setAdresse(r.getAdresse());
		dto.setVegetarien(r.getVegetarien().equals("OUI")?true:false);

		List<PlatDto> plats=new ArrayList<>();
		r.getPlats().forEach(p->plats.add(ConvertUtil.platEntityToDto(p)));
		dto.setPlats(plats);

		return dto;
	}

	public List<RestaurantDetailDto> rechercherRestaurant(String nomRestaurant) {
		return restaurantRepository.findByNomContainingIgnoreCase(nomRestaurant).stream().map(r->restaurantEntityToDetailDto(r)).collect(Collectors.toList());
	}


	public List<RestaurantDetailDto> rechercherRestaurant(String nomRestaurant,int numPage,int taillePage) {
		Pageable pageable= PageRequest.of(numPage,taillePage);
		return restaurantRepository.findByNomContainingIgnoreCase(nomRestaurant,pageable).stream().map(r->restaurantEntityToDetailDto(r)).collect(Collectors.toList());
	}


	public RestaurantDetailDto getRestaurantFeteDesMeres2019(Long idRestaurant) {
		RestaurantDetailDto rest=restaurantEntityToDetailDto(repository.getDetailsById(idRestaurant));

		rest.getPlats().stream().forEach(p->p.setPrix((p.getPrix()-(p.getPrix()/100)*REDUCTION_FETES_DES_MERES)));

		return rest;
	}
}
