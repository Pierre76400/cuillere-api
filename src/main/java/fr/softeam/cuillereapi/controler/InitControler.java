package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.service.DatabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class InitControler {



	private DatabaseService databaseService;

	InitControler(DatabaseService databaseService) {
		this.databaseService = databaseService;
	}

	@GetMapping("/init")
	void init() {
		databaseService.clearDatabase();
		databaseService.initCategoriePlat();
		databaseService.adjectifs_smart.stream().parallel().forEach(databaseService::initByAdjectifSmart);
		databaseService.create_tmp_restaurant_2019();
	}





}