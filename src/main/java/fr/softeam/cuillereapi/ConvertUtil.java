package fr.softeam.cuillereapi;

import fr.softeam.cuillereapi.api.PlatDto;
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
}
