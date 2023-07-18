package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.api.RestaurantDto;
import fr.softeam.cuillereapi.model.Restaurant;
import fr.softeam.cuillereapi.repository.RestaurantRepository;
import fr.softeam.cuillereapi.service.DatabaseService;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class InitControler {



	private DatabaseService databaseService;

	InitControler(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@GetMapping("/init")
	void init() {
		databaseService.clearDatabase();
		databaseService.init();
	}



}