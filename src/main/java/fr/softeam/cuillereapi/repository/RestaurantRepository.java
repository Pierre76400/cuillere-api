package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

	List<Restaurant> findByNom(String lastName);

	Restaurant findById(long id);
}