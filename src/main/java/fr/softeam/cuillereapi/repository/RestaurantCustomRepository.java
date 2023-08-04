package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Restaurant;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantCustomRepository {

	private EntityManager entityManager;

	public RestaurantCustomRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Restaurant getDetailsById(Long idRestaurant) {
		return (Restaurant) entityManager.createQuery("from Restaurant where id=:idRestaurant")
										 .setParameter("idRestaurant", idRestaurant).getSingleResult();
	}
}
