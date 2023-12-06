package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.ConvertUtil;
import fr.softeam.cuillereapi.api.*;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.AvisService;
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
	private final AvisService avisService;

	RestaurantControler(RestaurantRepository restaurantRepository, RestaurantService restaurantService,PlatService platService,AvisService avisService) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantService = restaurantService;
		this.platService = platService;
		this.avisService = avisService;
	}

	@GetMapping("/restaurants")
	List<RestaurantDto> all() {
		return StreamSupport.stream(restaurantRepository.findAll().spliterator(), false)
				.map(r-> ConvertUtil.restaurantEntityToDto(r)).toList();
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

	@GetMapping("/restaurants/{idRestaurant}/avis")
	List<AvisDto> getAvis(@PathVariable Long idRestaurant) {
		return avisService.getAvis(idRestaurant);
	}

	@GetMapping("/restaurants/_search")
	RechercheRestaurantDto rechercherRestaurant(@RequestParam String  nomRestaurant) {
		RechercheRestaurantDto result=new RechercheRestaurantDto();
		result.setRestaurants(restaurantService.rechercherRestaurant(nomRestaurant));
		result.setNbResultat(result.getRestaurants().size());
		return result;
	}
}