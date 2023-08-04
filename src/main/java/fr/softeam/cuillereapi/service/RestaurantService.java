package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantCustomRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

	private RestaurantCustomRepository repository;

	public RestaurantService(RestaurantCustomRepository repository) {
		this.repository = repository;
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
		r.getPlats().forEach(p->plats.add(platEntityToDto(p)));
		dto.setPlats(plats);

		return dto;
	}

	private PlatDto platEntityToDto(Plat p) {
		PlatDto dto = new PlatDto();

		dto.setCategoriePlat(p.getCategoriePlat().getCode());
		dto.setLibelle(p.getLibelle());
		dto.setPrix(p.getPrix());

		return dto;
	}
}
