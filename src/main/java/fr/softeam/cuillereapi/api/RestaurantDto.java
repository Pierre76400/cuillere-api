package fr.softeam.cuillereapi.api;

public class RestaurantDto {
	private Long id;
	private String nom;
	private String adresse;
	private boolean vegetarien;

	private Integer nbAvis;

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

	public Integer getNbAvis() {
		return nbAvis;
	}

	public void setNbAvis(Integer nbAvis) {
		this.nbAvis = nbAvis;
	}
}
