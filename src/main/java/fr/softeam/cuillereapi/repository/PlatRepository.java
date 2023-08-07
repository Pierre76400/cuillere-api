package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlatRepository extends JpaRepository<Plat, Long> {

	List<Plat> findByRestaurantId(Long idRestaurant);
}