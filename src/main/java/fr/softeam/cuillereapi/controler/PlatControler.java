package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.PlatRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlatControler {

	private final PlatRepository platRepository;

	PlatControler(PlatRepository platRepository) {
		this.platRepository = platRepository;
	}

	@GetMapping("/plats")
	List<Plat> all() {
		List<Plat> list = new ArrayList<>();
		platRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
}