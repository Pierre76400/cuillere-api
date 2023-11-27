package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.service.DatabaseService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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