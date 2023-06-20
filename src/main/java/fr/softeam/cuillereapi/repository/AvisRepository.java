package fr.softeam.cuillereapi.repository;

import fr.softeam.cuillereapi.model.Avis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisRepository extends JpaRepository<Avis, Long> {
}