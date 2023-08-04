package fr.softeam.cuillereapi.api;

import java.util.List;

public class RestaurantDetailDto {
	private Long id;
	private String nom;
	private String adresse;
	private boolean vegetarien;

	private List<PlatDto> plats;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public boolean getVegetarien() {
		return vegetarien;
	}

	public void setVegetarien(boolean vegetarien) {
		this.vegetarien = vegetarien;
	}

	public boolean isVegetarien() {
		return vegetarien;
	}

	public List<PlatDto> getPlats() {
		return plats;
	}

	public void setPlats(List<PlatDto> plats) {
		this.plats = plats;
	}
}
