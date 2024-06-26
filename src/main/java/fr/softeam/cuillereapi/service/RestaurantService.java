package fr.softeam.cuillereapi.service;

import fr.softeam.cuillereapi.api.Dto;
import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.api.RestaurantDto;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantCustomRepository;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static fr.softeam.cuillereapi.ConvertUtil.restaurantEntityToDetailDto;
import static fr.softeam.cuillereapi.ConvertUtil.restaurantEntityToDto;

@Service
public class RestaurantService {

	private static final double REDUCTION_FETES_DES_MERES = 10.0;
	private RestaurantCustomRepository repository;
	private final RestaurantRepository restaurantRepository;

	private EntityManager entityManager;

	public RestaurantService(RestaurantCustomRepository repository,
							 RestaurantRepository restaurantRepository,
							 EntityManager entityManager) {
		this.repository = repository;
		this.restaurantRepository = restaurantRepository;
		this.entityManager = entityManager;
	}

	public List<RestaurantDto> rechercherRestaurantPagine(String nomRestaurant, int numPage, int taillePage) {
		Pageable pageable= PageRequest.of(numPage,taillePage);

		return restaurantRepository.findByNomContainingIgnoreCaseOrderById(nomRestaurant,pageable).stream().map(r->restaurantEntityToDto(r)).collect(Collectors.toList());
	}

	public RestaurantDetailDto getRestaurant(Long idRestaurant) {
		return restaurantEntityToDetailDto(repository.getDetailsById(idRestaurant));
	}

	public long compterRestaurant(String nomRestaurant) {
		return restaurantRepository.countByNomContainingIgnoreCase(nomRestaurant);
	}

	public List<RestaurantDto> rechercherRestaurant(String nomRestaurant) {
		if(nomRestaurant==null || nomRestaurant.isEmpty()){
			return new ArrayList<>();
		}
		return restaurantRepository.findByNomContainingIgnoreCase(nomRestaurant).stream().map(r->restaurantEntityToDto(r)).collect(Collectors.toList());
	}

	public RestaurantDetailDto getRestaurantFeteDesMeres2019(Long idRestaurant) {
		RestaurantDetailDto rest=restaurantEntityToDetailDto(repository.getDetailsById(idRestaurant));

		rest.getPlats().stream().forEach(p->p.setPrix((p.getPrix()-(p.getPrix()/100)*REDUCTION_FETES_DES_MERES)));

		return rest;
	}

    public Dto get(Long id, double lo, double la, boolean english) {
		double ray ;
		double dLa;
		double dLo;

		double lat2;

		if(english){
			ray=3958.8d;
		}
		else{
			ray=6371d;
		}

		Query query = entityManager.createNativeQuery("select id,nom,adresse,vegetarien,la,lo from restaurant where id=" + id);
		Object res = query.getSingleResult();
		List<Restaurant> restaurants = new ArrayList<>();

		double lat1 = Math.toRadians(la);

		Object[] objs = (Object[]) res;
		Restaurant r = new Restaurant();
		r.setId(((Number) objs[0]).longValue());
		r.setNom(objs[1].toString());
		r.setAdresse(objs[2].toString());
		r.setVegetarien(objs[3].toString());
		r.setLa(((Number) objs[4]).doubleValue());
		r.setLo(((Number) objs[5]).doubleValue());

		Dto dto=new Dto();
		dto.setId(r.getId());
		dto.setNom(r.getNom());
		dto.setAdresse(r.getAdresse());
		dto.setVegetarien(r.getVegetarien().equals("OUI")?true:false);

		dLa= Math.toRadians(r.getLa()- la);
		dLo = Math.toRadians(r.getLo() - lo);

		lat2 = Math.toRadians(r.getLa());

		double a = Math.sin(dLa / 2) * Math.sin(dLa / 2) +
				Math.sin(dLo/ 2) * Math.sin(dLo / 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		dto.setD(ray * c);

		return dto;
    }
}
