package fr.softeam.cuillereapi.model;


import jakarta.persistence.*;

import java.util.List;

@Entity
public class Restaurant {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	private String nom;

	private String adresse;

	private String vegetarien;

	@OneToMany(mappedBy = "restaurant")
	private List<Avis> avis;

	@OneToMany(mappedBy = "restaurant")
	private List<Plat> plats;

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

	public String getVegetarien() {
		return vegetarien;
	}

	public void setVegetarien(String vegetarien) {
		this.vegetarien = vegetarien;
	}

	public List<Avis> getAvis() {
		return avis;
	}

	public void setAvis(List<Avis> avis) {
		this.avis = avis;
	}

	public List<Plat> getPlats() {
		return plats;
	}

	public void setPlats(List<Plat> plats) {
		this.plats = plats;
	}
}
