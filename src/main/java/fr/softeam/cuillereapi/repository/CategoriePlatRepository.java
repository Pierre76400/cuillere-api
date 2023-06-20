package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.CategoriePlat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriePlatRepository
	extends JpaRepository<CategoriePlat, String> {
}