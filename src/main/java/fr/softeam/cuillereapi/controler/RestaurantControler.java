package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.api.RestaurantAvecInfoComplementaireDto;
import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.api.RestaurantDto;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.PlatService;
import fr.softeam.cuillereapi.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class RestaurantControler {

	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;
	private final PlatService platService;

	RestaurantControler(RestaurantRepository restaurantRepository, RestaurantService restaurantService,PlatService platService) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantService = restaurantService;
		this.platService = platService;
	}

	@GetMapping("/restaurants")
	List<RestaurantDto> all() {
		List<RestaurantDto> list = StreamSupport.stream(restaurantRepository.findAll().spliterator(), false)
												.map(r-> ConvertUtil.restaurantEntityToDto(r)).collect(Collectors.toList());
		return list;
	}

	@GetMapping("/restaurants/{idRestaurant}")
	RestaurantDetailDto getRestaurant(@PathVariable Long idRestaurant) {
		//TODO mettre en place la gestion des erreurs
		return restaurantService.getRestaurant(idRestaurant);
	}

	@GetMapping("/restaurants/{idRestaurant}/details")
	RestaurantAvecInfoComplementaireDto getRestaurantDetail(@PathVariable Long idRestaurant,@RequestParam double lo,@RequestParam double la,@RequestParam boolean english) {
		return restaurantService.getRestaurantDetail(idRestaurant,lo,la,english);
	}

	@GetMapping("/restaurants/{idRestaurant}/plats")
	List<PlatDto> getPlats(@PathVariable Long idRestaurant) {
		return platService.getPlats(idRestaurant);
	}

	//TODO Est ce que l'url est bonne ?
	@GetMapping("/restaurants/_search")
	List<RestaurantDetailDto> rechercherRestaurant(@RequestParam String  nomRestaurant) {
		//TODO mettre en place la gestion des erreurs
		return restaurantService.rechercherRestaurant(nomRestaurant);
	}


	@GetMapping("/restaurants/_search2")
	List<RestaurantDetailDto> rechercherRestaurant(@RequestParam String  nomRestaurant,@RequestParam int numPage,@RequestParam int taillePage) {
		//TODO mettre en place la gestion des erreurs
		return restaurantService.rechercherRestaurant(nomRestaurant,numPage,taillePage);
	}



}