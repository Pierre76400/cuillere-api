package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.api.RestaurantDto;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class RestaurantControler {

	private final RestaurantRepository restaurantRepository;

	RestaurantControler(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@GetMapping("/restaurants")
	List<RestaurantDto> all() {
		List<RestaurantDto> list = StreamSupport.stream(restaurantRepository.findAll().spliterator(), false)
			.map(r->restaurantEntityToDto(r)).collect(Collectors.toList());
		return list;
	}

	private RestaurantDto restaurantEntityToDto(Restaurant r) {
		RestaurantDto dto=new RestaurantDto();
		dto.setNom(r.getNom());
		dto.setAdresse(r.getAdresse());
		dto.setVegetarien(r.getVegetarien().equals("OUI")?true:false);

		return dto;
	}

}