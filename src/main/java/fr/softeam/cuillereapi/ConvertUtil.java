package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.api.RestaurantDetailDto;
import fr.softeam.cuillereapi.api.RestaurantDto;
import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.model.Plat;
import fr.softeam.cuillereapi.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtil {


	public static  RestaurantDto restaurantEntityToDto(Restaurant r) {
		RestaurantDto dto=new RestaurantDto();
		dto.setId(r.getId());
		dto.setNom(r.getNom());
		dto.setAdresse(r.getAdresse());
		dto.setVegetarien(r.getVegetarien().equals("OUI")?true:false);

		return dto;
	}


	public static  RestaurantDetailDto restaurantEntityToDetailDto(Restaurant r) {
		RestaurantDetailDto dto=new RestaurantDetailDto();
		dto.setId(r.getId());
		dto.setNom(r.getNom());
		dto.setAdresse(r.getAdresse());
		dto.setVegetarien(r.getVegetarien().equals("OUI")?true:false);

		List<PlatDto> plats=new ArrayList<>();
		r.getPlats().forEach(p->plats.add(ConvertUtil.platEntityToDto(p)));
		dto.setPlats(plats);

		return dto;
	}

	public static PlatDto platEntityToDto(Plat p) {
		PlatDto dto = new PlatDto();

		dto.setCategoriePlat(p.getCategoriePlat().getCode());
		dto.setLibelleCategoriePlat(p.getCategoriePlat().getLibelle());
		dto.setLibelle(p.getLibelle());
		dto.setPrix(p.getPrix());

		return dto;
	}

	public static AvisDto avisEntityToDto(Avis avis){
		AvisDto dto=new AvisDto();

		dto.setAuteur(avis.getAuteur());
		dto.setCommentaire(avis.getCommentaire());
		dto.setNote(avis.getNote());
		dto.setDateCreation(avis.getDateCreation());

		return dto;
	}
}
