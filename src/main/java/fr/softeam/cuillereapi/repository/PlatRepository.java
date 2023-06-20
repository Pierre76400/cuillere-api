package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Plat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatRepository extends JpaRepository<Plat, Long> {
}