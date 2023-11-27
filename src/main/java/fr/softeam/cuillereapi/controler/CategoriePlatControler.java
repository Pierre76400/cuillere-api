package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.model.CategoriePlat;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.CategoriePlatRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class CategoriePlatControler {

	private final CategoriePlatRepository categoriePlatRepository;

	CategoriePlatControler(CategoriePlatRepository categoriePlatRepository) {
		this.categoriePlatRepository = categoriePlatRepository;
	}

	@GetMapping("/categoriePlat")
	List<CategoriePlat> all() {
		List<CategoriePlat> list = new ArrayList<>();
		categoriePlatRepository.findAll().iterator().forEachRemaining(list::add);
		return list;
	}
}