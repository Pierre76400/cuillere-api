package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.*;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.PlatService;
import fr.softeam.cuillereapi.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@CrossOrigin("*")
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
		return StreamSupport.stream(restaurantRepository.findAll().spliterator(), false)
				.map(r-> ConvertUtil.restaurantEntityToDto(r)).collect(Collectors.toList());
	}

	@GetMapping("/restaurants/{idRestaurant}")
	ResponseEntity<RestaurantDetailDto> getRestaurant(@PathVariable Long idRestaurant) {
		RestaurantDetailDto rest=restaurantService.getRestaurant(idRestaurant);

		if(rest==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(rest, HttpStatus.CREATED);
	}

	@GetMapping("/restaurants/{idRestaurant}/details")
	public ResponseEntity<Dto> getRestaurantDetail(@PathVariable Long idRestaurant, @RequestParam double lo, @RequestParam double la, @RequestParam boolean english) {
		Dto rest= restaurantService.get(idRestaurant,lo,la,english);

		if(rest==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(rest, HttpStatus.CREATED);
	}

	@GetMapping("/restaurants/{idRestaurant}/plats")
	List<PlatDto> getPlats(@PathVariable Long idRestaurant) {
		return platService.getPlats(idRestaurant);
	}

	//TODO Est ce que l'url est bonne ?
	@GetMapping("/restaurants/_search")
	RechercheRestaurantDto rechercherRestaurant(@RequestParam String  nomRestaurant) {
		RechercheRestaurantDto result=new RechercheRestaurantDto();
		result.setRestaurants(restaurantService.rechercherRestaurant(nomRestaurant));
		result.setNbResultat(result.getRestaurants().size());
		return result;
	}
}