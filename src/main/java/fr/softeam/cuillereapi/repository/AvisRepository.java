package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AvisRepository extends JpaRepository<Avis, Long> {
	List<Avis>  findByDateCreationLessThan(LocalDate date);
	Long deleteByDateCreationLessThan(LocalDate date);

	@Modifying
	@Query("delete from Avis a where a.dateCreation<=:date")
	Integer deleteBulkByDateCreationLessThan(@Param("date") LocalDate date);

	List<Avis> findByRestaurantId(long restaurantId);
}