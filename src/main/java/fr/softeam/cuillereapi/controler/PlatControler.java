package fr.softeam.cuillereapi.controler;

import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.service.PlatService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class PlatControler {

	private final PlatService platService;

	PlatControler(PlatService platService) {
		this.platService = platService;
	}

	@GetMapping("/plats")
	List<PlatDto> all() {
		return platService.getAll();
	}


	@GetMapping("/plats/{idPlat}")
	PlatDto getPlat(@PathVariable Long idPlat) {
		return platService.getPlat(idPlat);
	}
}