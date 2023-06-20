package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantControler {

	private final RestaurantRepository restaurantRepository;

	RestaurantControler(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@GetMapping("/restaurants")
	List<Restaurant> all() {
		List<Restaurant> list = new ArrayList<>();
		restaurantRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
}