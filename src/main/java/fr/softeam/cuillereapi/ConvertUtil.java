package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.AvisDto;
import fr.softeam.cuillereapi.api.PlatDto;
import fr.softeam.cuillereapi.model.Avis;
import fr.softeam.cuillereapi.model.Plat;

public class ConvertUtil {

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

		return dto;
	}
}
